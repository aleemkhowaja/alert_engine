package com.path.alert.dao.notification.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.dao.notification.AlertNotificationDAO;
import com.path.alert.vo.alerts.ALRT_EVT_COMM_INTRNLVO;
import com.path.alert.vo.fixed.AccountQueue;
import com.path.alert.vo.notification.ALRT_EVT_BATCH_ARG_MAPCO;
import com.path.alert.vo.notification.ALRT_EVT_COMM_MODE_ARG_MAPVO;
import com.path.alert.vo.notification.ALRT_EVT_REPORT_ATTACHCO;
import com.path.alert.vo.notification.AlertEvtCO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.alert.vo.notification.AlertPredefinedTagsCO;
import com.path.alert.vo.notification.EmailSCRDynQryCO;
import com.path.alert.vo.notification.EvtCO;
import com.path.alert.vo.notification.QueryCO;
import com.path.alert.vo.notification.SubscriberCO;
import com.path.alert.vo.notification.SubscriptionCO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;


public class AlertNotificationDAOImpl extends BaseDAO implements AlertNotificationDAO
{

    @Override
    public SubscriberCO returnSubscriberId(SubscriberCO subscriberCO) throws DAOException
    {
	subscriberCO.setEmails("%" + subscriberCO.getEmails() + "%");
	subscriberCO.setMobiles("%" + subscriberCO.getMobiles() + "%");
	return (SubscriberCO) getSqlMap().queryForObject("alertNotificationMapper.returnSubscriberId", subscriberCO);
    }

    @Override
    public ArrayList<AlertEvtCO> returnEventDetails(AlertEvtCO alertEvt) throws DAOException
    {
	// TODO Auto-generated method stub
	return (ArrayList<AlertEvtCO>) getSqlMap().queryForList("alertNotificationMapper.returnEventDetails", alertEvt);
    }

    @Override
    public ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> returnAlrtArgsQuery(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtEvtArgs)
	    throws DAOException
    {
	// TODO Auto-generated method stub
	return (ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO>) getSqlMap()
		.queryForList("alertNotificationMapper.returnAlrtArgsQuery", alrtEvtArgs);
    }
    
    @Override
    public ALRT_EVT_COMM_MODE_ARG_MAPVO returnAlrtArgsReport(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtEvtArgs)
	    throws DAOException
    {
	// TODO Auto-generated method stub
	return (ALRT_EVT_COMM_MODE_ARG_MAPVO) getSqlMap()
		.queryForObject("alertNotificationMapper.returnAlrtArgsReport", alrtEvtArgs);
    }

    @Override
    public SubscriberCO selectSubscriberId(QueryCO queryCO) throws DAOException
    {
	// TODO Auto-generated method stub
	return (SubscriberCO) getSqlMap().queryForObject("alertNotificationMapper.selectSubscriberId", queryCO);
	/*if(null != array && array.size() > 0)
	    return array.get(0);
	return null;*/
    } 

    @Override
    public BigDecimal selectSubscriberIdByCIF(BigDecimal cifNo) throws DAOException
    {
	// TODO Auto-generated method stub
	return (BigDecimal) getSqlMap().queryForObject("alertNotificationMapper.selectSubscriberIdByCIF", cifNo);
    }

    @Override
    public BigDecimal selectSubscriberIdByChannelUserID(String channelUserID) throws DAOException
    {
	// TODO Auto-generated method stub
	return (BigDecimal) getSqlMap().queryForObject("alertNotificationMapper.selectSubscriberIdByChannelUserID",
		channelUserID);
    }

    @Override
    public SubscriberCO returnSubscriberDetails(SubscriberCO subscriberCO) throws DAOException
    {
	// TODO Auto-generated method stub
	return (SubscriberCO) getSqlMap().queryForObject("alertNotificationMapper.returnSubscriberDetails", subscriberCO);
    }

    @Override
    public AlertPredefinedTagsCO returnPredefinedTags(String tagName) throws DAOException {
	// TODO Auto-generated method stub
	return (AlertPredefinedTagsCO) getSqlMap().queryForObject("alertNotificationMapper.returnPredefinedTags",tagName);
    }

    @Override
    public SubscriberCO returnSubscriberIdByAccount(AccountQueue accountQ) throws DAOException {
	// TODO Auto-generated method stub
	return (SubscriberCO) getSqlMap().queryForObject("alertNotificationMapper.returnSubscriberIdByAccount",
		accountQ);
	/*if(null != array && array.size() > 0)
	    return array.get(0);
	return null;*/ 
    }

    @Override
    public String selectEventNameByID(BigDecimal evt_id) throws DAOException {
	// TODO Auto-generated method stub
	return (String) getSqlMap().queryForObject("alertNotificationMapper.selectEventNameByID",evt_id);
    }

    @Override
    public EvtCO selectEvtDetails(EvtCO evtCO) throws DAOException {
	// TODO Auto-generated method stub
	return (EvtCO) getSqlMap().queryForObject("alertNotificationMapper.selectEvtDetails",evtCO);
    }

    @Override
    public BigDecimal selectBatchId(String eventName) throws DAOException {
	// TODO Auto-generated method stub
	return (BigDecimal) getSqlMap().queryForObject("alertNotificationMapper.selectBatchId",
		eventName);
    }

    @Override
    public ArrayList<ALRT_EVT_BATCH_ARG_MAPCO> returnAlrtArgsQueryEvt(ALRT_EVT_BATCH_ARG_MAPCO alrtArgs) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<ALRT_EVT_BATCH_ARG_MAPCO>) getSqlMap()
		.queryForList("alertNotificationMapper.returnAlrtArgsQueryEvt", alrtArgs);
    }

    @Override
    public String selectEmailFromSCR(EmailSCRDynQryCO emailSCRDynQryCO) throws DAOException {
	// TODO Auto-generated method stub
	if(emailSCRDynQryCO.getTableName().equals(AlertNotificationConstant.AMF_ADDRESS)) {
	    return (String) getSqlMap().queryForObject("alertNotificationMapper.selectEmailFromSCRByAcc",
			emailSCRDynQryCO);
	}
	else if(emailSCRDynQryCO.getTableName().equals(AlertNotificationConstant.CIF_ADDRESS)) {
	    return (String) getSqlMap().queryForObject("alertNotificationMapper.selectEmailMobileByCif",
			emailSCRDynQryCO);
	}
	return (String) getSqlMap().queryForObject("alertNotificationMapper.selectEmailFromSCR",
		emailSCRDynQryCO);
    }

    @Override
    public ArrayList<BigDecimal> selectAttachement(ALRT_EVT_REPORT_ATTACHCO alrt_EVT_REPORT_ATTACHCO)
	    throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<BigDecimal>) getSqlMap().
		queryForList("alertNotificationMapper.selectAttachement", alrt_EVT_REPORT_ATTACHCO);
    }

    @Override
    public String returnSkipSubs(BigDecimal eventID) throws DAOException {
	// TODO Auto-generated method stub
	return (String) getSqlMap().queryForObject("alertNotificationMapper.returnSkipSubs",
		eventID);
    }

    @Override
    public SubscriptionCO selectSubscription(AccountQueue accountQueue)
	    throws DAOException {
	// TODO Auto-generated method stub
	return (SubscriptionCO) getSqlMap().queryForObject("alertNotificationMapper.selectSubscription",
		accountQueue);
    }

    @Override
    public BigDecimal selectEventIDByName(AlertNotificationCO alertNotificationCO) throws DAOException {
	// TODO Auto-generated method stub
	return (BigDecimal) getSqlMap().queryForObject("alertNotificationMapper.selectEventIDByName",alertNotificationCO);
    }
    
    
    /**
     * return All subscribers available in Group
     * @param groupId
     * @return
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<BigDecimal> returnSubscribersInGroup(AlertNotificationCO notificationCO)
	    throws DAOException {
	return (ArrayList<BigDecimal>) getSqlMap().
		queryForList("alertNotificationMapper.returnSubscribersInGroup", notificationCO);
    }
    
    /**
     * check if any group contains Yes 
     * to include all subscribers
     * @param alertNotificationCO
     * @return
     * @throws DAOException
     */
    @Override
    public int countIfIncludeAllSubInGroup(AlertNotificationCO alertNotificationCO) throws DAOException {
	// TODO Auto-generated method stub
	return (int) getSqlMap().queryForObject("alertNotificationMapper.countIfIncludeAllSubInGroup",alertNotificationCO);
    }
    
    @Override
    public ALRT_EVT_COMM_INTRNLVO returnInternalEventDetailsByEvtId(BigDecimal eventId) throws DAOException
    {
	return (ALRT_EVT_COMM_INTRNLVO) getSqlMap().queryForObject("alertNotificationMapper.internalEventDetailsByEvtId", eventId);
    }

}
