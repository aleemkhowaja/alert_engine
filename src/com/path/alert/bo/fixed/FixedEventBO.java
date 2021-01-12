package com.path.alert.bo.fixed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.lib.common.exception.BaseException;

public interface FixedEventBO {
        
    public HashMap<String, Object> getHash(BigDecimal evtID, BigDecimal receiverID) throws BaseException;
    
    public ArrayList<AlertNotificationCO> selectFromQueueOnline(String bulkYN) throws BaseException;
                
    public String selectExpression(BigDecimal evt_id) throws BaseException;
    
    public FixedTagCO selectFixedTags(BigDecimal fixedEventId, String tagName) throws BaseException;
    

}
