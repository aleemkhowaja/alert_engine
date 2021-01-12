package com.path.alert.engine.core.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.path.alert.bo.notification.AlertNotificationBO;
import com.path.alert.engine.core.sender.AlertDefaultSender;
import com.path.alert.engine.logger.AlertEngLogger;
import com.path.alert.engine.utils.AlertEngUtils;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.engine.utils.StopWatch;
import com.path.alert.vo.engine.AlertMessageListCO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.path.lib.log.PathFormatter;

/**
 * Use Message Adapter This class hold the Message Async consumer behavior
 * 
 * @author Mohammad Ali Mezzawi
 */
public class AlertMessageListener2 implements MessageListener {
	
	/**
	 * Hold logger reference
	 */
	private Log logger = Log.getInstance();
	
	/**
	 * 
	 */
	private AlertNotificationBO notificationBO;
	
	/**
	 * 
	 */
	private AlertDefaultSender sender;

	/**
	 * @param sender 
	 * @param senderThreadPoolExecutor
	 */
	public AlertMessageListener2(AlertDefaultSender sender) {
		
		logger.debug(formatLog("Listener Created", null));
		notificationBO = (AlertNotificationBO) ApplicationContextProvider
				.getApplicationContext().getBean("alertNotificationBO");
		
		this.sender = sender;
	}
	
	
	/**
	 * @note @todo : Should we send MapMessage or ObjectMessage
	 * Performance and Serialization should be kept in mind
	 */
	@SuppressWarnings("rawtypes")
	public void onMessage(Message message) {
		
		StopWatch stopWatch = new StopWatch();
		AlertNtfCO alertNtfCO = null;
		try {
			
			logger.debug(formatLog("[onMessage] Receive dispatched Message",null));
			stopWatch.start("list.onMessage");
			
			// get alert NotificationCo from the message
			alertNtfCO = (AlertNtfCO) ((ObjectMessage)message).getObject();
			
			// set status and start date
			alertNtfCO.getMsgVO().setMACHINE_NAME_IP(AlertEngUtils.getComputerName());
			alertNtfCO.getMsgVO().setSTATUS(AlertEngineConstants.STATUS_ACTIVE_MSG);
			AlertEngLogger.getInstance().updateMsgStatus(alertNtfCO.getMsgVO());
			
			logger.debug(formatLog("[onMessage] Set Message as Active",alertNtfCO));
			
			// later move the get event to this stage
			// event name is needed in all the below processing
			/*if(StringUtil.nullToEmpty(eventName).isEmpty()){
			    eventName = notificationBO.selectEventNameByID(
			    		alertNtf.getMsgVO().getEVENT_ID());
			}*/
			
			
			
			// get subscriber id
			alertNtfCO = notificationBO.getSubscriberDetails(alertNtfCO);
			// message.acknowledge();

			if (alertNtfCO.getErrorCode().intValue() <= 0){
				logger.debug(formatLog("[onMessage] User isn't a subscriber",alertNtfCO));
				alertNtfCO.getMsgVO().setSTATUS(AlertEngineConstants.STATUS_SKIPPED_MSG);
				alertNtfCO.getMsgVO().setOUTPUT_MSG(alertNtfCO.getErrorDesc());
				AlertEngLogger.getInstance().updateMsgStatus(alertNtfCO.getMsgVO());
				return;
			}
			
			logger.debug(formatLog("[onMessage] Start Preparing Message",alertNtfCO));
			AlertMessageListCO alertMessageListCO = notificationBO.prepareMessage(alertNtfCO);
			
			int prepareMessageStatus = alertMessageListCO.getErrorCode().intValue();
			if (prepareMessageStatus <= 0){
				logger.debug(formatLog(alertMessageListCO.getErrorDesc(),alertNtfCO));
				alertNtfCO.getMsgVO().setSTATUS(prepareMessageStatus == 0 ? 
					AlertEngineConstants.STATUS_SKIPPED_MSG : AlertEngineConstants.STATUS_FAILED_MSG);
				alertNtfCO.getMsgVO().setOUTPUT_MSG(alertMessageListCO.getErrorDesc());
				AlertEngLogger.getInstance().updateMsgStatus(alertNtfCO.getMsgVO());
				return;
			}
			
			
			// get the sender and send the message
			logger.debug(formatLog("[onMessage] Msg Handling done -> Now Send Msg",alertNtfCO));
			sender.send(alertMessageListCO);
			
		} catch (Exception e) {
			
			// log Message Info
			if(alertNtfCO != null && alertNtfCO.getMsgVO() != null ){
				
				/**
				 *  @note : currently we will truncate the first 4000 later we will implement logging
				 *  mechanism with CLOB
				 */
			    	String exceptionMsg = PathFormatter.toString(e, false);
			    	exceptionMsg = StringUtil.substring(exceptionMsg, 3900);
			    		
				alertNtfCO.getMsgVO().setSTATUS(AlertEngineConstants.STATUS_FAILED_MSG);
				alertNtfCO.getMsgVO().setOUTPUT_MSG("Exception Found while Building Msg =>" 
						+ exceptionMsg );
				AlertEngLogger.getInstance().updateMsgStatus(alertNtfCO.getMsgVO());
				
				String duration = stopWatch.returnDuration("list.onMessage");
				logger.error(e,formatLog(String.format(
					"[onMessage] Error while handling Message , Duration [%s]", duration),alertNtfCO));
			}
		} finally {
			
			if(alertNtfCO != null && alertNtfCO.getMsgVO() != null ){
				String duration = stopWatch.returnDuration("list.onMessage");
				logger.debug(formatLog(String.format("[onMessage][Finalized], Duration [%s]", duration),alertNtfCO));
			}
		}
	}
	
	
	/**
	 * Custom logging function
	 * @param string
	 */
	@SuppressWarnings("rawtypes")
	private String formatLog(String message , AlertNtfCO alertNtfCO) {
		
		String threadId = Thread.currentThread().getName();
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertMessageListener] ")
			.append(this)
			.append(String.format("[Thread Name : %s]", threadId));
		
		// add Message Information
		if(alertNtfCO != null){
			sb.append(String.format("[Request Id: %d Message ID: %d]"
					,alertNtfCO.getMsgVO().getREQUEST_ID().intValue(),
					alertNtfCO.getMsgVO().getMSG_ID().intValue()));
		}
		
		sb.append(message);
		
		return sb.toString();
	}
	

}