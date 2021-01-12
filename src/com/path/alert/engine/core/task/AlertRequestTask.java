package com.path.alert.engine.core.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.path.alert.bo.notification.AlertNotificationBO;
import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.logger.AlertEngLogger;
import com.path.alert.engine.utils.AlertEngUtils;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.engine.utils.StopWatch;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.alert.vo.notification.EvtCO;
import com.path.bo.common.ConstantsCommon;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

import net.sf.json.JSONObject;

/**
 * @author MohammadAliMezzawi
 *
 */
public class AlertRequestTask
{

    /**
     * RequestVO
     */
    private ALRT_ENG_REQUESTVO requestVO = new ALRT_ENG_REQUESTVO();

    /**
     * Alert Notification request
     */
    private AlertNotificationCO notificationCO;

    /**
     * 
     */
    private int nbOfReceiver;

    /**
     * List of tasklet
     */
    @SuppressWarnings("rawtypes")
    private List<AlertRequestTasklet> listOfTasklet;

    /**
     * 
     */
    private int offSet;

    /**
     * 
     */
    private BigDecimal chunkSize;

    /**
     * Flag to define if the request will be handled in SynchMode
     */
    private boolean synchMode;

    /**
     * Hold logger reference
     */
    private final Log logger = Log.getInstance();
    
    /**
     * Error Message
     */
    private String errorMsg = "";
    
	/**
	 * Process the alert Eng Request
	 * 
	 * @param notificationCO
	 */
	public boolean processRequest()
	{
		boolean result = false;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("AlertRequestTask.processRequest", "Start process Request");

		try {

			logger.debug(formatLog("Start process Request", null));

			// prepare the request main info
			getRequestVO().setEVENT_ID(notificationCO.getEventId());
			getRequestVO().setSTATUS(AlertEngineConstants.STATUS_NEW_REQUEST);
			getRequestVO().setREQUEST_BODY(JSONObject.fromObject(notificationCO).toString());
			getRequestVO().setMACHINE_NAME_IP(AlertEngUtils.getComputerName());

			// log the request
			result = AlertEngLogger.getInstance().logEngRequest(getRequestVO());

			/**
			 * Request body will not be logged due to its size
			 */
			logger.debug(formatLog(String.format("process Request logged Successfully: [%b]", result), getRequestVO()));

			// failed to log the request
			if (!result) {
				errorMsg = "Failed to log request";
				return false;
			}
				
			// execute task only if logging succeed
			result &= submitTasklet();

		} catch (Exception e) {

			// mark request as failed
			result = false;
			errorMsg = "Error while processRequest";
			logger.error(e, formatLog("Error while processRequest", getRequestVO()));

		} finally {

			try {

				// Update request status
				getRequestVO().setSTATUS(result ? AlertEngineConstants.STATUS_PROCESSED_REQUEST
						: AlertEngineConstants.STATUS_FAILED_REQUEST);
				getRequestVO().setOUTPUT_MSG(errorMsg);

				AlertEngLogger.getInstance().updateRequestStatus(getRequestVO());

				logger.debug(formatLog(String.format("process Request Executed : [%b] In [ %s ]", result,
						stopWatch.returnDuration("AlertRequestTask.processRequest")), getRequestVO()));

			} catch (Exception e) {
				logger.error(e, formatLog("Error while Updating request status", getRequestVO()));
			}
		}

		return result;
	}
    

    /**
     * Execute the tasklet list
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
	private boolean submitTasklet()
    {
		try {
			logger.debug(formatLog("submitTasklet : Start Prepare Tasklet", getRequestVO()));

			/**
			 * Check if batch event to parse data
			 */
			AlertNotificationBO alertNotificationBO = (AlertNotificationBO) ApplicationContextProvider
					.getApplicationContext().getBean("alertNotificationBO");
			
			// in case of batch the notification properties will be populate viathe BO
			EvtCO evtCO = alertNotificationBO.selectEvtDetails(notificationCO);
			
			
			if (null == evtCO) {
				errorMsg = "Event not found";
				return false;
			}else if (!AlertNotificationConstant.EVENT_STATUS_APPROVED
					.equals(StringUtil.nullToEmpty(evtCO.getStatus()))) {
				// 844780 - check if event is approved
				errorMsg = "Event not Approved";
				return false;
			}
			
			/**
			 * An event can operate on one company and since the company code could be null in the notificationCO
			 * we are populating it at this level to ensure that it will be present in the remain part of the flow.
			 */
			notificationCO.setCompCode(evtCO.getCompCode());
			
			/**
			 * check if Group List is send the retrieve all subscriber ids and set into notification co
			 */
			if(notificationCO.getGroupIdsList() != null && notificationCO.getGroupIdsList().size() > 0)
			{
			    int rows = alertNotificationBO.countIfIncludeAllSubInGroup(notificationCO);
			    if(rows > 0)
			    {
				notificationCO.setIncludeAllSubInGrpYN(ConstantsCommon.YES);
			    }
			    else
			    {
				notificationCO.setIncludeAllSubInGrpYN(ConstantsCommon.NO);
			    }
			    this.setNotificationCO(alertNotificationBO.returnSubscribersInGroup(notificationCO));
			}
			else
			{
			    this.setNotificationCO(alertNotificationBO.returnListOfBatchSubscriberById(notificationCO,evtCO));
			}
			
			/**
			 * Set loaded event detail into notification co to make sure that it will be passed to AlertNtfCo at the tasklet level
			 * @author Alim Khowaja/Mezzawi
			 */
			notificationCO.setEvtCO(evtCO);
			// prepare the tasklet
			if (!prepareTasklet()) {
				return false;
			}
			// get the maximum instantly receiver
			BigDecimal maxInstantlyReceiver = AlertEngine.getInstance().getConfiguration().getReqProcessingPolicy()
					.getMaxInstantlyReceiver();

			// @note we may implement for instantly here
			if (nbOfReceiver > maxInstantlyReceiver.intValue() || getNotificationCO().isBulkMode()) {
				logger.debug(formatLog(String.format("Tasklet Submitted to Bulk Queue #Rec : [%d] , BulkMode : [%b]",
						nbOfReceiver, getNotificationCO().isBulkMode()), getRequestVO()));

				return AlertEngine.getInstance().getBulkMsgContainer()
						.submit((List<AlertRequestTasklet>) listOfTasklet);

			}

			logger.debug(formatLog(
					String.format("Tasklet Submitted to Instantly Queue #Rec : [%d] , BulkMode : false", nbOfReceiver),
					getRequestVO()));

			// else submit to the instantly container
			return AlertEngine.getInstance().getInstantMsgContainer().submit((List<AlertRequestTasklet>) listOfTasklet);

		} 
		catch (BOException e) {
		    //844780 - in this handler we catch the business exception
		    errorMsg = e.getMessage();
		    logger.error(e, formatLog("Error while submitTasklet", getRequestVO()));
		}
		catch (Exception e) {
			logger.error(e, formatLog("Error while submitTasklet", getRequestVO()));
		}

		return false;
	}
    

    /**
	 * Prepare the needed tasklet
	 * 
	 * @param notificationCO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean prepareTasklet()
	{
		boolean result = true;

		try {
			
			// get chunk size
			chunkSize = AlertEngine.getInstance().getConfiguration().getReqProcessingPolicy().getChunkSize();
			
			listOfTasklet = new ArrayList<AlertRequestTasklet>();

			// initialize offset and receiver nb
			setOffSet(1);
			setNbOfReceiver(0);

			// get list of type
			HashMap<String, String> listOfType = returnListOfType();

			// prepare tasklet
			Iterator it = listOfType.entrySet().iterator();
			while (it.hasNext()) {
				Entry thisEntry = (Entry) it.next();
				String receiverType = (String) thisEntry.getKey();
				String methodName = (String) thisEntry.getValue();

				result &= prepareTaskletByType(
						(List<?>) notificationCO.getClass().getMethod("get" + methodName).invoke(notificationCO),
						receiverType);

				/**
				 * if the process of one tasklet failed we shouldn't process the
				 * other
				 */
				if (!result) {
					break;
				}
			}

		} catch (Exception e) {
			logger.error(e, formatLog("Error while prepareTasklet", getRequestVO()));
			result = false;
		}

		return result;

	}

    
    /**
     * Prepare tasklet by Receiver type
     * 
     * @param listOfReceiver
     */
	private <V> boolean prepareTaskletByType(List<V> listOfReceiver, String receiverType) {
		boolean result = true;
		try {
			// list is empty
//			if (listOfReceiver == null || listOfReceiver.size() <= 0)
			if (listOfReceiver == null || listOfReceiver.isEmpty()) {
				return result;
			}
			List<List<V>> receiverIdChunks = AlertEngUtils.chunkList(listOfReceiver, chunkSize.intValue());

			/**
			 * When send the notification via rmi or via webservice the list of
			 * receiver/cif/email could be null
			 */
			List<List<BigDecimal>> alrtReceiverIdChunks = notificationCO.getReceiverList() != null
					&& notificationCO.getReceiverList().get(receiverType) != null
					&& notificationCO.getReceiverList().get(receiverType).size() > 0
							? AlertEngUtils.chunkList(notificationCO.getReceiverList().get(receiverType),
									chunkSize.intValue())
							: null;

			for (int i = 0; i < receiverIdChunks.size(); i++) {
				List<V> receiverIdChunk = receiverIdChunks.get(i);
				AlertRequestTasklet<V> tasklet = new AlertRequestTasklet<V>();
				tasklet.setMsgIdOffset(offSet);
				tasklet.setReceiverIds(receiverIdChunk);
				tasklet.setReceiverType(receiverType);

				if (null != alrtReceiverIdChunks && alrtReceiverIdChunks.get(i) != null) {
					tasklet.setListOfAlrtReceivers(alrtReceiverIdChunks.get(i));
				}
				tasklet.setParentRequest(this);

				offSet += receiverIdChunk.size();
				nbOfReceiver += receiverIdChunk.size();
				listOfTasklet.add(tasklet);
			}

		} catch (Exception e) {
			logger.error(e, formatLog("Error while prepareTaskletByType => " + receiverType, getRequestVO()));
			result = false;
		}

		return result;
	}
    
    

	/**
	 * This method will return a Mapping between receiver type and the method
	 * that should be invoked on Notification co to get the list of receiver of
	 * that type
	 * 
	 * @return
	 */
	private HashMap<String, String> returnListOfType() {
		HashMap<String, String> listOfType = new HashMap<String, String>();
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_SUB, "SubscriberIdsList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_CIF, "CifList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_USER_ID, "ImalUserIdsList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_CHANNEL, "ChannelUserIdsList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT, "AccountList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_MAIL, "EmailsList");
		listOfType.put(AlertNotificationConstant.RECEIVER_TYPE_MOBILE, "MobilesList");
		return listOfType;
	}

    
    /**
     * @return the requestVO
     */
    public ALRT_ENG_REQUESTVO getRequestVO()
    {
	return requestVO;
    }

    /**
     * @param requestVO the requestVO to set
     */
    public void setRequestVO(ALRT_ENG_REQUESTVO requestVO)
    {
	this.requestVO = requestVO;
    }

    public AlertNotificationCO getNotificationCO()
    {
	return this.notificationCO;
    }

    public void setNotificationCO(AlertNotificationCO notificationCO)
    {
	this.notificationCO = notificationCO;
    }

    public int getNbOfReceiver()
    {
	return nbOfReceiver;
    }

    public void setNbOfReceiver(int nbOfReceiver)
    {
	this.nbOfReceiver = nbOfReceiver;
    }

    public boolean isSynchMode()
    {
	return synchMode;
    }

    /**
     * @param synchMode
     */
    public void setSynchMode(boolean synchMode)
    {
	this.synchMode = synchMode;
    }

    public int getOffSet()
    {
	return offSet;
    }

    public void setOffSet(int offSet)
    {
	this.offSet = offSet;
    }

    /**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}


	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	/**
     * Custom logging function
     * 
     * @param alrt_ENG_REQUESTVO
     * @param string
     */
    private String formatLog(String message, ALRT_ENG_REQUESTVO requestVO)
    {

	StringBuilder sb = new StringBuilder();
	sb.append("[AlertEngine][AlertRequestTask] ").append(this);

	if(requestVO != null)
	{
	    sb.append(String.format("Request ID: [%d] , Event ID: [%d]", getRequestVO().getREQUEST_ID().intValue(),
		    getRequestVO().getEVENT_ID().intValue()));
	}

	sb.append(message);

	return sb.toString();
    }

}
