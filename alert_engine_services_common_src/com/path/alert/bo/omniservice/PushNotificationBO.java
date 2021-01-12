package com.path.alert.bo.omniservice;

import com.path.alert.vo.omniservice.PushNotificationCO;
import com.path.alert.vo.omniservice.PushNotificationSC;
import com.path.lib.common.exception.BaseException;

public interface PushNotificationBO {
	
	public PushNotificationCO pushNotification(PushNotificationSC sc) throws BaseException; 
   

}
