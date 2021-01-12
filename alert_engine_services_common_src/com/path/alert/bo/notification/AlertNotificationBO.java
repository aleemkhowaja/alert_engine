package com.path.alert.bo.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.path.alert.vo.engine.AlertMessageListCO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.notification.ALRT_EVT_COMM_MODE_ARG_MAPVO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.alert.vo.notification.AlertPredefinedTagsCO;
import com.path.alert.vo.notification.EvtCO;
import com.path.lib.common.exception.BaseException;

/**
 * @author MohamadHojeij
 *
 */
public interface AlertNotificationBO
{
    /**
     * @param alertNotificationCO
     * @return
     * @throws BaseException
     */
    AlertNtfCO getSubscriberDetails(AlertNtfCO alertNtfCO) throws BaseException;
    
    /**
     * @param alertMessageTaskCO
     * @return
     * @throws BaseException
     */
    AlertMessageListCO prepareMessage(AlertNtfCO alertNtfCO) throws BaseException;
    
    AlertNotificationCO checkBulkEvent(AlertNotificationCO alertNotificationCO) throws BaseException;
    
    ArrayList<LinkedHashMap> retrieveQueryRes(String queryID,HashMap<String, String> hmQryParam , int nbRec) throws Exception;

    ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> returnAlrtArgsQuery(ALRT_EVT_COMM_MODE_ARG_MAPVO alrtArgs) throws Exception;

    AlertPredefinedTagsCO returnPredefinedTags(String tagName) throws Exception;
    
    EvtCO selectEvtDetails(AlertNotificationCO alertNotificationCO) throws BaseException;
    
    AlertNotificationCO returnListOfBatchSubscriberById(AlertNotificationCO alertNotificationCO ,EvtCO evtCO) throws BaseException;

    public AlertNotificationCO returnSubscribersInGroup(AlertNotificationCO notificationCO) throws BaseException;

    public int countIfIncludeAllSubInGroup(AlertNotificationCO alertNotificationCO) throws BaseException;
}
