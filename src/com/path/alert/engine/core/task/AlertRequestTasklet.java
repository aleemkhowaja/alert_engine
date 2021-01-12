package com.path.alert.engine.core.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.engine.core.container.MessageContainer;
import com.path.alert.engine.logger.AlertEngLogger;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.notification.EvtCO;
import com.path.lib.log.Log;
import com.path.vo.alert.notification.Account;

/**
 * This class is the house of alert request sub task.
 * Each Instance will handle the send message request for a specific type of receiver 
 * @author MohammadAliMezzawi
 * @param <V>
 *
 */
public class AlertRequestTasklet<V> implements Callable<Boolean>{
	
	/**
	 * Msg Id offset
	 */
	private int msgIdOffset;
	
	/**
	 * Receiver type ( CIF / USER_ID ... )
	 */
	private String receiverType;
	
	/**
	 * Generic List of receiver ids
	 */
	private List<V> receiverIds;
	
	private List<BigDecimal> listOfAlrtReceivers;

	/**
	 * Parent Alert request task
	 */
	private AlertRequestTask alertRequestTask;

	/**
	 * Message container Bulk/Instant
	 */
	private MessageContainer messageContainer;
	
	/**
	 * Hold logger reference
	 */
	private final Log logger = Log.getInstance();
	
	@Override
	public Boolean call() throws Exception {
		return execute();
	}
	
	/**
	 * This method will execute the tasklet by 
	 *  1- logging the MSG
	 *  2- Send the Message to the message container
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Boolean execute()
	{
	    boolean result = true;
	    
	    try{
		
		logger.debug(formatLog("Start Execute Tasklet"));
		
		List<AlertNtfCO> listOfNtf = new ArrayList<AlertNtfCO>();
		listOfNtf = prepareMessageByType();
		
		// log the request here
		result = AlertEngLogger.getInstance().logRequestMessages(
				listOfNtf , receiverType);
		
		// logging was executed
		logger.debug(formatLog(String.format("Tasklet logged Successfully ?: [%b]",result)));
		
		// we shouldn't continue to parse the message if it failed to log
		if(!result) {
		    return false;
		}
		// @todo should we notify the parent ( request ) 
		// what if parent has multiple tasklet, should
		// we mark request as failed when one tasklet has failed
		// bulk mode and instant mode has the same issue
		
		result &= getMessageContainer().sendMsg(listOfNtf);
		
		// Sending process was done
		logger.debug(formatLog(String.format("Tasklet Executed Successfully ?: [%b]",
				result)));
	    }catch(Exception e){
		 logger.error(e, formatLog("Error while execute Tasklet"));
		 result = false;
	    }
	    
	    //	PMD infraction of file "PMD_imal_alert_engine_Non_Critical_PMD_report_Infractions" part "BooleanInstantiation" implemented				
	    // return new Boolean(result);
		Boolean ret = Boolean.FALSE;
		if(result) {
		  	ret = Boolean.TRUE;
		}
		return ret;
	}
	
	
	/**
	 * This method will create Message for the producer
	 * and will prepare all notification's need info ( message id , type... )
	 * @param <V>
	 */
	@SuppressWarnings("rawtypes")
	private List<AlertNtfCO> prepareMessageByType() {
		
	    List<AlertNtfCO> ntfList = new ArrayList<AlertNtfCO>();
	    
	    try {
		
		BigDecimal requestId = getParentRequest().getRequestVO().getREQUEST_ID();
		BigDecimal eventId = getParentRequest().getRequestVO().getEVENT_ID();
		EvtCO evtCO = getParentRequest().getNotificationCO().getEvtCO();
		// we are using the event name in business
		String eventName = getParentRequest().getNotificationCO().getEventName();
		BigDecimal queueId = getParentRequest().getNotificationCO().getQueueId();
		BigDecimal compCode = getParentRequest().getNotificationCO().getCompCode();
		// done on 12/04 this should be changed later
		ArrayList<BigDecimal> alrtReceiverList = null != getParentRequest().getNotificationCO()
				.getReceiverList() ? getParentRequest().getNotificationCO()
				.getReceiverList().get(receiverType) : null;
		
		// this should be revisited also
		ArrayList<LinkedHashMap> listOfBatchTags = null != getParentRequest().getNotificationCO().getResultBatch() ?
			getParentRequest().getNotificationCO().getResultBatch() : new ArrayList<LinkedHashMap>();
			
		HashMap<String, String> eventTags = getParentRequest().getNotificationCO().getTagEvent();
		
		//lang code used if the event have skip subscription
		String langCode = getParentRequest().getNotificationCO().getLangCode();
		
		/**
		 * we have the below type of receiver
		 * 	1- CIF => BigDecimal
		 *  2- Channel,User id => string
		 *  3- Account => Account
		 */
		for(int i = 0; i < receiverIds.size(); i ++){
		    	V receiverId = receiverIds.get(i);
			AlertNtfCO<V> ntf = new AlertNtfCO<V>();
			// @todo do we still need it ?? or we can paste it as argument to populateTypeRelatedInfo ??!!!
			ntf.setReceiverId(receiverId);
			ntf.setReceiverType(receiverType);
			ntf.setEventName(eventName);
			ntf.setCompCode(compCode);
			ntf.setEventID(eventId);
			ntf.setQueueId(queueId);
			ntf.setEvtCO(evtCO);
			ntf.setLangCode(langCode);
			// send receiver id
			if(null != alrtReceiverList  && null != alrtReceiverList.get(i)) {
			    ntf.setAlrtReceiverID(alrtReceiverList.get(i));
			}
			// populate info related to the reciever type
			populateTypeRelatedInfo(ntf);
			
			/**
			 * - Event Tags ( dynamic tags set at the event level)
			 * - Batch Tags ( Retrieved by the batch query )
			 */
			ntf.setTagEvent(eventTags);
			
			// since msgIdOffset is 1 based ( not zero based )
			int batchtagIndex = msgIdOffset - 1;
			HashMap<String,Object> batchTags =  batchtagIndex >= listOfBatchTags.size() ? null : 
				listOfBatchTags.get(msgIdOffset-1);
			ntf.setBatchTags(batchTags);
			
			ntf.getMsgVO().setMSG_ID(new BigDecimal(msgIdOffset));
			msgIdOffset++;
			// mark message as New
			ntf.getMsgVO().setSTATUS(AlertEngineConstants.STATUS_NEW_REQUEST);
			ntf.getMsgVO().setREQUEST_ID(requestId);
			ntf.getMsgVO().setEVENT_ID(eventId);
			ntfList.add(ntf);
		}
		
	    }catch(Exception e){
		logger.error(e, formatLog("Error while prepareMessageByType for "+ receiverType));
	    }
	    
	    return ntfList;
	}

	
	/**
	 * Populate type specific info
	 * @param alertNtfCO
	 */
	@SuppressWarnings("rawtypes")
	private void populateTypeRelatedInfo(AlertNtfCO alertNtfCO) throws Exception {
		
		// based on the receiver type
		switch(alertNtfCO.getReceiverType()){
		
            		case AlertNotificationConstant.RECEIVER_TYPE_CIF:
            			alertNtfCO.getMsgVO().setCIF_NO((BigDecimal)
            					alertNtfCO.getReceiverId());
            			break;
            			
            		case AlertNotificationConstant.RECEIVER_TYPE_SUB:
            			alertNtfCO.getMsgVO().setSUB_ID((BigDecimal)
            					alertNtfCO.getReceiverId());
            			break;
			case AlertNotificationConstant.RECEIVER_TYPE_USER_ID:
				alertNtfCO.getMsgVO().setUSER_ID((String)
						alertNtfCO.getReceiverId());
				break;
			case AlertNotificationConstant.RECEIVER_TYPE_CHANNEL:
				alertNtfCO.getMsgVO().setCHANNEL_ID(
						(String) alertNtfCO.getReceiverId());
				break;
			case AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT:
				Account acc = (Account)alertNtfCO.getReceiverId();
				alertNtfCO.getMsgVO().setADDITIONAL_REFERENCE(acc.getAdditionalRef());
				alertNtfCO.getMsgVO().setBRANCH_CODE(acc.getBranch());
				alertNtfCO.getMsgVO().setCURRENCY_CODE(acc.getCurrency());
				alertNtfCO.getMsgVO().setGL_CODE(acc.getAccGl());
				alertNtfCO.getMsgVO().setCIF_NO(acc.getCifNo());
				alertNtfCO.getMsgVO().setSL_NO(acc.getSerialNo());
				alertNtfCO.getMsgVO().setIBAN_ACC_NO(acc.getIbanAccNo());
				break;
				
			case AlertNotificationConstant.RECEIVER_TYPE_MAIL:
				alertNtfCO.getMsgVO().setEMAIL_ID((String)
						alertNtfCO.getReceiverId());
				break;
			case AlertNotificationConstant.RECEIVER_TYPE_MOBILE:
				alertNtfCO.getMsgVO().setMOBILE_PHONE((String)
						alertNtfCO.getReceiverId());
				break;
		}
	}
	
	
	/**
	 * @param msgIdOffset
	 */
	public void setMsgIdOffset(int msgIdOffset) {
		this.msgIdOffset = msgIdOffset;
	}

	/**
	 * @return
	 */
	public int getMsgIdOffset() {
		return msgIdOffset;
	}

	/**
	 * @param receiverType
	 */
	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	/**
	 * @param receiverIds
	 */
	public void setReceiverIds(List<V> receiverIds) {
		this.receiverIds = receiverIds;
	}

	/**
	 * @return
	 */
	public List<V> getReceiverIds() {
		return receiverIds;
	}

	/**
	 * @param alertRequestTask
	 */
	public void setParentRequest(AlertRequestTask alertRequestTask) {
		this.alertRequestTask = alertRequestTask;
	}

	/**
	 * @return
	 */
	public AlertRequestTask getParentRequest() {
		return this.alertRequestTask;
	}

	/**
	 * @param messageContainer
	 */
	public void setMessageContainer(MessageContainer messageContainer) {
		this.messageContainer = messageContainer;
	}

	/**
	 * @return
	 */
	public MessageContainer getMessageContainer() {
		return messageContainer;
	}

	/**
	 * Custom logging function
	 * @param alrt_ENG_REQUESTVO 
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertRequestTasklet] ")
			.append(this).append(String.format("Request ID: [%d] , Event ID: [%d]",
					getParentRequest().getRequestVO().getREQUEST_ID().intValue(),
					getParentRequest().getRequestVO().getEVENT_ID().intValue()))
			.append(message);
		
		return sb.toString();
	}

	/**
	 * @return the listOfAlrtReceivers
	 */
	public List<BigDecimal> getListOfAlrtReceivers() {
	    return listOfAlrtReceivers;
	}

	/**
	 * @param listOfAlrtReceivers the listOfAlrtReceivers to set
	 */
	public void setListOfAlrtReceivers(List<BigDecimal> listOfAlrtReceivers) {
	    this.listOfAlrtReceivers = listOfAlrtReceivers;
	}
	
	
}
