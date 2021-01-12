package com.path.alert.engine.logger;

import java.util.ArrayList;
import java.util.List;
import com.path.alert.bo.engine.AlertEngLoggingBO;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_COM_MODEVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_DETAILVO;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

public class AlertEngLogger {
	
	/**
	 * Alert engine logger Singleton instance
	 */
	private volatile static AlertEngLogger instance;
	
	/**
	 * Error Logger
	 */
	private final Log logger = Log.getInstance();

	/**
	 * Reference to Alert Engine BO
	 */
	private AlertEngLoggingBO alertEngLoggingBO;
	
	/**
	 * Monitor object
	 */
	private static Object monitor = new Object();
	
	/**
	 * Return Alert Engine Logger instance
	 */
	public static AlertEngLogger getInstance()
	{
		// check if Singleton instance not created yet
		// get the lock and let us create it
		if(instance == null){
			synchronized(monitor){
				if(instance == null){
					
					instance = new AlertEngLogger();
					instance.setAlertEngLoggingBO((AlertEngLoggingBO) ApplicationContextProvider
						.getApplicationContext().getBean("alertEngLoggingBO"));
					//@todo shoudln't we populate the configuration here??
				}
			}
		}
		
		return instance;
	}	
	

	/**
	 * Delete Receiver details and data from the online queue
	 * @param messageCO
	 */
	public void deleteReceiverData(AlertNtfCO messageCO) {
		try {
			getAlertEngLoggingBO().deleteReceiverData(messageCO);
		} catch (BaseException e) {
			logger.error(e, formatLog("Error while deleting receiver data"));
		}
	}
	
	
	/**
	 * Log Request
	 * @param requestVO 
	 * @return
	 */
	public boolean logEngRequest(ALRT_ENG_REQUESTVO requestVO){
		
		boolean loggingResult = true;
		
		try {
			getAlertEngLoggingBO().logEngRequest(requestVO);
		} catch (BaseException e) {
			loggingResult = false;
			logger.error(e,formatLog("Error while logging Request"));
		}
		
		return loggingResult;
	}

	
	/**
	 * Log Notifications by Receiver type
	 * @param taskList
	 * @param receiverType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean logRequestMessages(List<AlertNtfCO> notifications, String receiverType) {
		
		boolean loggingResult = true;
		try {
			loggingResult = getAlertEngLoggingBO().logEngMessages(notifications,receiverType);
		} catch (BaseException e) {
			loggingResult = false;
			//@todo add request id
			logger.error(e,formatLog(String.format("Error while logging Request Messages")));
		}
		
		return loggingResult;
	}
	
	
	/**
	 * Log Message details
	 * @param message
	 */
	public void logMsgDetail(AlertMessageCO message) {
		// TODO Auto-generated method stub
		List<ALRT_ENG_MSG_DETAILVO> messageInformation = new ArrayList<ALRT_ENG_MSG_DETAILVO>();
		String[] listOfMails = message.getEmails() != null ?
			message.getEmails().split(AlertEngineConstants.RECEIVER_SEPARATOR):null;
		
		if(listOfMails != null && listOfMails.length > 0 && message.getCommunicationType()
				.equalsIgnoreCase(AlertEngineConstants.COMM_TYPE_EMAIL)){
			// title
			ALRT_ENG_MSG_DETAILVO titleVO = new ALRT_ENG_MSG_DETAILVO();
			titleVO.setCONTENT_TYPE("SUBJECT");
			titleVO.setCONTENT(message.getMessageSubject());
			titleVO.setCOMM_MODE(AlertEngineConstants.COMM_TYPE_EMAIL);
			messageInformation.add(titleVO);
			
			ALRT_ENG_MSG_DETAILVO msgBodyVO = new ALRT_ENG_MSG_DETAILVO();
			msgBodyVO.setCONTENT_TYPE("BODY");
			msgBodyVO.setCONTENT(message.getMessageBody());
			msgBodyVO.setCOMM_MODE(AlertEngineConstants.COMM_TYPE_EMAIL);
			messageInformation.add(msgBodyVO);
			
			// list of attachement 
			// @todo later attachement name
			if(message.getAttachements() != null)
			{
				for(byte[] attachement : message.getAttachements() ){
					ALRT_ENG_MSG_DETAILVO attachVO = new ALRT_ENG_MSG_DETAILVO();
					attachVO.setCONTENT_TYPE("REPORT");
					attachVO.setCONTENT(attachement.toString());
					attachVO.setCOMM_MODE(AlertEngineConstants.COMM_TYPE_EMAIL);
					messageInformation.add(attachVO);
				}
			}
			
			
			
			// list of Emails 
			for(String email : listOfMails ){
				ALRT_ENG_MSG_DETAILVO emailVO = new ALRT_ENG_MSG_DETAILVO();
				emailVO.setCONTENT_TYPE("EMAIL");
				emailVO.setCONTENT(email);
				emailVO.setCOMM_MODE("E");
				messageInformation.add(emailVO);
			}
		}
		
		String[] mobilesNbs = message.getMobiles() != null ?
			message.getMobiles().split(AlertEngineConstants.RECEIVER_SEPARATOR):null;
		
		// log Sms 
		if(mobilesNbs != null && mobilesNbs.length > 0 && (message.getCommunicationType()
				.equalsIgnoreCase(AlertEngineConstants.COMM_TYPE_SMS)) ){
			
			// list of Emails 
			for(String phoneNumber : mobilesNbs ){
				ALRT_ENG_MSG_DETAILVO mobileVO = new ALRT_ENG_MSG_DETAILVO();
				mobileVO.setCONTENT_TYPE("PHONE_NB");
				mobileVO.setCONTENT(phoneNumber);
				mobileVO.setCOMM_MODE(AlertEngineConstants.COMM_TYPE_SMS);
				messageInformation.add(mobileVO);
				
				ALRT_ENG_MSG_DETAILVO smsBodyVO = new ALRT_ENG_MSG_DETAILVO();
				smsBodyVO.setCONTENT_TYPE("PHONE_BODY");
				smsBodyVO.setCONTENT(message.getMessageBody());
				smsBodyVO.setCOMM_MODE(AlertEngineConstants.COMM_TYPE_SMS);
				messageInformation.add(smsBodyVO);
			}
		}
		
		try {
			getAlertEngLoggingBO().logMsgDetail(message,messageInformation);
		} catch (BaseException e) {
			logger.error(e,formatLog(String.format("Error while logging Messages details")));
		}
		
	}

	
	/**
	 * @param message
	 * @param object
	 */
	public void logMsgByCommMode(AlertMessageCO message)
	{
		try {
			ALRT_ENG_MSG_COM_MODEVO msgComMode = new ALRT_ENG_MSG_COM_MODEVO();
			ALRT_ENG_MSGVO msgvo = message.getNtfCO().getMsgVO();
			msgComMode.setCOMM_MODE(message.getCommunicationType());
			msgComMode.setREQUEST_ID(msgvo.getREQUEST_ID());
			msgComMode.setMSG_ID(msgvo.getMSG_ID());
			msgComMode.setSTATUS(AlertEngineConstants.STATUS_ACTIVE_MSG);
			
			getAlertEngLoggingBO().logMsgByCommMode(msgComMode);
			
		} catch (BaseException e) {
			
			int requestId = 0,msgId = 0;
			if(message.getNtfCO() != null && message.getNtfCO().getMsgVO() != null) {
				requestId = NumberUtil.nullToZero(message.getNtfCO()
						.getMsgVO().getREQUEST_ID()).intValue();
				
				msgId = NumberUtil.nullToZero(message.getNtfCO()
						.getMsgVO().getMSG_ID()).intValue();
			}
			
			logger.error(e,formatLog(String.format("Error while logging Messages [%d][%d] By Communication mode %s",
					requestId,msgId,message.getCommunicationType())));
		}
		
	}
	
	
	/**
	 * @param msgVO
	 */
	public void updateRequestStatus(ALRT_ENG_REQUESTVO requestVO) {
		try {
			getAlertEngLoggingBO().updateRequestStatus(requestVO);
		} catch (BaseException e) {
			
			logger.error(e,formatLog(String.format("Error while Updating the Request status for Message Id [%d]",
					NumberUtil.nullToZero(requestVO.getREQUEST_ID()).intValue())));
		}
	}
	
	/**
	 * @param msgVO
	 */
	public void updateMsgStatus(ALRT_ENG_MSGVO msgVO) {
		try {
			getAlertEngLoggingBO().updateMsgStatus(msgVO);
		} catch (BaseException e) {
			
			logger.error(e,formatLog(String.format("Error while Updating the Msg status for Message Id [%d][%d]",
				NumberUtil.nullToZero(msgVO.getREQUEST_ID()).intValue(),
				NumberUtil.nullToZero(msgVO.getMSG_ID()).intValue())));
		}
	}
	
	
	/**
	 * Update Msg Status by Communication mode
	 * @param msgVO
	 */
	public void updateMsgByCommModeStatus(AlertMessageCO message, String status) {
		
		ALRT_ENG_MSG_COM_MODEVO msgComMode = new ALRT_ENG_MSG_COM_MODEVO();
		try {
			ALRT_ENG_MSGVO msgvo = message.getNtfCO().getMsgVO();
			msgComMode.setCOMM_MODE(message.getCommunicationType());
			msgComMode.setREQUEST_ID(msgvo.getREQUEST_ID());
			msgComMode.setMSG_ID(msgvo.getMSG_ID());
			msgComMode.setSTATUS(status);
			msgComMode.setOUTPUT_MSG(StringUtil.substring(message.getErrorDesc(),255));
			getAlertEngLoggingBO().updateMsgByCommModeStatus(msgComMode);
		} catch (BaseException e) {
			
			logger.error(e,formatLog(String.format("Error while Updating the Msg by Comm Mode status for Message Id [%d][%d]",
					NumberUtil.nullToZero(msgComMode.getREQUEST_ID()).intValue(),
					NumberUtil.nullToZero(msgComMode.getMSG_ID()).intValue())));
		}
	}
	
	/**
	 * @return the alertEngLoggingBO
	 */
	public AlertEngLoggingBO getAlertEngLoggingBO() {
		return alertEngLoggingBO;
	}


	/**
	 * @param alertEngLoggingBO the alertEngLoggingBO to set
	 */
	public void setAlertEngLoggingBO(AlertEngLoggingBO alertEngLoggingBO) {
		this.alertEngLoggingBO = alertEngLoggingBO;
	}


	/**
	 * Custom logging function
	 * @param alrt_ENG_REQUESTVO 
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertEngLogger] ")
			.append(this)
			.append(message);
		
		return sb.toString();
	}
}
