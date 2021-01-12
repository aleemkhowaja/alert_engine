package com.path.alert.dao.engine;

import java.util.List;

import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_COM_MODEVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_DETAILVO;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.exception.DAOException;


public interface AlertEngLoggingDAO
{
	/**
	 * Delete Receiver data
	 * @param messageCO
	 * @throws DAOException
	 */
	void deleteReceiverData(AlertNtfCO messageCO) throws DAOException;
	
	/**
	 * Log Eng Request
	 * @param MessageProducerTask task
	 * @return
	 */
	Integer logEngRequest(ALRT_ENG_REQUESTVO engRequest) throws DAOException;

	
	/**
	 * Log Message
	 * @param taskList
	 * @param receiverType
	 * @return
	 * @throws DAOException
	 */
	boolean logEngMessages(List<AlertNtfCO> taskList, String receiverType) throws DAOException;

	
	/**
	 * Log Message details
	 * @param message
	 * @param messageInformation
	 * @return
	 * @throws DAOException 
	 */
	boolean logMsgDetail(AlertMessageCO message, List<ALRT_ENG_MSG_DETAILVO> messageInformation) throws DAOException;

	/**
	 * Update Request Status
	 * @param requestVO
	 * @return
	 * @throws DAOException
	 */
	Integer updateRequestStatus(ALRT_ENG_REQUESTVO requestVO) throws DAOException;

	/**
	 * Update Msg status, starting/ending time base on status
	 * @param msgVO
	 * @return
	 * @throws DAOException
	 */
	Integer updateMsgStatus(ALRT_ENG_MSGVO msgVO) throws DAOException;
	
	
	/**
	 * Update Msg status by communication mode
	 * @param msgComMode
	 * @return
	 * @throws DAOException
	 */
	boolean updateMsgByCommModeStatus(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws DAOException;
}
