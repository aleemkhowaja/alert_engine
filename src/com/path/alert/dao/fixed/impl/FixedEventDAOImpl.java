package com.path.alert.dao.fixed.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.alert.dao.fixed.FixedEventDAO;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.vo.fixed.ALRT_FIXED_GEN_EXPCO;
import com.path.alert.vo.fixed.ALRT_QUEUE_DETAILSCO;
import com.path.alert.vo.fixed.ALRT_QUEUE_ONLINECO;
import com.path.alert.vo.fixed.AccountQueue;
import com.path.alert.vo.fixed.AlertReceiverCO;
import com.path.alert.vo.fixed.FixedGenExpParamCO;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;
import com.path.vo.alert.notification.Account;

/**
 * @author MohamadHojeij
 *
 */
public class FixedEventDAOImpl extends BaseDAO implements FixedEventDAO  {

    /* (non-Javadoc)
     * @see com.path.alert.dao.fixed.FixedEventDAO#updateAlrtQueueStatus()
     */
    @Override
    public ALRT_QUEUE_ONLINECO updateAlrtQueueStatus(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException {
	
	/**
	 * Machine name can be used to lock the rows but session lock
	 * was added before machine name , for that we will keep for more 
	 * racing condition check.
	 */
    	alrtQueue.setSessionID(System.currentTimeMillis());
    	getSqlMap().queryForObject("fixedEventMapper.updateAlrtQueueStatus",alrtQueue);
    	return alrtQueue;
    }

    /* (non-Javadoc)
     * @see com.path.alert.dao.fixed.FixedEventDAO#selectFromQueueOnline()
     */
    @Override
    public ArrayList<ALRT_QUEUE_ONLINECO> selectFromQueueOnline(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException {
	// TODO Auto-generated method stub
//    	System.out.println("selectFromQueueOnline" + alrtQueue.getSessionID());
    	
    	ArrayList<ALRT_QUEUE_ONLINECO> result = (ArrayList<ALRT_QUEUE_ONLINECO>) getSqlMap().queryForList("fixedEventMapper.selectFromQueueOnline",
    			alrtQueue);
    	
//    	System.out.println("selectFromQueueOnline Result :" + alrtQueue.getSessionID() + " => " + result);
    	return result;
    }

    

    @Override
    public ArrayList<BigDecimal> returnAllEvtByFixed(ALRT_QUEUE_ONLINECO alrtQueue) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<BigDecimal>) getSqlMap().queryForList("fixedEventMapper.returnAllEvtByFixed",
		alrtQueue);
    }

    @Override
    public ArrayList<ALRT_FIXED_GEN_EXPCO> getGenExpression(BigDecimal evtId) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<ALRT_FIXED_GEN_EXPCO>) getSqlMap().queryForList("fixedEventMapper.getGenExpression",
		evtId);
    }

    @Override
    public int validateGenExp(FixedGenExpParamCO fixedGenExpParamCO) throws DAOException {
	// TODO Auto-generated method stub
	return (Integer) getSqlMap().queryForObject("fixedEventMapper.validateGenExp",
		fixedGenExpParamCO);
    }

    @Override
    public ArrayList<String> getColumnsName(BigDecimal fixed_evt_id) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<String>) getSqlMap().queryForList("fixedEventMapper.getColumnsName",
		fixed_evt_id);
    }

    @Override
    public ArrayList<String> returnColumnTags(FixedTagCO fixedTagCO) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<String>) getSqlMap().queryForList("fixedEventMapper.returnColumnTags",
		fixedTagCO);
    }

    @Override
    public String selectExpression(BigDecimal evt_id) throws DAOException {
	// TODO Auto-generated method stub
	return (String) getSqlMap().queryForObject("fixedEventMapper.selectExpression", evt_id);
    }


    @Override
    public ArrayList<AlertReceiverCO> getReceiverDetails(AlertReceiverCO receiverCO) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<AlertReceiverCO>) getSqlMap().queryForList("fixedEventMapper.getReceiverDetails",
		receiverCO);
    }

    @Override
    public BigDecimal getFixedEvtId(BigDecimal evtID) throws DAOException {
	// TODO Auto-generated method stub
	return (BigDecimal) getSqlMap().queryForObject("fixedEventMapper.getFixedEvtId", evtID);
    }

    @Override
    public AlertReceiverCO getReceiverRow(Account account,BigDecimal queueID,String tableName) throws DAOException {
	// TODO Auto-generated method stub
	AccountQueue accountQueue = new AccountQueue();
	accountQueue.setAccount(account);
	accountQueue.setQueueID(queueID);
	accountQueue.setTableName(tableName);
	
	/**
	 * set the connection co
	 */
	accountQueue.setConnCO(AlertEngine.getInstance().getConnCO());
	
	return (AlertReceiverCO) getSqlMap().queryForObject("fixedEventMapper.getReceiverRow", accountQueue);
    }

    @Override
    public ArrayList<ALRT_QUEUE_DETAILSCO> returnReceiverData(AlertReceiverCO receiverCO) throws DAOException {
	// TODO Auto-generated method stub
	return (ArrayList<ALRT_QUEUE_DETAILSCO>) getSqlMap().queryForList("fixedEventMapper.returnReceiverData", receiverCO);
    }

    @Override
    public FixedTagCO selectFixedTags(FixedTagCO co) throws DAOException {
	// TODO Auto-generated method stub
	return (FixedTagCO) getSqlMap().queryForObject("fixedEventMapper.selectFixedTags",co);
    }

}
