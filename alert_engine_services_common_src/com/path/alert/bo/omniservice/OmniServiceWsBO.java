package com.path.alert.bo.omniservice;

import java.util.HashMap;
import java.util.Map;

public interface OmniServiceWsBO {
    
    
    /**
     * send omni push notificaton
     * @param msgHm
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> sendPushNotification( Map<String,Object> inputParamMap) throws Exception;

}
