package com.path.alert.engine.core.sender;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.StringJoiner;

import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.bo.omniservice.PushNotificationBO;
import com.path.alert.bo.omniservice.PushNotificationConstant;
import com.path.alert.bo.smsservice.SMSServiceWsBO;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.logger.AlertEngLogger;
import com.path.alert.engine.utils.AlertEngUtils;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.alerts.ALRT_EVT_COMM_INTRNLVO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.alert.vo.engine.AlertMessageListCO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.notification.SubscriberCO;
import com.path.alert.vo.omniservice.PushNotificationCO;
import com.path.alert.vo.omniservice.PushNotificationSC;
import com.path.alert.vo.smsservice.SendSMSResp;
import com.path.bo.common.CommonLibBO;
import com.path.bo.common.WebServiceCommonBO;
import com.path.bo.common.alerts.AlertsBO;
import com.path.bo.common.email.EmailSenderBO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.PTH_CTRLVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.SecurityUtils;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.path.lib.log.PathFormatter;
import com.path.vo.common.email.MailCO;
import com.path.vo.common.email.MailServerCO;
import com.path.vo.core.alerts.AlertsRequestParamSC;

/**
 * This class house all behavior of engine sender.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AlertDefaultSender implements AlertSender {

	/**
	 * Alert sender name
	 */
	private String name;
	
	/**
	 * @todo we should inject it ( inject email sender or commonlib ??!!)
	 * @todo should we create the alertSender bo as bean
	 * @param senderName
	 */
	private EmailSenderBO emailSenderBO;
	
	/**
	 * reference to WebServiceCommonBO
	 */
	private WebServiceCommonBO wsServiceBo;
	
	/**
	 * Hold the sender configuration
	 */
	private AlertSenderConfig config;
	
	private CommonLibBO commonLibBO;
	
	/**
	 * Hold the Omni Push Notification 
	 */
	private PushNotificationBO pushNotificationBO;
	
	/**
	 * Hold the Sms Service BO
	 */
	private SMSServiceWsBO smsServiceWsBO;
	
	/**
	 * 
	 */
	private AlertsBO alertsBO;
	
	/**
	 * Hold logger reference
	 */
	private final Log logger = Log.getInstance();
	
	/**
	 * @param senderName
	 */
	public AlertDefaultSender( String senderName ){
		
		name = senderName;
		// get manual wire the BO
		setEmailSenderBO((EmailSenderBO) ApplicationContextProvider
				.getApplicationContext().getBean("emailSenderBO"));
		
		setWsServiceBo((WebServiceCommonBO) ApplicationContextProvider
				.getApplicationContext().getBean("webServiceCommonBO"));
		
		setCommonLibBO((CommonLibBO) ApplicationContextProvider.getApplicationContext().getBean("commonLibBO"));
		
		/**
		 * Omni Push Notification
		 */
		setPushNotificationBO((PushNotificationBO) ApplicationContextProvider.getApplicationContext().getBean("pushNotificationBO"));
		
		setSmsServiceWsBO((SMSServiceWsBO) ApplicationContextProvider.getApplicationContext().getBean("smsServiceWsBO"));
		
		setAlertsBO((AlertsBO) ApplicationContextProvider.getApplicationContext().getBean("alertsBO"));
		
		applyDefaultConfig();
		logger.debug(formatLog("Create sender",null));
	}
	
	
	/**
	 * This method will send a bunch of messages
	 * @return
	 */
	public boolean send(AlertMessageListCO alertMessageListCO) throws BaseException{
		
		if(alertMessageListCO.getAlertMessageList() == null
			|| alertMessageListCO.getAlertMessageList().isEmpty())
		{
		    throw new BaseException(formatLog("Message list is empty", null));
		}
		
		boolean result = true;
		ALRT_ENG_MSGVO msgVO = null;
		
		try{
			
			msgVO = alertMessageListCO.getAlertMessageList()
				.get(0).getNtfCO().getMsgVO();
			
			StringJoiner joiner =  new StringJoiner(",");
			for (AlertMessageCO message : alertMessageListCO.getAlertMessageList()) {
				try {
					result &= send(message);
					
					if(StringUtil.isNotEmpty(message.getErrorDesc()))
					    joiner.add(message.getErrorDesc());
					
				} catch (Exception e) {
					result = false;
					logger.error(e,formatLog("[send]",message));
				}
			}
			
			/**
			 * Send Internal Alerts
			 */
			result &= sendInternalAlert(alertMessageListCO);
			joiner.add(alertMessageListCO.getErrorDesc());
			
			// update message status
			msgVO.setSTATUS(result?AlertEngineConstants.STATUS_SENT_MSG:AlertEngineConstants.STATUS_FAILED_MSG);
			msgVO.setOUTPUT_MSG(joiner.toString());
			AlertEngLogger.getInstance().updateMsgStatus(msgVO);
			
			afterSending(alertMessageListCO,result);
			
		}catch(Exception exp){
			result = false;
			logger.error(exp,formatLog("[sendSms] While Sending the Message",null));
			
			// try to log for the last time
			if(null != msgVO){
				msgVO.setSTATUS(AlertEngineConstants.STATUS_FAILED_MSG);
				msgVO.setOUTPUT_MSG(PathFormatter.toString(exp, true));
				AlertEngLogger.getInstance().updateMsgStatus(msgVO);
			}
		}
		
		return result;
	}

	
	/**
	 * This method will send a message based on communication type
	 * @return
	 * @throws BaseException 
	 */
	public boolean send(AlertMessageCO message){
		
		boolean result = true;
		
		try{
			// log Message by Communication mode 
			AlertEngLogger.getInstance().logMsgByCommMode(message);
			
			 // log Msg Detail
			AlertEngLogger.getInstance().logMsgDetail(message);
			
			/**
			 * We should send message in case error code is Success ( 1 )
			 */
	    	if(message.getErrorCode().intValue() != 
	    			AlertEngineConstants.SUCCESS_CODE.intValue()) {
	    		result = false;
	    		return result;
	    	}
	    		
			
			// send message base on communication type
			switch (message.getCommunicationType()){
				case AlertEngineConstants.COMM_TYPE_EMAIL:
					result &= sendMail(message);
					break;
				case AlertEngineConstants.COMM_TYPE_SMS:
					result &= sendSms(message);
					break;
				case AlertEngineConstants.COMM_TYPE_OMNI:
					result &= sendOmni(message);
					break;
			}
			
		}catch(Exception e){
			result = false;
			logger.error(e,formatLog("[sendSms] While Sending the Message",null));
		}finally {
			
			// log the communication mode status as failed
			AlertEngLogger.getInstance().updateMsgByCommModeStatus(message,
					result?AlertEngineConstants.STATUS_SENT_MSG:AlertEngineConstants.STATUS_FAILED_MSG);
		}
		
		return result;
	   
	}
	
	
	/**
	 * This method return all information about scheduler as string
	 * @return
	 */
	public String toString() {
		return String.format("[AlertDefaultSender] [%s] ", getName());
	}
	
	
	/**
	 * This method is called just after sending the message to execute
	 * any needed behavior like cleaning the message draft data.
	 * @param alertMessageListCO
	 * @param result
	 */
	private void afterSending(AlertMessageListCO alertMessageListCO, boolean result) {
		
		/**
		 * If the system failed to send the message we will keep all
		 * data for debugging. 
		 */
		if(!result) {
			return;
		}
		
		/**
		 * check if we have a receiver id
		 */
		AlertNtfCO messageCO = alertMessageListCO.getAlertMessageList()
			.get(0).getNtfCO();
		
		/**
		 * Clear Online queue data
		 */
		if(null != messageCO.getAlrtReceiverID()) {
			AlertEngLogger.getInstance()
				.deleteReceiverData(messageCO);
		}
	}
	
	
	/**
	 * This method will send sms message
	 * @param message
	 * @throws BaseException 
	 */
	private boolean sendSms(AlertMessageCO message)
	{
		boolean result = true;
		
		try{
			
			String[] mobilesNbs = message.getMobiles().split(AlertEngineConstants.RECEIVER_SEPARATOR);
			HashMap<String,Object> msgHm = AlertEngUtils.fillSmsVariablesMap(message);
			
			/**
			 * 
			 */
			BigDecimal smsMappingId  = config.isDefaultSms() ? 
					AlertEngineConstants.DEAFULT_SMS_PWS_MAPPING_ID : AlertEngineConstants.CUSTOM_SMS_PWS_MAPPING_ID;
			
			HashMap<String,Object> outputHM = new HashMap<String,Object>();
			// loop over number
			for(String mobileNb : mobilesNbs){
				
				try{
					// override the mobile number
					msgHm.put("as_mobile_phone", mobileNb);
					
					
					/**
					 * add process = 1 when default sms active
					 * and 0 for custom sms
					 */
					BigDecimal processed =  config.isDefaultSms() ? BigDecimal.ONE : BigDecimal.ZERO;
					msgHm.put("an_processed", processed);
					    	

					/**
					 * call sms procedure to insert in SMS_Messages table
					 */
					outputHM = smsServiceWsBO.sendSMS(msgHm);
					
					/**
					 * 
					 */
					if(config.isDefaultSms()) {
					    result &= true;
					    continue;
					}
					    
					/**
					 * Call Pws for Custom Sms 
					 */
					wsServiceBo.callPWS(smsMappingId, msgHm);

					result &= true;
				    	if(outputHM != null && !NumberUtil.isEmptyDecimal(new BigDecimal(outputHM.get("code").toString())));
				    	{
				    	    SendSMSResp resp = new SendSMSResp();
				    	    resp.setCode(new BigDecimal(outputHM.get("code").toString()));
				    	    smsServiceWsBO.updateProcessedCodeForSms(resp); 
				    	}
					
				}catch(Exception e){
				        
					logger.error(e,formatLog("[sendSms]",message));
					result = false;
				}
			}
			
		}catch(Exception e){
			logger.error(e,formatLog("[sendSms] While Sending SMS",null));
			message.setErrorDesc(PathFormatter.toString(e, true));
			result = false;
		}
		
		return result;
	}
	
	
    /**
     * This method will send OMNI message
     * 
     * @param message
     * @throws BaseException
     */
    private boolean sendOmni(AlertMessageCO message)
    {
	boolean result = true;

	try
	{

	    PushNotificationSC pushNotificationSC = new PushNotificationSC();
	    
	    String appId = message.getNtfCO().getTagEvent() != null && message.getNtfCO().getTagEvent().containsKey(PushNotificationConstant.APP_ID) ?
		    message.getNtfCO().getTagEvent().get(PushNotificationConstant.APP_ID).toString() : null;
		    
            String channelId = message.getNtfCO().getTagEvent() != null && message.getNtfCO().getTagEvent().containsKey(PushNotificationConstant.CHANNEL_ID) ?
		    message.getNtfCO().getTagEvent().get(PushNotificationConstant.CHANNEL_ID).toString() : null;
		    
	    
	    /**
	     * check the following conditions to return back
	     * check if omni installed
	     * 1. If event tags are null
	     * 2. If eventTagsMap doesn't contains appId
	     * 2. If eventTagsMap doesn't contains channelId
	     */
	    if(AlertEngineConstants.IS_OMNI_INSTALLED)
	    {
		if(null == appId || null == channelId )
		{
		    message.setErrorCode(AlertEngineConstants.FAILED_CODE);
		    message.setErrorDesc(PushNotificationConstant.APP_CHANNEL_ID_REQUIRED);
		    return false;
		}
		/**
		 * check if appid and channel id is not number
		 */
		else if(!NumberUtil.isNumber(appId) || !NumberUtil.isNumber(channelId))
		{
		    message.setErrorCode(AlertEngineConstants.FAILED_CODE);
			 message.setErrorDesc(PushNotificationConstant.APP_CHANNEL_ID_SHOULD_NUMERIC);
			 return false;
		}
		
		pushNotificationSC.setAppId(new BigDecimal(appId));
		pushNotificationSC.setChannelId(new BigDecimal(channelId));
	    }
	    pushNotificationSC.setTitle(message.getMessageSubject());
	    pushNotificationSC.setBody(message.getMessageBody());
//	    pushNotificationSC.setNotifData(message.getMessageBody());//to add later in the screen 
	    pushNotificationSC.setLandingPage("notifscreen");
	    
	    pushNotificationSC.setCompCode(NumberUtil.isEmptyDecimal(message.getNtfCO().getCompCode())?
		    message.getNtfCO().getEvtCO().getCompCode():message.getNtfCO().getCompCode());
	    
	    pushNotificationSC.setChannelUserName(message.getNtfCO().getSubscriberCO().getChannelEndUser());
	    
	    /**
	     * push Omni Notification
	     */
	    PushNotificationCO response = pushNotificationBO.pushNotification(pushNotificationSC);
	    if(null == response ||response.getOutputCode() == PushNotificationConstant.EXCEPTION_OUTPUT_CODE)
	    {
		logger.error(formatLog("[sendOmni] While Sending Push Notification : ",message));
		message.setErrorCode(AlertEngineConstants.FAILED_CODE);
		message.setErrorDesc(PushNotificationConstant.FAILED_TO_SEND_PUSH_NOTIFICATION);
		return false;
	    }
	}
	catch(Exception e)
	{
	    logger.error(e, formatLog("[sendOmni] While Sending Push Notification", null));
	    message.setErrorDesc(PathFormatter.toString(e, true));
	    result = false;
	}

	return result;
    }

	
	/**
	 * This method will send mail message
	 * @param message
	 */
	private static final String SMTP_PASS_ENC_KEY = "@SMTPkey@Path17@";
	private boolean sendMail(AlertMessageCO message)
	{
		String msgBody = "";
		String msgSubj = "";
		
		try {
			
			MailCO mailMsg = new MailCO();
			msgBody = StringUtil.nullToEmpty(message.getMessageBody());
			msgSubj = StringUtil.nullToEmpty(message.getMessageSubject());
			
			mailMsg.setSubject(msgSubj);
			mailMsg.setBodyMail(new StringBuffer(msgBody));
			
			// get Email list
			String[] listOfMails = message.getEmails().split(AlertEngineConstants.RECEIVER_SEPARATOR);
			
			 // attach linked report ?? do we have multiple attachement ??
		    if(message.getAttachements() != null && message.getAttachements().size() > 0)
		    {
		    	mailMsg.setAttachmentList(message.getAttachements());
		    	mailMsg.setFilesNames(message.getAttachementName());
		    }
		    
		    // set mails
		    mailMsg.setTo(listOfMails);
		    
		    // insert debugging
			logger.debug(formatLog(String.format("[sendMail] Sending email to %s => Subject: %s, Body: %s",
					message.getEmails(),msgSubj,StringUtil.substring(msgBody, 100)),message));

			
			// if default smtp is enabled
			if( getConfig().isDefaultSmtp()){
				
				PTH_CTRLVO vo = commonLibBO.returnPthCtrl();
				//in case sads details missing then use default sender configured on the server
				if(vo.getSMTP_IP() == null || vo.getSMTP_PASSWORD() == null || vo.getSMTP_USER() == null )
				{
					emailSenderBO.sendEmail(mailMsg);
				}
				else
				{
					MailServerCO mailServerCO = new MailServerCO();
					mailServerCO.setHost(vo.getSMTP_IP());
					if(NumberUtil.isNumber(vo.getSMPT_PORT())) {
						mailServerCO.setPort(new BigDecimal(vo.getSMPT_PORT()).intValue());
					}
					mailServerCO.setUserName(vo.getSMTP_USER());
					mailMsg.setFrom(vo.getSMTP_SENDER());
					//decrypting password based on sads encryption 
					mailServerCO.setPassword(SecurityUtils.decryptAES(SMTP_PASS_ENC_KEY, vo.getSMTP_PASSWORD()));
					mailServerCO.setProtocol("smtp");
					emailSenderBO.sendEmail(mailMsg,mailServerCO);
				}
				return true;
			}
			
			// else use the custom mail configuration
			HashMap<String,Object> msgHm = PathPropertyUtil.convertToMap(mailMsg);
			wsServiceBo.callPWS(AlertEngineConstants.EMAIL_PWS_MAPPING_ID,msgHm);
			//@todo fix this
			return true;
			
		} catch (Exception e) {
			
			message.setErrorDesc(PathFormatter.toString(e, true));
			String msgbody = StringUtil.nullToEmpty(message.getMessageBody());
			logger.error(e,formatLog(String.format("[sendMail] Failed to Send email to %s => Subject: %s, Body: %s",
					message.getEmails(),msgSubj,StringUtil.substring(msgbody, 100)),message));
		}
		
		return false;
	}
	
	/**
	 * Send Internal Alerts
	 * @param alertMessageListCO
	 * @throws BaseException
	 */
	private boolean sendInternalAlert(AlertMessageListCO alertMessageListCO) throws BaseException
		
	{
	    
	    SubscriberCO co =  alertMessageListCO.getAlertMessageList().get(0).getNtfCO().getSubscriberCO();
	    
	    ALRT_EVT_COMM_INTRNLVO alrt_EVT_COMM_INTRNLVO = alertMessageListCO.getAlrt_EVT_COMM_INTRNLVO();
	   // alertMessageListCO.getAlertMessageList().get(0).getNtfCO().getSubscriberCO().get
	    AlertsRequestParamSC alertsRequestParamSC = new AlertsRequestParamSC();
	    alertsRequestParamSC.setCompCode(alrt_EVT_COMM_INTRNLVO.getCOMP_CODE());
	    alertsRequestParamSC.setBranchCode(alrt_EVT_COMM_INTRNLVO.getBRANCH_CODE());
	    alertsRequestParamSC.setSenderUserId(StringUtil.nullEmptyToValue(alrt_EVT_COMM_INTRNLVO.getSENDER_USR_ID(), "SYSTEM"));
	    alertsRequestParamSC.setReceiverUserId(co.getUserId());
	    alertsRequestParamSC.setTodoApplication(alrt_EVT_COMM_INTRNLVO.getAPP_NAME());
	    alertsRequestParamSC.setTodoParam(alrt_EVT_COMM_INTRNLVO.getTODO_KEY());
	    alertsRequestParamSC.setTodoAlert(alrt_EVT_COMM_INTRNLVO.getTODO_ALRT());
	    alertsRequestParamSC.setTodoProgRef(alrt_EVT_COMM_INTRNLVO.getTODO_PROG_REF());
	    alertsRequestParamSC.setDateReceived(commonLibBO.returnSystemDateWithTime());
	    
	    /**
	     * Validate the reciever user Id
	     */
	    if(StringUtil.isEmptyString(alertsRequestParamSC.getReceiverUserId()))
	    {
		alertMessageListCO.setErrorCode(AlertEngineConstants.FAILED_CODE);
		alertMessageListCO.setErrorDesc(AlertNotificationConstant.FAILED_TO_SEND_INTERNAL_ALRT + ":" +
						AlertNotificationConstant.MISSING_RECEIVER_USR_ID);
		return false;
	    }
	    /**
	     * send Alert
	     */
	    alertsBO.sendAlert(alertsRequestParamSC, AlertEngine.getInstance().getConnCO());
	    
	    return true;
	}
	
	/**
	 * Apply Default configuration
	 */
	private void applyDefaultConfig() {
		
		AlertSenderConfig config = new AlertSenderConfig();
		setConfig(config);
		
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return
	 */
	public EmailSenderBO getEmailSenderBO() {
		return emailSenderBO;
	}

	/**
	 * @param emailSenderBO
	 */
	public void setEmailSenderBO(EmailSenderBO emailSenderBO) {
		this.emailSenderBO = emailSenderBO;
	}

	/**
	 * @return
	 */
	public WebServiceCommonBO getWsServiceBo() {
		return wsServiceBo;
	}

	
	/**
	 * @param wsServiceBo
	 */
	public void setWsServiceBo(WebServiceCommonBO wsServiceBo) {
		this.wsServiceBo = wsServiceBo;
	}


	/**
	 * @return
	 */
	public AlertSenderConfig getConfig() {
		return config;
	}


	/**
	 * @param config
	 */
	public void setConfig(AlertSenderConfig config) {
		this.config = config;
	}
	
	
	/**
	 * Custom logging function
	 * @param string
	 */
	private String formatLog(String message, AlertMessageCO messageCO) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][DefaultSender][")
			.append(name).append("]")
			.append(this);
		
		if(messageCO != null){
			sb.append(String.format("Request ID: [%d] , Event ID: [%d] , Message Id [%d]",
					messageCO.getNtfCO().getMsgVO().getREQUEST_ID().intValue(),
					messageCO.getNtfCO().getMsgVO().getEVENT_ID().intValue(),
					messageCO.getNtfCO().getMsgVO().getMSG_ID().intValue()));
		}
		
		sb.append(message);
		
		return sb.toString();
	}


	public CommonLibBO getCommonLibBO() {
		return commonLibBO;
	}


	public void setCommonLibBO(CommonLibBO commonLibBO) {
		this.commonLibBO = commonLibBO;
	}


	public PushNotificationBO getPushNotificationBO() {
		return pushNotificationBO;
	}


	public void setPushNotificationBO(PushNotificationBO pushNotificationBO) {
		this.pushNotificationBO = pushNotificationBO;
	}

	public SMSServiceWsBO getSmsServiceWsBO()
	{
	    return smsServiceWsBO;
	}

	public void setSmsServiceWsBO(SMSServiceWsBO smsServiceWsBO)
	{
	    this.smsServiceWsBO = smsServiceWsBO;
	}


	/**
	 * @return the alertsBO
	 */
	public AlertsBO getAlertsBO()
	{
	    return alertsBO;
	}


	/**
	 * @param alertsBO the alertsBO to set
	 */
	public void setAlertsBO(AlertsBO alertsBO)
	{
	    this.alertsBO = alertsBO;
	}
	
	

}