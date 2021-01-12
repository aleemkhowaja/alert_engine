package com.path.alert.dao.omniservice.impl;


import java.util.ArrayList;
import java.util.List;

import com.path.alert.dao.omniservice.PushNotificationDAO;
import com.path.alert.vo.omniservice.PushNotificationCO;
import com.path.alert.vo.omniservice.PushNotificationSC;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;


public class PushNotificationDAOImpl extends BaseDAO implements PushNotificationDAO
{

	@Override
	public List<PushNotificationCO> returnPushNotifDeviceTokenList(PushNotificationSC sc) throws DAOException {
	    List<PushNotificationCO> list = new ArrayList<PushNotificationCO>();
	    list = getSqlMap().queryForList("pushNotificationMapper.returnPushNotifDeviceTokenList", sc);

	    return list;
	}

	@Override
	public List<PushNotificationCO> returnPushNotificationList(PushNotificationSC sc) throws DAOException {
	    List<PushNotificationCO> list = new ArrayList<PushNotificationCO>();
	    list = getSqlMap().queryForList("pushNotificationMapper.returnPushNotificationList", sc);

	    return list;
	}
	
}
