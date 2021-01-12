package com.path.alert.dao.fixed;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.alert.vo.fixed.ALRT_FIXED_GEN_EXPCO;
import com.path.alert.vo.fixed.ALRT_QUEUE_DETAILSCO;
import com.path.alert.vo.fixed.ALRT_QUEUE_ONLINECO;
import com.path.alert.vo.fixed.AlertReceiverCO;
import com.path.alert.vo.fixed.FixedGenExpParamCO;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.lib.common.exception.DAOException;
import com.path.vo.alert.notification.Account;

/**
 * @author MohamadHojeij
 *
 */
public interface FixedEventDAO {
    
    /**
     * @throws DAOException
     */
    public ALRT_QUEUE_ONLINECO updateAlrtQueueStatus(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException;
   
    /**
     * @return
     * @throws DAOException
     */
    
    public AlertReceiverCO getReceiverRow(Account account,BigDecimal queueID,String tableName) throws DAOException;
    
    public BigDecimal getFixedEvtId(BigDecimal evtID) throws DAOException;
    
    public ArrayList<ALRT_QUEUE_ONLINECO> selectFromQueueOnline(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException;
    

    public ArrayList<BigDecimal> returnAllEvtByFixed(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException;

    public ArrayList<ALRT_FIXED_GEN_EXPCO> getGenExpression(BigDecimal evtId) throws DAOException;
    
    public int validateGenExp(FixedGenExpParamCO fixedGenExpParamCO) throws DAOException;
    
    public ArrayList<String> getColumnsName(BigDecimal fixed_evt_id) throws DAOException;
    
    public ArrayList<String> returnColumnTags(FixedTagCO fixedTagCO) throws DAOException;
    
    public String selectExpression(BigDecimal evt_id) throws DAOException;
    
    public ArrayList<ALRT_QUEUE_DETAILSCO> returnReceiverData(AlertReceiverCO receiverCO) throws DAOException;
    
    public ArrayList<AlertReceiverCO> getReceiverDetails(AlertReceiverCO receiverCO) throws DAOException;

    public FixedTagCO selectFixedTags(FixedTagCO co) throws DAOException;

}
