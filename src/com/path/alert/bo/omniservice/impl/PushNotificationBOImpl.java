package com.path.alert.bo.omniservice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.path.alert.bo.omniservice.OmniServiceWsBO;
import com.path.alert.bo.omniservice.PushNotificationBO;
import com.path.alert.bo.omniservice.PushNotificationConstant;
import com.path.alert.dao.omniservice.PushNotificationDAO;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.omniservice.ALRT_PUSH_NOTIFICATIONVO;
import com.path.alert.vo.omniservice.PushNotificationCO;
import com.path.alert.vo.omniservice.PushNotificationSC;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

public class PushNotificationBOImpl extends BaseBO implements PushNotificationBO
{

    protected Log logger = Log.getInstance();

    private OmniServiceWsBO omniServiceWsBO;

    private PushNotificationDAO pushNotificationDAO;

    @Override
    public PushNotificationCO pushNotification(PushNotificationSC sc) throws BaseException
    {

	PushNotificationCO response = new PushNotificationCO();
	try
	{
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    String authKey = StringUtil.nullToEmpty(PathPropertyUtil.returnPathPropertyFromFile(
		    PushNotificationConstant.OSRV_PROPERTIES, PushNotificationConstant.PUSH_NOTIFICATION_AUTH_KEY));
	    HashMap<String, String> additionalHeaders = new HashMap<String, String>();
	    paramMap.put("Url", PushNotificationConstant.PUSH_NOTIFICATION_URL);
	    paramMap.put("ContentType", PushNotificationConstant.APPLICATION_JSON);
	    paramMap.put("Accept", PushNotificationConstant.APPLICATION_JSON);
	    additionalHeaders.put("Authorization", authKey);
	    paramMap.put("HttpMethod", PushNotificationConstant.METHOD_POST);
	    paramMap.put("AdditionalHeaders", additionalHeaders);

	    List<PushNotificationCO> COs = pushNotificationDAO.returnPushNotifDeviceTokenList(sc);
	    for(PushNotificationCO pushNotificationCO : COs)
	    {
		HashMap<String, Object> inputParamMap = new HashMap<String, Object>();
		HashMap<String, Object> notifParamMap = new HashMap<String, Object>();
		HashMap<String, Object> dataParamMap = new HashMap<String, Object>();
		notifParamMap.put("title", sc.getTitle());
		notifParamMap.put("body", sc.getBody());
		notifParamMap.put("sound", PushNotificationConstant.PUSH_NOTIFICATION_SOUND);
		notifParamMap.put("click_action", PushNotificationConstant.PUSH_NOTIFICATION_CLICK_ACTION);
		notifParamMap.put("icon", PushNotificationConstant.PUSH_NOTIFICATION_ICON);
		inputParamMap.put("notification", notifParamMap);
		dataParamMap.put("landing_page", sc.getLandingPage());
		dataParamMap.put("notif_data", sc.getNotifData());
		inputParamMap.put("data", dataParamMap);
		inputParamMap.put("priority", PushNotificationConstant.PUSH_NOTIFICATION_PRIORITY);
		inputParamMap.put("restricted_package_name", "");
		inputParamMap.put("to", pushNotificationCO.getDeviceToken());

		paramMap.put("InputParameter", inputParamMap);

		/**
		 * Call the method which used to call rest service to firebase
		 * for sending Push Notifiation
		 * 
		 * @Author: Alim Khowaja
		 */
		HashMap<String, Object> outputParamMap = omniServiceWsBO.sendPushNotification(paramMap);

		/**
		 * After calling firebase Need to save logs in
		 * ALRT_PUSH_NOTIFICATION table
		 * 
		 * @Author: Alim Khowaja
		 */
		ALRT_PUSH_NOTIFICATIONVO vo = new ALRT_PUSH_NOTIFICATIONVO();
		vo.setCOMP_CODE(sc.getCompCode());
		vo.setAPP_ID(sc.getAppId());
		vo.setCHNL_ID(sc.getChannelId());
		/**
		 * If result is success from firebase rest Service there we need
		 * to set status unread Otherwise set as Failed
		 * 
		 * @Author: Alim Khowaja
		 */
		if((Boolean) outputParamMap.get("result"))
		{
		    vo.setSTATUS(PushNotificationConstant.STATUS_UNREAD);
		    response.setOutputCode(PushNotificationConstant.SUCCESS_OUTPUT_CODE);
		}
		else
		{
		    vo.setSTATUS(PushNotificationConstant.STATUS_FAILED);
		    response.setOutputCode(PushNotificationConstant.EXCEPTION_OUTPUT_CODE);
		}
		vo.setTITLE(sc.getTitle());
		vo.setBODY(sc.getBody());
		vo.setLANDING_PAGE(sc.getLandingPage());
		vo.setNOTIFICATION_DATA(sc.getNotifData());
		vo.setNOTIFICATION_DEVICE_TOKEN_ID(pushNotificationCO.getNotifDeviceToekenId());
		vo.setCHANNEL_USER_NAME(sc.getChannelUserName());
		vo.setCREATED_DATE(commonLibBO.returnSystemDateWithTime());
		genericDAO.insert(vo);
	    }
	    response.setOutputNotification(PushNotificationConstant.SUCCESS_OUTPUT_DESC);
	    response.setOutputType(PushNotificationConstant.SUCCESS_OUTPUT_TYPE);
	}
	catch(Exception e)
	{
	    logger.error(e, e.getMessage());
	    response.setOutputCode(PushNotificationConstant.EXCEPTION_OUTPUT_CODE);
	    response.setOutputNotification(PushNotificationConstant.EXCEPTION_OUTPUT_DESC);
	    response.setOutputType(PushNotificationConstant.FATAL_OUTPUT_TYPE);
	}
	return response;
    }

    public void setOmniServiceWsBO(OmniServiceWsBO omniServiceWsBO)
    {
	this.omniServiceWsBO = omniServiceWsBO;
    }

    public void setPushNotificationDAO(PushNotificationDAO pushNotificationDAO)
    {
	this.pushNotificationDAO = pushNotificationDAO;
    }
}