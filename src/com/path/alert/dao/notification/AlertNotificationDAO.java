package com.path.alert.dao.notification;

import java.math.BigDecimal;
import java.util.ArrayList;

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
import com.path.lib.common.exception.DAOException;

public interface AlertNotificationDAO
{
    
    public SubscriberCO returnSubscriberId(SubscriberCO subscriberCO) throws DAOException;
    
    public ArrayList<AlertEvtCO> returnEventDetails(AlertEvtCO alertEvt) throws DAOException;
    
    public ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> returnAlrtArgsQuery(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtEvtArgs) throws DAOException;
    public ALRT_EVT_COMM_MODE_ARG_MAPVO returnAlrtArgsReport(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtEvtArgs) throws DAOException;
    
    public SubscriberCO selectSubscriberId(QueryCO queryCO) throws DAOException;
    public BigDecimal selectSubscriberIdByCIF(BigDecimal cifNo) throws DAOException;
    public BigDecimal selectSubscriberIdByChannelUserID(String channelUserID) throws DAOException;
    public SubscriberCO returnSubscriberDetails(SubscriberCO subscriberCO) throws DAOException;
    public AlertPredefinedTagsCO returnPredefinedTags(String tagName) throws DAOException;
    public SubscriberCO returnSubscriberIdByAccount(AccountQueue accountQ) throws DAOException;
    public String selectEventNameByID(BigDecimal evt_id) throws DAOException;
    public EvtCO selectEvtDetails(EvtCO batchEvtCO) throws DAOException;
    public BigDecimal selectBatchId(String eventName) throws DAOException;

    public ArrayList<ALRT_EVT_BATCH_ARG_MAPCO> returnAlrtArgsQueryEvt(ALRT_EVT_BATCH_ARG_MAPCO alrtArgs) throws DAOException;
    
    public String selectEmailFromSCR(EmailSCRDynQryCO emailSCRDynQryCO) throws DAOException;
    
    public ArrayList<BigDecimal> selectAttachement(ALRT_EVT_REPORT_ATTACHCO alrt_EVT_REPORT_ATTACHCO) throws DAOException;

    public String returnSkipSubs(BigDecimal eventID) throws DAOException;

    public SubscriptionCO selectSubscription(AccountQueue accountQueue) throws DAOException;

    public BigDecimal selectEventIDByName(AlertNotificationCO alertNotificationCO) throws DAOException;

    /**
     * return All subscribers available in Group
     * @param notificationCO
     * @return
     * @throws DAOException
     */
    public ArrayList<BigDecimal> returnSubscribersInGroup(AlertNotificationCO notificationCO) throws DAOException;

    /**
     * check if any group contains Yes 
     * to include all subscribers
     * @param alertNotificationCO
     * @return
     * @throws DAOException
     */
    public int countIfIncludeAllSubInGroup(AlertNotificationCO alertNotificationCO) throws DAOException;

    /**
     * teturn internal alert by  event id
     * @param eventId
     * @return
     * @throws DAOException
     */
    public ALRT_EVT_COMM_INTRNLVO returnInternalEventDetailsByEvtId(BigDecimal eventId)
	    throws DAOException;
    
    

}
