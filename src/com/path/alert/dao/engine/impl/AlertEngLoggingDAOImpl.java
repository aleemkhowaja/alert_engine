package com.path.alert.dao.engine.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.path.alert.dao.engine.AlertEngLoggingDAO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.bo.common.ConstantsCommon;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_COM_MODEVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_DETAILVO;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;
import com.path.lib.log.PathFormatter;


/**
 * This class represent the DAO for Alert Engine
 * @author MohammadAliMezzawi
 *
 */
public class AlertEngLoggingDAOImpl extends BaseDAO implements AlertEngLoggingDAO
{
	@Override
	public void deleteReceiverData(AlertNtfCO messageCO) throws DAOException {
		getSqlMap().delete("alertEngLoggingMapper.deleteReceiverData", messageCO);
	}
	
	@Override
	public Integer logEngRequest(ALRT_ENG_REQUESTVO engRequestVO) throws DAOException {
		return (Integer) getSqlMap().insert("alertEngLoggingMapper.logEngRequest", engRequestVO);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean logEngMessages(List<AlertNtfCO> ntfList, String receiverType) throws DAOException {
		
		SqlSession sqlSess = null;
		boolean result = true;
		try {

			// long start = Calendar.getInstance().getTimeInMillis();
			sqlSess = getSqlMap().returnBatchSession(null);
			
			/**
			 * flush perform cleaning of all Batches
			 * included into sqlSession if any
			 */
			sqlSess.flushStatements();
			
			//System.out.println("Element Size" + ntfList.size());
			
			for(AlertNtfCO ntf : ntfList){
				ALRT_ENG_MSGVO msgVO = ntf.getMsgVO();
				msgVO.setIsOracle(ConstantsCommon.CURR_DBMS_ORACLE);
				msgVO.setIsSybase(ConstantsCommon.CURR_DBMS_SYBASE);
				msgVO.setIsSQLServer(ConstantsCommon.CURR_DBMS_SQLSERVER);
				sqlSess.update("ALRT_ENG_MSG.insertALRT_ENG_MSG", msgVO);
			}
			
			sqlSess.commit();

			//long end = Calendar.getInstance().getTimeInMillis();
			//System.out.println("logEngMessages : Time taken in MS = " + (end - start));

		} catch (Exception e) {
			log.error(PathFormatter.toString(e,false));
			sqlSess.rollback();
			throw new DAOException("Error Occured", e);
		} finally {
			if (sqlSess != null) {
				sqlSess.close();
			}
		}
		
		return result;
	}
	
	
	@Override
	public boolean logMsgDetail(AlertMessageCO message, List<ALRT_ENG_MSG_DETAILVO> messageInformation) throws DAOException {
		
		SqlSession sqlSess = null;
		boolean result = true;
		try {

			//long start = Calendar.getInstance().getTimeInMillis();
			sqlSess = getSqlMap().returnBatchSession(null);
			
			/**
			 * flush perform cleaning of all Batches
			 * included into sqlSession if any
			 */
			sqlSess.flushStatements();
			
			//System.out.println("Element Size" + messageInformation.size());
			
			BigDecimal msgId = message.getNtfCO().getMsgVO().getMSG_ID();
			BigDecimal requestId = message.getNtfCO().getMsgVO().getREQUEST_ID();
			BigDecimal eventId = message.getNtfCO().getMsgVO().getEVENT_ID();
			
			for(ALRT_ENG_MSG_DETAILVO detailVO : messageInformation){
				detailVO.setMSG_ID(msgId);
				detailVO.setREQUEST_ID(requestId);
				detailVO.setEVENT_ID(eventId);
				detailVO.setIsOracle(ConstantsCommon.CURR_DBMS_ORACLE);
				detailVO.setIsSybase(ConstantsCommon.CURR_DBMS_SYBASE);
				detailVO.setIsSQLServer(ConstantsCommon.CURR_DBMS_SQLSERVER);
				sqlSess.update("alertEngLoggingMapper.logMsgDetail", detailVO);
			}
			
			sqlSess.commit();

			//long end = Calendar.getInstance().getTimeInMillis();
			//System.out.println("logMsgDetail : Time taken in MS = " + (end - start));

		} catch (Exception e) {
			log.error(PathFormatter.toString(e,false));
			sqlSess.rollback();
			throw new DAOException("Error Occured", e);
		} finally {
			if (sqlSess != null) {
				sqlSess.close();
			}
		}
		
		return result;
	}
	
	@Override
	public Integer updateRequestStatus(ALRT_ENG_REQUESTVO requestVO) throws DAOException {
		return getSqlMap().update("alertEngLoggingMapper.updateRequestStatus", requestVO);
	}
	
	@Override
	public Integer updateMsgStatus(ALRT_ENG_MSGVO msgVO) throws DAOException {
		return getSqlMap().update("alertEngLoggingMapper.updateMsgStatus", msgVO);
	}
	
	@Override
	public boolean updateMsgByCommModeStatus(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws DAOException {
		Integer result = (Integer)getSqlMap().update("alertEngLoggingMapper.updateMsgByCommModeStatus", msgComMode);
		return result.intValue()>0;
	}
}
