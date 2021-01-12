package com.path.alert.bo.webservice.engine.impl;

import java.util.HashMap;

import com.path.alert.bo.engine.AlertEngineBO;
import com.path.alert.bo.webservice.engine.AlertEngineWebServiceWrapperBO;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.bo.common.MessageCodes;
import com.path.bo.common.login.LoginBO;
import com.path.lib.common.base.WebServiceBaseBO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.SecurityUtils;
import com.path.vo.alert.notification.SendAlertNotificationReq;
import com.path.vo.alert.notification.SendAlertNotificationResp;
import com.path.vo.common.ImBaseRequest;

public class AlertEngineWebServiceWrapperBOImpl extends WebServiceBaseBO implements AlertEngineWebServiceWrapperBO {
	/**
	 * Hold Alert engine bo reference
	 */
	private AlertEngineBO alertEngineBO;
	
	//LoginBo for authenticate user
	private LoginBO loginBO;
	
	

	public HashMap<String, Object> sendAlertNotification(HashMap<String, Object> messageInfo) throws Exception {
		
		//initialize service call
	   initializeServiceCall(messageInfo);
		   
	   SendAlertNotificationResp sendAlertNotificationResp  = new SendAlertNotificationResp();
			
	   // Convert request HashMap to Req object 
	   SendAlertNotificationReq sendAlertNotificationReq = (SendAlertNotificationReq) PathPropertyUtil.convertToObject(messageInfo, SendAlertNotificationReq.class);
		   
	   Boolean skipSubscription = false;
	   if(messageInfo.containsKey("skipUsrAuth")) {
		   
		   Object ob = messageInfo.get("skipUsrAuth");
		   skipSubscription = Boolean.parseBoolean(ob.toString());
	   }
	   if(!skipSubscription)
	   {
		 //Authenticate User
		 authenticateUser(sendAlertNotificationReq);
	   }
	   
	   //copy all request input to the response
	   PathPropertyUtil.copyProperties(sendAlertNotificationReq , sendAlertNotificationResp);
	   PathPropertyUtil.copyProperties(sendAlertNotificationReq.getServiceContext(),sendAlertNotificationResp.getResponseServiceContext());
	   
	   
	   
	   AlertNotificationCO alertNotificationCO = new AlertNotificationCO();
	   
	   PathPropertyUtil.copyProperties(sendAlertNotificationReq , alertNotificationCO);
	   alertNotificationCO.setCompCode(sendAlertNotificationReq.getCompanyCode());
	   
	   /**
	    * set language in Notification
	    */
	   alertNotificationCO.setLangCode(sendAlertNotificationReq.getRequesterContext().getLangId());
	   
	   boolean isMessageSent = alertEngineBO.sendMessage(alertNotificationCO);
	   if(isMessageSent) 
	   {
	       sendAlertNotificationResp.getServiceResponse().setStatusDesc(AlertEngineConstants.MSG_QUEUE);
	   } else 
	   {
	       sendAlertNotificationResp.getServiceResponse().setStatusDesc(commonLibBO.returnTranslErrorMessage(
			AlertEngineConstants.FAILED_MSG_CODE, sendAlertNotificationReq.getRequesterContext().getLangId()));
	   }
	   //convert response to Hashmap
	   messageInfo =  PathPropertyUtil.convertToMap(sendAlertNotificationResp);
	   return messageInfo;
	}
	
	
	/**
	 * authenticate user
	 * @param baseRequest
	 * @return
	 */
	private int  authenticateUser(ImBaseRequest baseRequest) throws Exception
	{
		int isAuthenticateUser = 0;
		String user = (String) baseRequest.getRequesterContext().getUserID();
		
		String password = SecurityUtils.decodeB64((String) baseRequest.getRequesterContext().getPassword());
		//return 1 in case of success 
		isAuthenticateUser = loginBO.authenticateUserAndPass(user, password);
		if(isAuthenticateUser == 0)
		{
			throw new BOException(MessageCodes.INVALID_LOGON_PASSWORD);
		}
		return isAuthenticateUser;
	}

	public AlertEngineBO getAlertEngineBO() {
		return alertEngineBO;
	}

	public void setAlertEngineBO(AlertEngineBO alertNtfBO) {
		alertEngineBO = alertNtfBO;
	}


	public LoginBO getLoginBO() {
		return loginBO;
	}


	public void setLoginBO(LoginBO loginBO) {
		this.loginBO = loginBO;
	}

	
}
