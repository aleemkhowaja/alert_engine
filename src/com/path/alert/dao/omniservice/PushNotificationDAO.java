package com.path.alert.dao.omniservice;

import java.util.List;

import com.path.alert.vo.omniservice.PushNotificationCO;
import com.path.alert.vo.omniservice.PushNotificationSC;
import com.path.lib.common.exception.DAOException;


public interface PushNotificationDAO {
	
	public List<PushNotificationCO> returnPushNotifDeviceTokenList(PushNotificationSC sc) throws DAOException;

	public List<PushNotificationCO> returnPushNotificationList(PushNotificationSC sc) throws DAOException;
	
}
