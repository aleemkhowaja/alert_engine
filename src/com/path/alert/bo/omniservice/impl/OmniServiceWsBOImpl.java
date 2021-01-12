package com.path.alert.bo.omniservice.impl;

import java.util.HashMap;
import java.util.Map;

import com.path.alert.bo.omniservice.OmniServiceWsBO;
import com.path.alert.bo.omniservice.PushNotificationConstant;
import com.path.lib.common.base.BaseBO;
import com.path.lib.ws.RestHttpUrlConnectionHandler;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class OmniServiceWsBOImpl extends BaseBO implements OmniServiceWsBO
{

    @Override
    public HashMap<String, Object> sendPushNotification(Map<String, Object> inputParamMap) throws Exception
    {
	HashMap<String, Object> outputParamMap = new HashMap<String, Object>();
	
	RestHttpUrlConnectionHandler restConHandleer = new RestHttpUrlConnectionHandler();
	String restResponse = restConHandleer.executeRestCall(inputParamMap);
	JSONObject jsonResponseObject= null ;
	Boolean result = false;
	try 
	{
	    jsonResponseObject = (JSONObject) JSONSerializer.toJSON(restResponse);
	    if(null != jsonResponseObject &&  jsonResponseObject.has("success") && 
		    jsonResponseObject.getString("success").equals(PushNotificationConstant.ONE))
	    {
		result = true;
	    }
	} catch (JSONException jex) 
	{
	    log.error("Error in OmniServiceWsBOImpl method sendPushNotification"+jex);
	}
	outputParamMap.put("result", result);
	return outputParamMap;
    }

}
