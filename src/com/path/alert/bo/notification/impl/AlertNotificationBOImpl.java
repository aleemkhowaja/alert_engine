package com.path.alert.bo.notification.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.bo.notification.AlertNotificationBO;
import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.bo.notification.AlertNtfMergeCode;
import com.path.alert.dao.notification.AlertNotificationDAO;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.alerts.ALRT_EVT_COMM_INTRNLVO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.alert.vo.engine.AlertMessageListCO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.fixed.AccountQueue;
import com.path.alert.vo.notification.ALRT_EVT_BATCH_ARG_MAPCO;
import com.path.alert.vo.notification.ALRT_EVT_COMM_MODE_ARG_MAPVO;
import com.path.alert.vo.notification.ALRT_EVT_REPORT_ATTACHCO;
import com.path.alert.vo.notification.AlertEvtCO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.alert.vo.notification.AlertPredefinedTagsCO;
import com.path.alert.vo.notification.EmailSCRDynQryCO;
import com.path.alert.vo.notification.EvtCO;
import com.path.alert.vo.notification.QueryCO;
import com.path.alert.vo.notification.SubscriberCO;
import com.path.alert.vo.notification.SubscriptionCO;
import com.path.bo.common.ConstantsCommon;
import com.path.bo.reporting.CommonReportingBO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.S_APPVO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.exception.DAOException;
import com.path.lib.common.util.DateUtil;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.path.lib.log.PathFormatter;
import com.path.vo.alert.notification.Account;
import com.path.vo.reporting.DynLookupSC;
import com.path.vo.reporting.IRP_AD_HOC_REPORTCO;
import com.path.vo.reporting.IRP_REP_ARGUMENTSCO;
import com.path.vo.reporting.ReportOutputCO;

/**
 * @author MohamadHojeij
 *
 */
public class AlertNotificationBOImpl extends BaseBO implements AlertNotificationBO {
	/**
	 * 
	 */
	private AlertNotificationDAO alertNotificationDAO;
	private final static String QUERY_TYPE_N = "N";
	private final static String QUERY_TYPE_L = "L";
	/**
	 * 
	 */
	private CommonReportingBO commonReportingBO;
	
	private FixedEventBO fixedEventBO;
	
	protected Log logger = Log.getInstance();

	private String[] searchCols;
	
	
    /**
     * 844780
     * SELECT EVENT DETAILS
     */
    public EvtCO selectEvtDetails(AlertNotificationCO alertNotificationCO) throws BaseException
    {
	
	BigDecimal eventID = alertNotificationCO.getEventId();
	String eventName = alertNotificationCO.getEventName();

	// check if event name not sent get it from event id
	if(null == eventID && !StringUtil.nullToEmpty(eventName).isEmpty())
	{
	    eventID = alertNotificationDAO.selectEventIDByName(alertNotificationCO);
	}

	EvtCO evtCO = new EvtCO();
	evtCO.setCompCode(alertNotificationCO.getCompCode());
	evtCO.setEventID(eventID);
	evtCO = alertNotificationDAO.selectEvtDetails(evtCO);

	return evtCO;
    }
	
	/**
	 * callReporting
	 * 844780 
	 * 
	 */
    public AlertNotificationCO returnListOfBatchSubscriberById(AlertNotificationCO alertNotificationCO, EvtCO evtCO)
	    throws BaseException
    {
	// not a batch type
	if(!AlertNotificationConstant.BATCH_EVT_TYPE.equals(StringUtil.nullToEmpty(evtCO.getEvtType())))
	{
	    return alertNotificationCO;
	}

	// try to get list of subscribers
	try
	{
	    // check if event name not sent get it from event id
	    BigDecimal eventID = alertNotificationCO.getEventId();
	    ArrayList<LinkedHashMap> resultQ = new ArrayList<>();
	    HashMap<Object, LinkedHashMap> resultHash = new HashMap<Object, LinkedHashMap>();
	    resultQ = prepareBatchResult(evtCO.getBatchId().toString(), eventID, evtCO.getCompCode());
	    //resultHash = convertToHash(evtCO.getBatchTypeQryCol(), resultQ);
	    alertNotificationCO.setResultBatch(resultQ);
	    alertNotificationCO = fillList(evtCO, resultQ, alertNotificationCO);
	}
	catch(Exception e)
	{
	    throw new BOException("Error in retrieving list of Subscribers ", e);
	}
	
	return alertNotificationCO;
    }
    
	
	public AlertNotificationCO checkBulkEvent(AlertNotificationCO alertNotificationCO) throws BaseException{
	    	//check if event name not sent get it from event id
	    	BigDecimal eventID = alertNotificationCO.getEventId();
	    	String eventName = alertNotificationCO.getEventName();
	    	@SuppressWarnings("rawtypes")
		ArrayList<LinkedHashMap> resultQ = new ArrayList<>();	
		HashMap<Object, LinkedHashMap> resultHash = new HashMap<Object, LinkedHashMap>();
		//HashMap<BigDecimal, LinkedHashMap> resultHashSubscribed = new HashMap<BigDecimal, LinkedHashMap>();
		
	  	if(null == eventID && ! StringUtil.nullToEmpty(eventName).isEmpty()){
	  	    eventID = alertNotificationDAO.selectEventIDByName(alertNotificationCO);
	  	}	
	  	EvtCO evtCO = new EvtCO();
	  	evtCO.setCompCode(alertNotificationCO.getCompCode());
	  	evtCO.setEventID(eventID);
	  	//Check the event type if batch
	  	evtCO = alertNotificationDAO.selectEvtDetails(evtCO);
    	if(null == evtCO) {
    		  throw new BOException("Event ID not found with status 'P'");
    	}
	  	if(AlertNotificationConstant.BATCH_EVT_TYPE.equals(StringUtil.nullToEmpty(evtCO.getEvtType()))){
	  	try {
	  	    resultQ = prepareBatchResult(evtCO.getBatchId().toString(),eventID,evtCO.getCompCode());
	  	    //resultHash = convertToHash(evtCO.getBatchTypeQryCol(),resultQ);
    	  			
	  	} catch (Exception e) {
    	  		// TODO Auto-generated catch block
    	  		throw new BaseException(e);
	  	}
	  	alertNotificationCO.setResultBatch(resultQ);
	  	alertNotificationCO = fillList(evtCO,resultQ,alertNotificationCO);
	  }
	  	alertNotificationCO.setEvtCO(evtCO);
	  	return alertNotificationCO;
	}
	
        /**
         * @param evt_name
         * @return
         * @throws BaseException
         */
        private HashMap<String, String> prepareHashParamsEvt(BigDecimal eventID,BigDecimal comp_code) throws BaseException {
    
        	HashMap<String, String> hmQryParam = new HashMap<String, String>();
        
        	ALRT_EVT_BATCH_ARG_MAPCO alrtArgs = new ALRT_EVT_BATCH_ARG_MAPCO();
        
        	alrtArgs.setEventID(eventID);
        	alrtArgs.setCompCode(comp_code);
        	ArrayList<ALRT_EVT_BATCH_ARG_MAPCO> argsList = alertNotificationDAO.returnAlrtArgsQueryEvt(alrtArgs);
        
        	for (ALRT_EVT_BATCH_ARG_MAPCO element : argsList) {
        	    if (!StringUtil.nullToEmpty(element.getMAPPING_TAG_NAME()).isEmpty()) {
        		hmQryParam.put(element.getARG_NAME(), element.getMAPPING_TAG_NAME().equals(AlertNotificationConstant.COMP_CODE) ? 
        			comp_code.toString() : 
        			    commonLibBO.returnSystemDateNoTime().toString());
        	    } else if (!StringUtil.nullToEmpty(element.getMAPPING_VALUE()).isEmpty()) {
        		hmQryParam.put(element.getARG_NAME(), element.getMAPPING_VALUE());
        	    }
        	}
        	return hmQryParam;
    
        }
	
	/**
	 * @param batch_id
	 * @param eventName
	 * @return
	 * @throws Exception
	 */
	private ArrayList<LinkedHashMap> prepareBatchResult(String batch_id,BigDecimal eventID,BigDecimal comp_code) throws Exception{

	    HashMap<String, String> queryParamsHash = new HashMap<String,String>();
	    queryParamsHash = prepareHashParamsEvt(eventID,comp_code);
	    ArrayList<LinkedHashMap> result = new ArrayList<>();
	    result = retrieveQueryRes(batch_id,queryParamsHash,0);
	    
	    return result;
	}
	
	/**
	 * @param batchEvtCO
	 * @param result
	 * @param alertNtfCO
	 * @return
	 */
	private AlertNotificationCO fillList(EvtCO evtCO,
			ArrayList<LinkedHashMap> result,
		AlertNotificationCO alertNtfCO){
	    
	    switch (evtCO.getEvtBatchType()){
	    case AlertNotificationConstant.EvtBatchType_C:
		ArrayList<BigDecimal> listOfCif = new ArrayList<BigDecimal>();
		for(LinkedHashMap<Object,Object> map : result){
		    listOfCif.add((BigDecimal) map.get(evtCO.getBatchTypeQryCol()));
		}
		alertNtfCO.setCifList(listOfCif);
		break;
		
	    case AlertNotificationConstant.EvtBatchType_IU:
		ArrayList<String> listOfImalUsr = new ArrayList<String>();
		for(LinkedHashMap<Object, Object> map : result){
		    listOfImalUsr.add((String) map.get(evtCO.getBatchTypeQryCol()));
		}
		alertNtfCO.setImalUserIdsList(listOfImalUsr);
		break;
		
	    case AlertNotificationConstant.EvtBatchType_CU:
		ArrayList<String> listOfChUsr = new ArrayList<String>();
		for(LinkedHashMap<Object, Object> map : result){
		    listOfChUsr.add((String) map.get(evtCO.getBatchTypeQryCol()));
		}
		alertNtfCO.setChannelUserIdsList(listOfChUsr);
		break;
		
	    case AlertNotificationConstant.EvtBatchType_E:
		ArrayList<String> listOfEmails = new ArrayList<String>();
		for(LinkedHashMap<Object, Object> map : result){
		    listOfEmails.add((String) map.get(evtCO.getBatchTypeQryCol()));
		}
		alertNtfCO.setEmailsList(listOfEmails);
		break;
		
	    case AlertNotificationConstant.EvtBatchType_M: 
		ArrayList<String> listOfMobiles = new ArrayList<String>();
		for(LinkedHashMap<Object, Object> map : result){
		    listOfMobiles.add((String) map.get(evtCO.getBatchTypeQryCol()));
		}
		alertNtfCO.setMobilesList(listOfMobiles);
		break;
			
	    }
	    return alertNtfCO;
	    
	}

	/**
	 * @param alertNotificationCOHash
	 * @return
	 * @throws BaseException
	 */
	@SuppressWarnings("rawtypes")
	public AlertNtfCO getSubscriberDetails(AlertNtfCO alertNtf) throws BaseException {
		
		//check if event name not sent get it from event id
		String eventName = alertNtf.getEventName();
		if(StringUtil.nullToEmpty(eventName).isEmpty()){
		    eventName = alertNotificationDAO.selectEventNameByID(
		    		alertNtf.getMsgVO().getEVENT_ID());
		}
		
		// this will be removed when moving the above code to the message listener
		alertNtf.setEventName(eventName);

		// get Subscriber by CIF
		alertNtf = returnSubscriberId(alertNtf);
	    
		// @todo fix dynamic tags
		return alertNtf;
	}

	/**
	 * @param batchTypeQryCol
	 * @param result
	 * @return
	 * @deprecated
	 */
	private HashMap<Object, LinkedHashMap> convertToHash(String batchTypeQryCol, ArrayList<LinkedHashMap> result) {
	    // TODO Auto-generated method stub
	    HashMap<Object, LinkedHashMap> resultHash = new HashMap<Object, LinkedHashMap>();
	    for(LinkedHashMap row : result){
		resultHash.put(row.get(batchTypeQryCol), row); 
	    }
	    return resultHash;
	}

	
	
	/**
	 * @param subLang
	 * @param eventName
	 * @param subId
	 * @param tempType
	 * @return
	 * @throws DAOException
	 */
	private ArrayList<AlertEvtCO> getEventProperties(String subLang, BigDecimal eventID, BigDecimal subId,SubscriptionCO subscriptionCO
		,String tempType,String evtType) throws DAOException{
	    
	    ArrayList<AlertEvtCO> alrtEvtCOList = new ArrayList<AlertEvtCO>();
	    AlertEvtCO alertEvtParam = new AlertEvtCO();
	    alertEvtParam.setLangCode(subLang);
	    alertEvtParam.setEvtID(eventID);
	    alertEvtParam.setSubscriberID(subId);
	    alertEvtParam.setTemplateType(tempType);
	    alertEvtParam.setEvt_type(evtType);
	    alertEvtParam.setSubscriptionID(null != subscriptionCO ? subscriptionCO.getSubscriptionID() : null);
	    alrtEvtCOList = alertNotificationDAO.returnEventDetails(alertEvtParam);
	    
	    return alrtEvtCOList;
	}
	
	private HashMap<String, Object> prepareAttachements(AlertMessageCO alertMessageCO,AlertEvtCO alertEvtCO,
			AlertNtfMergeCode mergeCode) throws BaseException{
	    
	    HashMap<String, Object> hashResult = new HashMap<>();
	    ALRT_EVT_REPORT_ATTACHCO alrt_EVT_REPORT_ATTACHCO = new ALRT_EVT_REPORT_ATTACHCO();
	    ArrayList<byte[]> attachemetsArray = new ArrayList<byte[]>();
	    String[] attachementsName ;
	    int i =0;
	    ArrayList<BigDecimal> reportArray = new ArrayList<BigDecimal>();
	    alrt_EVT_REPORT_ATTACHCO.setCOMMUNICATION_TYPE(alertEvtCO.getCommunicationType());
	    alrt_EVT_REPORT_ATTACHCO.setEVT_ID(alertEvtCO.getEvtID());
	    reportArray = alertNotificationDAO.selectAttachement(alrt_EVT_REPORT_ATTACHCO);
	    attachementsName = new String[reportArray.size()];
	    for(BigDecimal report : reportArray){
		HashMap<String, Object> reportHash = generateReport(alertMessageCO,alertEvtCO,report,mergeCode);
		ReportOutputCO repOutCO = new ReportOutputCO();
		repOutCO = (ReportOutputCO) PathPropertyUtil.convertToObject(reportHash, ReportOutputCO.class);
		if (null != repOutCO.getReportBytes()) {
		    attachemetsArray.add(repOutCO.getReportBytes());
		    attachementsName[i] = repOutCO.getReportName();
		    i ++;
		}
	    }
	    hashResult.put("list", attachemetsArray);
	    hashResult.put("name", attachementsName);
	    return hashResult;
	    
	    
	    
	}
	
	
	/**
	 * This method will prepare a version of the message for each communication type.
	 * 
	 * @param alertMessageTaskCO
	 * @return
	 * @throws BaseException
	 */
	public AlertMessageListCO prepareMessage(AlertNtfCO alertNtfCO) throws BaseException {
	    
		AlertMessageListCO alertMessageListCO = new AlertMessageListCO();
		alertMessageListCO.setErrorCode(AlertEngineConstants.SUCCESS_CODE);
		ArrayList<AlertMessageCO> alertMessageCOsList = new ArrayList<AlertMessageCO>();
		String sourceOfContact = null;

		try {
			/**
			 * Get Subscriber info : 1- emails 2- mobiles 3- Language ...
			 */
//		    SubscriberCO subscriberCO = new SubscriberCO();
		    //paty 828287 removing this condition because if skip subscription 1, the subscriber is being empty while it was set in the alertNtfCO and should be used from it in all cases.
//		    if(alertNtfCO.getSubscriberCO().getSubScriberId().compareTo(BigDecimal.ZERO) == 1){
		    
		    SubscriberCO subscriberCO = alertNtfCO.getSubscriberCO();
		    
			// Check Subscriber info for safety reason
			if (subscriberCO == null){
			    alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
			    alertMessageListCO.setErrorDesc("Subscriber " + alertNtfCO.getSubscriberCO().getSubScriberId()  + " no detail found");
			    return alertMessageListCO;
			}
			
			/**
			 * @note : too many co are sent from the task and tasklet
			 * and one of those co are evtco which should contain the event id
			 * but seems it isn't populated in the mapper and rather than fixing that
			 * another attributes was defined to hold the event id.
			 * This should be fixed later on while re-factoring the engine step by step.
			 * for now we will just paste the id here
			 */
			EvtCO evtCO = alertNtfCO.getEvtCO();
			evtCO.setEventID(alertNtfCO.getEventID());
			
			
			logger.info("event Name=>" + alertNtfCO.getEventName());
			logger.info("getting event type=>" + evtCO.getEvtType());
			
			// get the source of contact
			sourceOfContact = returnSourceOfContact(evtCO,subscriberCO);
			    
			/**
			 * Source of Contact Validation will not applicable for mobile and email
			 */
			if(!StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_MOBILE)
			    && !StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_MAIL)
			    && !StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_GROUP))
			{
				// check if the source of contact is appropriate to this reciever type
				if(!isValidSrcOfContact(alertNtfCO,sourceOfContact)) {
					 alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
					 alertMessageListCO.setErrorDesc("Source of contact ["+sourceOfContact+"] not appropriate to the Reciever Type ["+alertNtfCO.getReceiverType()
					 	+"] For subscriber id["+alertNtfCO.getSubscriberCO().getSubScriberId() +"]");
					 return alertMessageListCO;
				}
				else
				    if(StringUtil.nullToEmpty(sourceOfContact).equals(AlertNotificationConstant.SRC_CONTACT_F)
					    && !StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_CIF)
					    && !StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT)
					    && (StringUtil.nullToEmpty(evtCO.getSkipSubscription()).equalsIgnoreCase(ConstantsCommon.NO)
					    && NumberUtil.isEmptyDecimal(alertNtfCO.getSubscriberCO().getCifNo()))
			             )
				    {
					 alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
					 alertMessageListCO.setErrorDesc("Source of contact CIf ["+sourceOfContact+"]  and subscriber id["
					 	+ ""+alertNtfCO.getSubscriberCO().getSubScriberId() +"] Or End Channel User Id Should Contain the appropriate Cif");
					 return alertMessageListCO;
				    }
				
			}
			
			
			// Prepare the merge code
			AlertNtfMergeCode mergeCode = returnAlrtMergeCode(alertNtfCO);
    
			/**
			 * For Fixed Event we should check if this receiver criteria match
			 * the expression attached at the level of the event.
			 * if no the delivery of this message will be skipped and the status
			 * will be set at Info.
			 */
			if (!applyEventFilter(alertNtfCO, mergeCode.returnAllTags())) {
				alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
				alertMessageListCO.setErrorDesc("Subscriber " + alertNtfCO.getSubscriberCO().getSubScriberId()
						+ " expression not matched evt id " + alertNtfCO.getMsgVO().getEVENT_ID());
				return alertMessageListCO;
			}
			

			
			// get Event details ( default Message body, msg body , subject ...)
			ArrayList<AlertEvtCO> alertEvtCOArray = getEventProperties(subscriberCO.getLang(),
					alertNtfCO.getEventID(),alertNtfCO.getSubscriberCO().getSubScriberId(),alertNtfCO.getSubscriberCO().getSubscriptionCO(),null,evtCO.getEvtType());
			
			// return internal alerts
			ALRT_EVT_COMM_INTRNLVO alrt_EVT_COMM_INTRNLVO = alertNotificationDAO.returnInternalEventDetailsByEvtId(alertNtfCO.getEventID());
			
					
			
			if ((alertEvtCOArray == null || alertEvtCOArray.isEmpty()) && alrt_EVT_COMM_INTRNLVO == null){
			    alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
			    alertMessageListCO.setErrorDesc("No Alert definition found for event " + alertNtfCO.getEventName());
			    return alertMessageListCO;
			}
			
			/**
			 * get the values from tags
			 */
			buildInternalAlertValuesFromTags(alertMessageListCO, alrt_EVT_COMM_INTRNLVO, mergeCode);
			
			//set alrt_EVT_COMM_INTRNLVO in alertMessageListCO
			alertMessageListCO.setAlrt_EVT_COMM_INTRNLVO(alrt_EVT_COMM_INTRNLVO);
			
			//if the event communication mode like Email , Sms, Push notifiation 
			if (alertEvtCOArray == null || alertEvtCOArray.isEmpty())
			{
			    return alertMessageListCO;
			}
			
			/**
			 * For each communication mode we will build an alert Message CO
			 * which is the final version and will be sent to the sender.
			 */
			for (AlertEvtCO alertEvtCO : alertEvtCOArray) {
				
				AlertMessageCO alertMessageCO = new AlertMessageCO();
				
				try {
					
				    alertEvtCO.setEvtID(alertNtfCO.getEventID());
					
				    // Prepare Alert message
					alertMessageCO.setNtfCO(alertNtfCO);
					alertMessageCO.setCommunicationType(alertEvtCO.getCommunicationType());
					alertMessageCO.setErrorCode(AlertEngineConstants.SUCCESS_CODE);
					
					/**
					 * Check if Message template type is set
					 * we have two type of templates : 
					 * 1- Static : template body and subject defined as text in the event.
					 * 2- Dynamic : Template body and subject are build by a query report.
					 */
					if (StringUtil.nullToEmpty(alertEvtCO.getTemplateType()).isEmpty()) {
						
						log.error("Template type for communication type:" + alertEvtCO.getCommunicationType()
								+ " and evt id:" + alertEvtCO.getEvtID() + " is not defined.");
						alertMessageCO.setErrorCode(AlertEngineConstants.INFO_CODE);
						alertMessageCO.setErrorDesc("Template type for communication type:" + alertEvtCO.getCommunicationType()
								+ " and evt id:" + alertEvtCO.getEvtID() + " is not defined.");
						alertMessageCOsList.add(alertMessageCO);
						// skip this communication type
						continue;
					}
					
					    /**
						 * get Subscriber Email and Phone number
						 * should we populate it based on the communication type ??
						 */
						if(alertEvtCO.getCommunicationType().equals(AlertEngineConstants.COMM_TYPE_EMAIL)
							&& StringUtil.nullToEmpty(evtCO.getSkipSubscription())
					    			.equalsIgnoreCase(ConstantsCommon.YES) && 
					    			StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_MAIL))
						{
						    alertMessageCO.setEmails(alertNtfCO.getMsgVO().getEMAIL_ID());
						}
						else if(alertEvtCO.getCommunicationType().equals(AlertEngineConstants.COMM_TYPE_EMAIL))
						{
						    alertMessageCO.setEmails(returnSubscriberEmails(alertNtfCO, sourceOfContact));
						}
						
						
						if(alertEvtCO.getCommunicationType().equals(AlertEngineConstants.COMM_TYPE_SMS)
							&& StringUtil.nullToEmpty(evtCO.getSkipSubscription())
					    			.equalsIgnoreCase(ConstantsCommon.YES) && 
					    			StringUtil.nullToEmpty(alertNtfCO.getReceiverType()).equals(AlertNotificationConstant.RECEIVER_TYPE_MOBILE))
						{
						    alertMessageCO.setMobiles(alertNtfCO.getMsgVO().getMOBILE_PHONE());
						    
						}
						else if(alertEvtCO.getCommunicationType().equals(AlertEngineConstants.COMM_TYPE_SMS))
						{
						    alertMessageCO.setMobiles(returnSubscriberMobiles(alertNtfCO, sourceOfContact));
						}
						
					/**
					 * Check if the needed info are provided
					 * Email in case communication type is E
					 * 
					 */
					if(StringUtil.nullEmptyToValue(alertMessageCO.getEmails(),"").isEmpty() && 
						alertEvtCO.getCommunicationType().equals("E")
						|| StringUtil.nullEmptyToValue(alertMessageCO.getMobiles(),"").isEmpty() && 
						alertEvtCO.getCommunicationType().equals("S")) 
					{

					    
					    String error =   alertEvtCO.getCommunicationType().equals("E") ? 
						    			"Email communication type in Event: " + alertEvtCO.getEvtID() + ""
						    			+ " Communication Type :"+ alertEvtCO.getCommunicationType() +" Should have the Email Address" : 
							     " Mobile communication type in Event: " + alertEvtCO.getEvtID() + 
							     " Communication Type :"+ alertEvtCO.getCommunicationType() +" Should have the Mobile Number";
					    
						// got the point ???	
					    alertMessageCO.setErrorCode(AlertEngineConstants.INFO_CODE);
					    alertMessageCO.setErrorDesc(error);
					    alertMessageCOsList.add(alertMessageCO);
					    // skip this communication type
					    continue;
					}
					
					//get the attachements
					HashMap<String, Object> attachResult = new HashMap<>();
					attachResult = prepareAttachements(alertMessageCO,alertEvtCO,mergeCode);
					alertMessageCO.setAttachements((ArrayList<byte[]>) attachResult.get("list"));
					alertMessageCO.setAttachementName((String[]) attachResult.get("name"));
					
					/**
					 * Build Message content ( Body/Subject ) based on the template type.
					 */
					boolean result = alertEvtCO.getTemplateType().equals("D")? 
						buildDynamicTemplateMessage(alertMessageCO,alertEvtCO,mergeCode):
						buildStaticTemplateMessage(alertMessageCO,alertEvtCO,mergeCode);
					
					alertMessageCOsList.add(alertMessageCO);
					
				} catch (Exception exp) {
				    
					/**
					 * @note : we should reevaluate this approach, since
					 * it could be done in a better way.
					 * In this approach we are handling the exception by com mode
					 * and add the failed message to the list so it can be logged at the
					 * sender level.
					 */
					alertMessageCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
					alertMessageCO.setErrorDesc(StringUtil.substring(PathFormatter.toString(exp, false), 3999));
					
				    
				    // log it in the error file
				    logger.error(exp,"failed to prepare msg for com mode "+ alertEvtCO.getCommunicationType());
					alertMessageCOsList.add(alertMessageCO);
				}

			}

		} catch (Exception e) {
			logger.error(e,"failed to prepare msg ");
			alertMessageListCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
			alertMessageListCO.setErrorDesc(StringUtil.substring(PathFormatter.toString(e, false), 3999));
		}
		
		alertMessageListCO.setAlertMessageList(alertMessageCOsList);
		return alertMessageListCO;
	}
	
	/**
	 * 
	 * Build the Internal Alert Fields
	 * @param alertNtfCO
	 * @param alrt_EVT_COMM_INTRNLVO
	 */
	
	private void buildInternalAlertValuesFromTags(AlertMessageListCO alertMessageListCO, ALRT_EVT_COMM_INTRNLVO alrt_EVT_COMM_INTRNLVO, AlertNtfMergeCode mergeCode)	
	{
		/**
		 * get company code from custom tags through expression
		 */
		if(NumberUtil.isEmptyDecimal(alrt_EVT_COMM_INTRNLVO.getCOMP_CODE()))
		{
		    alrt_EVT_COMM_INTRNLVO.setCOMP_CODE(new BigDecimal(setInternalAlrtValuesFromTags(
			    alertMessageListCO, alrt_EVT_COMM_INTRNLVO.getCOMP_CODE_EXPR(), mergeCode)));
		}
		
		/**
		 * get branch code from custom tags through expression
		 */
		if(NumberUtil.isEmptyDecimal(alrt_EVT_COMM_INTRNLVO.getBRANCH_CODE()))
		{
		    alrt_EVT_COMM_INTRNLVO.setBRANCH_CODE(new BigDecimal(setInternalAlrtValuesFromTags(
			    alertMessageListCO, alrt_EVT_COMM_INTRNLVO.getBRANCH_CODE_EXPR(), mergeCode)));
		}
		
		/**
		 * get branch code from custom tags through expression
		 */
		if(StringUtil.isEmptyString(alrt_EVT_COMM_INTRNLVO.getAPP_NAME()))
		{
		    alrt_EVT_COMM_INTRNLVO.setAPP_NAME(setInternalAlrtValuesFromTags(
			    alertMessageListCO, alrt_EVT_COMM_INTRNLVO.getAPP_NAME_EXPR(), mergeCode));
		}
		
		/**
		 * get todo key through expression
		 */
		if(StringUtil.isEmptyString(alrt_EVT_COMM_INTRNLVO.getTODO_KEY()))
		{
		    String todKey = setInternalAlrtValuesFromTags(alertMessageListCO, alrt_EVT_COMM_INTRNLVO.getTODO_KEY(), mergeCode);
		    alrt_EVT_COMM_INTRNLVO.setTODO_KEY(todKey);
		}
	}
	
    /**
     * set Internal Alert Values
     * 
     * @param alertMessageListCO
     * @param expression
     * @param mergeCode
     * @return
     */
    private String setInternalAlrtValuesFromTags(AlertMessageListCO alertMessageListCO, String expression,
	    AlertNtfMergeCode mergeCode)
    {
	try
	{

	    // if message is empty no need to send it ;)
	    if(expression.trim().isEmpty())
	    {
		alertMessageListCO.setErrorCode(AlertEngineConstants.INFO_CODE);
		alertMessageListCO.setErrorDesc("Missing Parameters of Internal Alerts:");
		return null;
	    }

	    /**
	     * Check if Message contain an expression
	     * 
	     */
	    if(expression.indexOf("'") >= 0 && expression.indexOf("+") > 0)
	    {
		// mode (issue when having query tags they were not replaced)
		HashMap<String, Object> tagsValuesHm = mergeCode.returnAllTags(null,null);

		// Remove the tag delimiter < >
		expression = expression.replaceAll(AlertEngineConstants.MSG_TAG_REGEX, "[$1]");

		// prepare the expression parameters
		ArrayList<Map<String, Object>> ll = new ArrayList<>();
		ll.add(tagsValuesHm);
		expression = (String) commonLibBO.executeExpression(expression, 0, ll);

	    }
	    else
	    {
		expression = mergeCode.replaceTag(expression, null, null);
	    }

	    return expression;

	}
	catch(Exception exp)
	{
	    logger.error(exp, String.format("failed to build the Internal Alert Details"));
	    alertMessageListCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
	    alertMessageListCO.setErrorDesc(StringUtil.substring(PathFormatter.toString(exp, false), 3999));
	    return null;
	}

    }

	/**
	 * Build Dynamic message template (Body/Subject)
	 * @param alertMessageCO
	 * @param alertEvtCO
	 * @param mergeCode
	 * @return
	 */
	private boolean buildDynamicTemplateMessage(AlertMessageCO alertMessageCO, AlertEvtCO alertEvtCO,
			AlertNtfMergeCode mergeCode) {

		try {

			HashMap<String, Object> reportHash = generateReport(alertMessageCO, alertEvtCO, BigDecimal.ZERO, mergeCode);

			/**
			 * Failed to generate the report
			 */
			if (null == reportHash) {
				alertMessageCO.setErrorCode(AlertEngineConstants.INFO_CODE);
				alertMessageCO.setErrorDesc(
						"Reporting did not return report in byte Comm type:" + alertEvtCO.getCommunicationType()
								+ " and evt id:" + alertEvtCO.getEvtID() + " Rep id:" + alertEvtCO.getReportId());
				return false;
			}

			ReportOutputCO repOutCO = new ReportOutputCO();
			repOutCO = (ReportOutputCO) PathPropertyUtil.convertToObject(reportHash, ReportOutputCO.class);
			if (null == repOutCO.getReportBytes()) {
				alertMessageCO.setErrorCode(AlertEngineConstants.INFO_CODE);
				alertMessageCO.setErrorDesc(
						"Reporting did not return report in byte Comm type:" + alertEvtCO.getCommunicationType()
								+ " and evt id:" + alertEvtCO.getEvtID() + " Rep id:" + alertEvtCO.getReportId());
				return false;
			}

			String messageSubject = !StringUtil.nullToEmpty(alertEvtCO.getMessageSubject()).isEmpty()
					? alertEvtCO.getMessageSubject()
					: alertEvtCO.getDefaultMessageSubject();

			alertMessageCO.setMessageBody(new String(repOutCO.getReportBytes(), "UTF-8"));
			alertMessageCO.setMessageSubject(messageSubject);
			return true;

		} catch (Exception exp) {
			
			ALRT_ENG_MSGVO messageVO = alertMessageCO.getNtfCO().getMsgVO();
			logger.error(exp,String.format("failed to buildDynamicTemplateMessage Message [%d][%d]",
					messageVO.getREQUEST_ID().intValue(),messageVO.getMSG_ID().intValue()));
			alertMessageCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
			alertMessageCO.setErrorDesc(StringUtil.substring(PathFormatter.toString(exp, false), 3999));
			return false;
		}

	}
	

	/**
	 * Build Static template Message body and subject
	 * @param alertMessageCO
	 * @param alertEvtCO
	 * @param mergeCode
	 * @return
	 * @throws Exception
	 */
	private boolean buildStaticTemplateMessage(AlertMessageCO alertMessageCO,
			AlertEvtCO alertEvtCO, AlertNtfMergeCode mergeCode) throws Exception {
		
		try {

			String messageToBeSent = StringUtil.nullEmptyToValue(alertEvtCO.getMessageBody(),
					alertEvtCO.getDefaultMessageBody());

			// if message is empty no need to send it ;)
			if (messageToBeSent.trim().isEmpty()) {
				alertMessageCO.setErrorCode(AlertEngineConstants.INFO_CODE);
		        alertMessageCO.setErrorDesc("Message Body is missing, communication mode:" + alertEvtCO.getCommunicationType()
		        	+ " event id:" + alertEvtCO.getEvtID().toString());
				return false;
			}
			// Message Subject
			String messageSubject = !StringUtil.nullToEmpty(alertEvtCO.getMessageSubject()).isEmpty()
					? alertEvtCO.getMessageSubject()
					: alertEvtCO.getDefaultMessageSubject();

			// build the Subject
			if (!StringUtil.nullToEmpty(messageSubject).isEmpty()) {
				messageSubject = mergeCode.replaceTag(messageSubject, alertEvtCO.getCommunicationType(),
						alertEvtCO.getQueryId());
			}
			/**
			 * Check if Message contain an expression
			 */
			if (messageToBeSent.indexOf("'") >= 0 && messageToBeSent.indexOf("+") > 0) {
				// send the query id when exists on the related communication
				// mode (issue when having query tags they were not replaced)
				HashMap<String, Object> tagsValuesHm = mergeCode.returnAllTags(alertEvtCO.getCommunicationType(),
						alertEvtCO.getQueryId());

				// Remove the tag delimiter < >
				messageToBeSent = messageToBeSent.replaceAll(AlertEngineConstants.MSG_TAG_REGEX, "[$1]");

				// prepare the expression parameters
				ArrayList<Map<String, Object>> ll = new ArrayList<>();
				ll.add(tagsValuesHm);
				messageToBeSent = (String) commonLibBO.executeExpression(messageToBeSent, 0, ll);

			} else {
				messageToBeSent = mergeCode.replaceTag(messageToBeSent, alertEvtCO.getCommunicationType(),
						alertEvtCO.getQueryId());
			}
			
			// set message body and subject
			if(alertEvtCO.getCommunicationType().equalsIgnoreCase("E"))
				messageToBeSent = messageToBeSent.replaceAll("\n", "<br/>");
			
			alertMessageCO.setMessageBody(messageToBeSent);
			alertMessageCO.setMessageSubject(messageSubject);
			return true;
			
		} catch (Exception exp) {
			
			ALRT_ENG_MSGVO messageVO = alertMessageCO.getNtfCO().getMsgVO();
			logger.error(exp,String.format("failed to buildStaticTemplateMessage Message [%d][%d]",
					messageVO.getREQUEST_ID().intValue(),messageVO.getMSG_ID().intValue()));
			alertMessageCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
			alertMessageCO.setErrorDesc(StringUtil.substring(PathFormatter.toString(exp, false), 3999) );
			return false;
		}
	}
	
	
	/**
	 * Create and prepare the merge code that will used while build message content
	 * 1- Load the default tags using the subscriber and event CO.
	 * 2- Load the custom tags poplated in the event tags
	 * 3- Load Fixed event tags ( if event is fixed event)
	 * 4- Load batch tags ( populated at tasklet level in case event is batch)
	 * 5- Query tags will be returned on demand by communication mode.
	 * 
	 * @param alertNtfCO
	 * @return
	 * @throws BaseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AlertNtfMergeCode returnAlrtMergeCode(AlertNtfCO alertNtfCO) throws BaseException {
		
		AlertNtfMergeCode mergeCode = new AlertNtfMergeCode(alertNtfCO.getEvtCO());
		mergeCode.loadDefaultTags(alertNtfCO.getSubscriberCO());
		mergeCode.loadEventTag(alertNtfCO.getTagEvent());
		mergeCode.loadReceiverTags(alertNtfCO.getEvtCO().getFixedEventID(), loadReceiverDetailsTags(alertNtfCO));
		mergeCode.loadBatchTags(alertNtfCO.getBatchTags());
		
		return mergeCode;
	}

	/**
	 * Load Receiver Details Tags
	 * @param alertNtfCO
	 * @return
	 * @throws BaseException
	 */
	private HashMap<String, Object> loadReceiverDetailsTags(AlertNtfCO alertNtfCO) throws BaseException {
	
		HashMap<String, Object> tagsEvt = new HashMap<>();
		/**
		 * In case the event type isn't fixed we have skipped
		 * loading the receiver details but since we will be using
		 * the queue tables to hold the request sent via procedure
		 * we have removed the below condition.
		 * !StringUtil.nullToEmpty(alertNtfCO.getEvtCO()
		 * .getEvtType()).equalsIgnoreCase("F")
		 */
		if(null == alertNtfCO.getAlrtReceiverID()) {
			return tagsEvt;
		}
		
		// get Receiver details Tags created by the trigger as Json {tag:value,tag1:value1}
		tagsEvt = fixedEventBO.getHash(alertNtfCO.getMsgVO().getEVENT_ID()
	    	,alertNtfCO.getAlrtReceiverID());

	    return tagsEvt;
	}
	
	/**
	 * Check if source of contact is appropriate to a receiver type e.g : if
	 * receiver type = Account then source of contact should be Account If
	 * receiver type = Cif then source of contact should be Cif/Card
	 * 
	 * @param alertNtfCO
	 * @param sourceOfContact
	 * @return
	 */
	private boolean isValidSrcOfContact(AlertNtfCO alertNtfCO, String sourceOfContact) {

		String receiverType = alertNtfCO.getReceiverType();
		switch (sourceOfContact) {
		
		
		/**
		 * In case source of Contact is F ( CIF ) Or D ( Card ) Receiver type
		 * should be either CIF or Account or Subscriber
		 */
		
		case AlertNotificationConstant.SRC_CONTACT_F:
		case AlertNotificationConstant.SRC_CONTACT_D:
			if (!(AlertNotificationConstant.RECEIVER_TYPE_CIF.equals(receiverType)
					|| AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT.equals(receiverType)
					|| AlertNotificationConstant.RECEIVER_TYPE_SUB.equals(receiverType))) {
				return false;
			}
			break;
	        
		case AlertNotificationConstant.SRC_CONTACT_C:
			if (!AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT.equals(receiverType)) 
			{
				return false;
			}
		}
	   
		
		return true;
	}

	/**
	 * Return the Source of contact based on the given subscriber and event
	 * @param evtCO
	 * @param subscriberCO
	 * @return
	 * @throws BOException
	 */
	private String returnSourceOfContact(EvtCO evtCO, SubscriberCO subscriberCO) throws BOException {
    	
		/**
		 * In case of event system.
		 * later on we should validate if zero is still a valid condition
		 */
    	if(subscriberCO.getSubScriberId().compareTo(BigDecimal.ZERO) == 0 
    			|| evtCO.getEvtType().equals("S")) {
    		return evtCO.getSourceOfContact();
    	}
    	
    	// prioritize the subscription value
    	if( null != subscriberCO.getSubscriptionCO()) {
    		return subscriberCO.getSubscriptionCO().getSourceOfContact();
    	}
    	// Skip isn't checked and subscription is null 
    	if(StringUtil.nullToEmpty(evtCO.getSkipSubscription())
    			.equalsIgnoreCase(ConstantsCommon.NO)) {
    		throw new BOException(String.format("Event[%s] No subscription for subscriber [%s]",
    				evtCO.getEventID(),subscriberCO.getSubScriberId()));
    	}
    	// if skip subscription is flagged 
    	return evtCO.getSourceOfContact();
    }
	

	/**
	 * Return the Subscriber email based on the source of contact
	 * @param alertNtfCO
	 * @param sourceOfContact
	 * @return
	 * @throws BaseException 
	 */
	private String returnSubscriberEmails(AlertNtfCO alertNtfCO, String sourceOfContact) throws BaseException {
		String emails = getCommunicationModeTarget(alertNtfCO, sourceOfContact, AlertNotificationConstant.EMAIL);
		return StringUtil.nullToEmpty(emails).isEmpty() ? 
				alertNtfCO.getSubscriberCO().getEmails() : emails;
	}
	
	/**
	 * Return the Subscriber mobiles based on the source of contact
	 * @param alertNtfCO
	 * @param sourceOfContact
	 * @return
	 * @throws BaseException 
	 */
	private String returnSubscriberMobiles(AlertNtfCO alertNtfCO, String sourceOfContact) throws BaseException {
		String mobiles = getCommunicationModeTarget(alertNtfCO, sourceOfContact, AlertNotificationConstant.MOBILE);
		return StringUtil.nullToEmpty(mobiles).isEmpty() ? 
				alertNtfCO.getSubscriberCO().getMobiles() : mobiles;
	}
	
	
	/**
	 * @param sourceOfContact
	 * @param sub_id
	 * @return
	 * @throws BaseException
	 */
	private String getCommunicationModeTarget(AlertNtfCO alertNtfCO, String sourceOfContact, String communicationType)
			throws BaseException {
		
		/**
		 * If source of contact isn't CIF or Account or Card, then we will
		 * use the subscriber mail and mobile
		 */
		if(!(sourceOfContact.equalsIgnoreCase(AlertNotificationConstant.SRC_CONTACT_C) 
				|| sourceOfContact.equalsIgnoreCase(AlertNotificationConstant.SRC_CONTACT_F) 
				|| sourceOfContact.equalsIgnoreCase(AlertNotificationConstant.SRC_CONTACT_D))) {
			return null;
		}
			
		EmailSCRDynQryCO emailSCRDynQryCO = new EmailSCRDynQryCO();
		emailSCRDynQryCO.setMsgVO(alertNtfCO.getMsgVO());		
		/**
		 *  In case the source of contact is CIF and we are sending subscriber and skip subscription flag
		 *  is off, we will use the cif inside subscriber in case because it's null inside the message vo
		 *  since the populate receiver type info didn't and will not populate it.
		 *  it will just populate the subscriber id.
		 */
		if(NumberUtil.isEmptyDecimal(emailSCRDynQryCO.getMsgVO().getCIF_NO()))
		{
		    emailSCRDynQryCO.getMsgVO().setCIF_NO(alertNtfCO.getSubscriberCO().getCifNo());
		}
		
		emailSCRDynQryCO.setCompCode(alertNtfCO.getCompCode());
	//	emailSCRDynQryCO.setSubId(alertNtfCO.getSubscriberCO().getSubScriberId());
		emailSCRDynQryCO.setColumnSelect(communicationType);

		/**
		 * set the connection co
		 */
		emailSCRDynQryCO.setConnCO(AlertEngine.getInstance().getConnCO());
		
		switch (sourceOfContact) {
		
			// account
			case AlertNotificationConstant.SRC_CONTACT_C:
				emailSCRDynQryCO.setTableName(AlertNotificationConstant.AMF_ADDRESS);
				emailSCRDynQryCO.setColumnPrimary(AlertNotificationConstant.LINE_NO);
				emailSCRDynQryCO.setCifCond(AlertNotificationConstant.ACC_CIF);
				break;
				
			// CIF
			case AlertNotificationConstant.SRC_CONTACT_F:
	
				emailSCRDynQryCO.setTableName(AlertNotificationConstant.CIF_ADDRESS);
				emailSCRDynQryCO.setColumnPrimary(AlertNotificationConstant.LINE_NO);
				emailSCRDynQryCO.setCifCond(AlertNotificationConstant.CIF_NOCOLUMN);
				break;
	
			// cards
			case AlertNotificationConstant.SRC_CONTACT_D:
				emailSCRDynQryCO.setTableName(AlertNotificationConstant.CTSCARDS_MGT);
				emailSCRDynQryCO.setColumnPrimary(AlertNotificationConstant.APPLICATION_ID);
				emailSCRDynQryCO.setCifCond(AlertNotificationConstant.CIF);
				break;
		}

		return alertNotificationDAO.selectEmailFromSCR(emailSCRDynQryCO);
	}

	/**
         * @param subscriberID
         * @return
         * @throws DAOException
         * Get Subscriber info : 1- emails 2- mobiles 3- Language ...
         */
       /* private SubscriberCO returnSubscriberDetails(BigDecimal subscriberID,BigDecimal compCode) throws DAOException {
        	SubscriberCO subscriberCO = new SubscriberCO();
        	subscriberCO.setSubScriberId(subscriberID);
        	subscriberCO = alertNotificationDAO.returnSubscriberDetails(subscriberCO);
        	return subscriberCO;
        }
*/
	
        
        

	/**
	 * @param alertEvtCO
	 * @param alertMessageTaskCO
	 * @throws BaseException
	 */
	private HashMap<String, Object> generateReport(AlertMessageCO alertMessageCO,AlertEvtCO alertEvtCO,BigDecimal report,AlertNtfMergeCode mergeCode) {
		// @todo fix this haj
		IRP_AD_HOC_REPORTCO reportVO = new IRP_AD_HOC_REPORTCO();
		HashMap<String, Object> reportAttachementHash = null;
		HashMap<String, Object> reportDefVOHash;
		ALRT_ENG_MSGVO messageVO = alertMessageCO.getNtfCO().getMsgVO();
		try {
			reportDefVOHash = commonReportingBO.returnReportById(report.equals(BigDecimal.ZERO) ? 
				alertEvtCO.getReportId() : report);

			if (reportDefVOHash == null || reportDefVOHash.isEmpty()) {
				throw new BOException("Report info not found for id " + alertEvtCO.getReportId());
			}
			reportVO = (IRP_AD_HOC_REPORTCO) PathPropertyUtil.convertToObject(reportDefVOHash,
					IRP_AD_HOC_REPORTCO.class);

			HashMap<String, Object> configuration = new HashMap<String, Object>();

			// fill configuration for reporting
			configuration.put(ConstantsCommon.FORMAT_PARAM,
					StringUtil.nullEmptyToValue(reportVO.getDEFAULT_FORMAT(), "html"));
			configuration.put(ConstantsCommon.MENU_ID_PARAM, reportVO.getPROG_REF());
			configuration.put(ConstantsCommon.APP_NAME_PARAM, reportVO.getAPP_NAME());
			configuration.put(ConstantsCommon.REPORT_ID_PARAM, reportVO.getREPORT_ID());
			configuration.put(ConstantsCommon.HEAD_FOOT_PARAM, (reportVO.getSHOW_HEAD_FOOT().equals("1")) ? true : false);

			// get the language of the subscriber
			configuration.put(ConstantsCommon.LANG_PARAM, alertEvtCO.getLangCode());
			configuration.put(ConstantsCommon.DB_PARAM, NumberUtil.nullToZero(reportVO.getCONN_ID()));

			/**
			 * Since user id is 8 chars length we are taking
			 * the last 4 digits of the request id and the message id
			 */
			int uidReq = messageVO.getREQUEST_ID().intValue() % 10000;
			int uidMsg = messageVO.getMSG_ID().intValue() % 10000;
			
			// session param
			HashMap<String, Object> sessionParams = new HashMap<String, Object>();
			sessionParams.put(AlertNotificationConstant.currentAppName, ConstantsCommon.ALERT_APP_NAME);
			sessionParams.put(AlertNotificationConstant.userName,  "" + uidReq + uidMsg );
			sessionParams.put(AlertNotificationConstant.companyCode, null);
			sessionParams.put(AlertNotificationConstant.branchCode, null);
 
			log.info("Report Configuration params : " + configuration.toString());

			HashMap<String, Object> reportParams = prepareReportParams(reportVO, alertEvtCO,report, mergeCode);
			reportAttachementHash = commonReportingBO.returnReportBytes(reportParams, sessionParams, configuration,
					true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportAttachementHash = null;
			log.error(PathFormatter.toString(e, false));
		}

		return reportAttachementHash;

	}
	
	
   
    
    	@Override
	public ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> returnAlrtArgsQuery(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtArgs)
		throws Exception {
	    // TODO Auto-generated method stub
	    return alertNotificationDAO.returnAlrtArgsQuery(alrtArgs);
	}
	
	@Override
	public AlertPredefinedTagsCO returnPredefinedTags(String tagName) throws Exception {
	    // TODO Auto-generated method stub
	    return alertNotificationDAO.returnPredefinedTags(tagName);
	}
	
	
	
	

        /**
         * @param evtId
         * @param commucicationType
         * @param mapTags
         * @return
         * @throws DAOException
         */
        /*private HashMap<String, String> prepareHashParamsCommMode(BigDecimal queryID,BigDecimal evtId, String commucicationType,
    	    HashMap<String, String> mapTags) throws DAOException {
    
        	HashMap<String, String> hmQryParam = new HashMap<String, String>();
        
        	ALRT_EVT_COMM_MODE_ARG_MAPVO alrtArgs = new ALRT_EVT_COMM_MODE_ARG_MAPVO();
        
        	alrtArgs.setCOMMUNICATION_TYPE(commucicationType);
        	alrtArgs.setEVT_ID(evtId);
        	alrtArgs.setQUERY_REPORT_ID(queryID);
        	ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> argsList = alertNotificationDAO.returnAlrtArgsQuery(alrtArgs);
        
        	for (ALRT_EVT_COMM_MODE_ARG_MAPVO element : argsList) {
        	    if (!StringUtil.nullToEmpty(element.getMAPPING_TAG_NAME()).isEmpty()) {
        		hmQryParam.put(element.getARG_NAME(), mapTags.get(element.getMAPPING_TAG_NAME()));
        	    } else if (!StringUtil.nullToEmpty(element.getMAPPING_VALUE()).isEmpty()) {
        		hmQryParam.put(element.getARG_NAME(), element.getMAPPING_VALUE());
        	    }
        	}
        	return hmQryParam;
    
        }*/

	/**
	 * @param queryID
	 * @param evtId
	 * @param commucicationType
	 * @param mapTags
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<LinkedHashMap> retrieveQueryRes(String queryID,HashMap<String, String> hmQryParam , int nbRec) throws Exception {
 
		ArrayList<LinkedHashMap> result = new ArrayList<>();
		int nbRecs = 1;
		DynLookupSC dynLookupSC = new DynLookupSC();
		dynLookupSC.setQryId(queryID);
		dynLookupSC.setHmQryParam(hmQryParam);
		dynLookupSC.setSearchCols(searchCols);
		
		HashMap<String, Object> dynLookupSCMap = new HashMap<>();
		
		dynLookupSCMap = PathPropertyUtil.convertToMap(dynLookupSC);
		if(nbRec == 0) {
		    nbRecs = commonReportingBO.returnQryResultCnt(dynLookupSCMap);
		}
		dynLookupSCMap.put(AlertNotificationConstant.NBREC, nbRecs);
		 
		result = commonReportingBO.returnQryResult(dynLookupSCMap);

		return result;
	}

	/**
	 * Prepare the reports parameters.
	 * 
	 * @param repVO
	 * @param alertEvtCO
	 * @param alertCO
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> prepareReportParams(IRP_AD_HOC_REPORTCO repVO, AlertEvtCO alertEvtCO,
			BigDecimal report, AlertNtfMergeCode mergeCode) throws Exception {

		HashMap<String, Object> reportParams = new HashMap<String, Object>();
		ALRT_EVT_COMM_MODE_ARG_MAPVO alrtArgs = new ALRT_EVT_COMM_MODE_ARG_MAPVO();
		alrtArgs.setCOMMUNICATION_TYPE(alertEvtCO.getCommunicationType());
		alrtArgs.setEVT_ID(alertEvtCO.getEvtID());
		alrtArgs.setATTACH_REPORT_ID(report);

		// ????
		alrtArgs.setQUERY_REPORT_ID(report.equals(BigDecimal.ZERO) ? alertEvtCO.getReportId() : BigDecimal.ZERO);

		for (IRP_REP_ARGUMENTSCO element : repVO.getArgumentsList().values()) {
			alrtArgs.setARG_NAME(element.getARGUMENT_NAME());
			ALRT_EVT_COMM_MODE_ARG_MAPVO arg = alertNotificationDAO.returnAlrtArgsReport(alrtArgs);

			Object argVal = null;

			// In case the mapped element is a constant value
			if (StringUtil.nullToEmpty(arg.getMAPPING_TAG_NAME()).isEmpty()) {

				switch (element.getARGUMENT_TYPE()) {
				case AlertNotificationConstant.NUMBER:
					argVal = new BigDecimal(arg.getMAPPING_VALUE());
					break;
				case AlertNotificationConstant.DATE:
					argVal = DateUtil.parseDate(arg.getMAPPING_VALUE(), "DD/MM/YYYY");
					break;
				case AlertNotificationConstant.STRING:
					argVal = arg.getMAPPING_VALUE().toString();
					break;
				}

				reportParams.put(element.getARGUMENT_NAME(), argVal);
				continue;
			}

			/**
			 * Else the mapped argument is a Tag.
			 */
			HashMap<String, Object> allTags = mergeCode.returnAllTags(alertEvtCO.getCommunicationType(),
					alertEvtCO.getQueryId());
			argVal = allTags.get(arg.getMAPPING_TAG_NAME());

			/**
			 * Note : The argument type at the report params list supersede the
			 * tag type.
			 * There is some missing scenarios like
			 * what if the argVal is a number while the argtype is a string ??
			 * is that handled by the report generation ?
			 * For now we will handle two parts
			 */
			if (argVal instanceof String ) {
				
				if(AlertNotificationConstant.NUMBER.equals(element.getARGUMENT_TYPE())) {
					// case of comp_code tag comes as well from fixed event as string
					argVal = StringUtil.nullToEmpty((String) argVal).isEmpty() || !NumberUtil.isNumber((String) argVal)
							? null : new BigDecimal((String) argVal);
				}else if ( AlertNotificationConstant.DATE.equals(element.getARGUMENT_TYPE())) {
					argVal = StringUtil.nullToEmpty((String) argVal).isEmpty() ? null : 
						DateUtil.parseDate((String) argVal, "DD/MM/YYYY");;
				}
			}
			
			// add date now

			reportParams.put(arg.getARG_NAME(), argVal);

		}
		
		return reportParams;
	}
	
	/**
	 * @param receiverIds
	 * @param type
	 * @return
	 * @throws BaseException
	 */ 
	private <V> AlertNtfCO returnSubscriberId(AlertNtfCO alertNtfCO)
		    throws BaseException
	{
	    	

		if(alertNtfCO == null || null == alertNtfCO.getReceiverId() ) {
		    return null;
		}
		SubscriberCO subscriberCO = new SubscriberCO();
		alertNtfCO.setErrorCode(AlertEngineConstants.SUCCESS_CODE);
		if(null != alertNtfCO.getEventID()){
			
		    String skipSubs = alertNtfCO.getEvtCO().getSkipSubscription();
		    if("Y".equals(StringUtil.nullToEmpty(skipSubs)))
		    {
    		    	/**
    		    	 *  @note : as per mezzawi this is confusing since we have two different
    		    	 *  behaviors where in some cases we use the skip subscriber flag to identify the 
    		    	 *  skip subscription and in other we use the subscriber id equal to 
    		    	 *  zero
    		    	 */
    		    	subscriberCO.setSubScriberId(BigDecimal.ZERO);
    		    	subscriberCO.setLang(StringUtil.nullEmptyToValue(alertNtfCO.getLangCode(), AlertNotificationConstant.DEFAULT_LANG_FLAG) );
			alertNtfCO.setSubscriberCO(subscriberCO);
			return alertNtfCO;
		    }
		}

		QueryCO queryCO = new QueryCO();
		String querySyntax = "";
		String columnName = "";
		String matchingOperator = "";
		
		// get receiver type
		String receiverType = alertNtfCO.getReceiverType();
		
		//#BUG 827157
		/*if(AlertNotificationConstant.RECEIVER_TYPE_SUB.equals(receiverType))
		{
		    subscriberCO.setSubScriberId((BigDecimal)alertNtfCO.getReceiverId());
		    alertNtfCO.setSubscriberCO(subscriberCO);
		    return alertNtfCO;
		}
		else */
		if(receiverType.equals(AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT)){
			
		    AccountQueue accountQ = new AccountQueue();
		    accountQ.setAccount((Account)alertNtfCO.getReceiverId());
		    accountQ.setEventName(alertNtfCO.getEventName());
		    accountQ.setEventID(alertNtfCO.getEventID());
		    accountQ.setCompCode(alertNtfCO.getCompCode());
		    logger.info("receiver type=>" + receiverType+",account=>"+accountQ.toString());
		    subscriberCO = alertNotificationDAO.returnSubscriberIdByAccount(accountQ);
		}
		
		/** 
		 * Build the query based on the receiver type
		 */
		else{
		    switch(receiverType)
			{
			   case AlertNotificationConstant.RECEIVER_TYPE_SUB:
			        columnName = AlertNotificationConstant.SUB_ID;
				matchingOperator = "N";
				break;
			   case AlertNotificationConstant.RECEIVER_TYPE_CIF:
				columnName = AlertNotificationConstant.CIF_NO;
				matchingOperator = "N";
				break;
			    case AlertNotificationConstant.RECEIVER_TYPE_CHANNEL:
			    	columnName = AlertNotificationConstant.CHANNEL_END_USER_ID;
			    	matchingOperator = "S";
			    	break;
			    case AlertNotificationConstant.RECEIVER_TYPE_FB:
			    	columnName = AlertNotificationConstant.FACEBOOK_SOCIAL_ID;
			    	matchingOperator = "S";
			    	break;
			    case AlertNotificationConstant.RECEIVER_TYPE_TW:
			    	columnName = AlertNotificationConstant.TWITTER_SOCIAL_ID;
			    	matchingOperator = "S";
			    	break;
			    case AlertNotificationConstant.RECEIVER_TYPE_USER_ID:
			    	columnName = AlertNotificationConstant.USERNAME;
			    	matchingOperator = "S";
			    	break;
			    case AlertNotificationConstant.RECEIVER_TYPE_MAIL:
				columnName = AlertNotificationConstant.EMAIL_ID;
			    	matchingOperator = "L";
				break;
			    case AlertNotificationConstant.RECEIVER_TYPE_MOBILE:
			    	columnName = AlertNotificationConstant.MOBILE_PHONE;
			    	matchingOperator = "L";
				break;
			}

	    		querySyntax = returnQuerySyntax(matchingOperator, alertNtfCO.getReceiverId());
	    		logger.info("receiver type=>" + receiverType+",querySyntax=>"+querySyntax);
			queryCO.setColumnName(columnName);
			queryCO.setQuery(querySyntax);
			queryCO.setEventName(alertNtfCO.getEventName());
			queryCO.setEvtID(alertNtfCO.getEventID());
			queryCO.setCompCode(alertNtfCO.getCompCode());
			subscriberCO = alertNotificationDAO.selectSubscriberId(queryCO);
		}
		
		
	if(null == subscriberCO){
	   // throw new BOException("Subscriber ID not found");
	    alertNtfCO.setErrorCode(AlertEngineConstants.INFO_CODE);
	    alertNtfCO.setErrorDesc("Subscriber ID not found");
	    return alertNtfCO;
	}
	
	SubscriptionCO subscriptionCO = null;
	AccountQueue accountQueue = new AccountQueue();
	accountQueue.setCompCode(alertNtfCO.getCompCode());
	accountQueue.setEventID(alertNtfCO.getEventID());
	accountQueue.setSubscriberID(subscriberCO.getSubScriberId());
	
	/**
	 * Skip Subscriptions for System Event
	 * Requested by Paty
	 * Added by Alim
	 */
	String eventType = alertNtfCO.getEvtCO().getEvtType();
	if(!StringUtil.nullToEmpty(eventType).equalsIgnoreCase(AlertEngineConstants.SYSTEM_EVENT_TYPE))
	{
		//check subscription if event type != system event
		subscriptionCO = checkSubscription(accountQueue);
		
		//retrieve skip subscription
		String skipSub = alertNotificationDAO.returnSkipSubs(alertNtfCO.getEventID());
		
		if(null == subscriptionCO && StringUtil.nullToEmpty(skipSub).equalsIgnoreCase(ConstantsCommon.NO)){
			alertNtfCO.setErrorCode(AlertEngineConstants.INFO_CODE);
			alertNtfCO.setErrorDesc("Subscriber ID:" + subscriberCO.getSubScriberId().toString() + " is not subscribed on evt id: " + alertNtfCO.getEventID().toString());
			return alertNtfCO;
		}
	}
	
	subscriberCO.setSubscriptionCO(subscriptionCO);
	alertNtfCO.setSubscriberCO(subscriberCO);
	return alertNtfCO;
	}

	private SubscriptionCO checkSubscription(AccountQueue accountQueue) throws DAOException{
	    // TODO Auto-generated method stub
	    SubscriptionCO subscriptionCO = alertNotificationDAO.selectSubscription(accountQueue);
	    return(subscriptionCO);
	    
	    
	}

	/**
	 * This method ????
	 * @param <V>
	 * 
	 * @param columnName
	 * @param type
	 * @param listN
	 * @param listS
	 * @return
	 * @note : what about sql injection and quote smart ??
	 */
	private <V> String returnQuerySyntax(String type, V receiverId) {
		if (QUERY_TYPE_N.equals(type)) {
			return " = " + receiverId;
		}
		    if (QUERY_TYPE_L.equals(type)) {
		    	return " LIKE " + "'%".concat((String) receiverId)
		    			.concat("%'") + " ";
		    }
		    return " = " + "'".concat((String) receiverId).concat("'");
	}


	public CommonReportingBO getCommonReportingBO() {
		return commonReportingBO;
	}

	public void setCommonReportingBO(CommonReportingBO commonReportingBO) {
		this.commonReportingBO = commonReportingBO;
	}

	public AlertNotificationDAO getAlertNotificationDAO() {
		return alertNotificationDAO;
	}

	public void setAlertNotificationDAO(AlertNotificationDAO alertNotificationDAO) {
		this.alertNotificationDAO = alertNotificationDAO;
	}
	public void setFixedEventBO(FixedEventBO fixedEventBO) {
	    this.fixedEventBO = fixedEventBO;
	}

	
	
	/**
	 * This method will check if this receiver criteria match the expression
	 * attached at the level of the event.
	 * 
	 * @param alertNtfCO
	 * @return
	 * @throws Exception
	 */
	private boolean applyEventFilter(AlertNtfCO alertNtfCO, HashMap<String, Object> allTags) throws Exception {

		EvtCO evtCO = alertNtfCO.getEvtCO();
		String exp = evtCO.getEventExp();

		// if event doesn't have a filter expression
		if (!StringUtil.nullToEmpty(evtCO.getEvtType()).equalsIgnoreCase("F")
				|| StringUtil.nullToEmpty(exp).isEmpty()) {
			return true;
		}

		// Remove the tag delimiter < >
		exp = exp.replaceAll(AlertEngineConstants.MSG_TAG_REGEX, "[$1]");
		ArrayList<Map<String, Object>> expressionParams = new ArrayList<>();
		expressionParams.add(allTags);

		// Apply the expression
		return (Boolean) commonLibBO.executeExpression(exp, 0, expressionParams);
	}

	/*
	 * return true or false if OMNI admin installed
	 */
	private boolean isOMNIAdminInstalled() throws BaseException {
		S_APPVO sAppVO = new S_APPVO();
		sAppVO.setAPP_NAME(AlertEngineConstants.OMNI_ADMIN_APP_NAME);
		return commonLibBO.returnApplication(sAppVO) != null;
	}
	
	
    /**
     * return All subscribers available in Group
     * 
     * @param groupId
     * @return
     * @throws DAOException
     */
    @Override
    public AlertNotificationCO returnSubscribersInGroup(AlertNotificationCO notificationCO) throws BaseException
    {
	notificationCO.setSubscriberIdsList(alertNotificationDAO.returnSubscribersInGroup(notificationCO));
	return notificationCO;
    }
    
    /**
     * check if any group contains Yes 
     * to include all subscribers
     * @param alertNotificationCO
     * @return
     * @throws DAOException
     */
    @Override
    public int countIfIncludeAllSubInGroup(AlertNotificationCO alertNotificationCO) throws BaseException {
	return alertNotificationDAO.countIfIncludeAllSubInGroup(alertNotificationCO);
    }
	    

}
