package com.path.alert.bo.webservice.engine;

import java.util.HashMap;

public interface AlertEngineWebServiceWrapperBO
{
    public HashMap<String, Object> sendAlertNotification(HashMap<String, Object> messageInfo) throws Exception;

}
