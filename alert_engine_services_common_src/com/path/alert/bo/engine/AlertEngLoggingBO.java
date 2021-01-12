package com.path.alert.bo.engine;

import java.util.List;

import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_COM_MODEVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_DETAILVO;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.exception.BaseException;

/**
 * This will hold the engine core
 * @author Mohammad Ali Mezzawi
 * 
 * @Note
 * This interface should only contain
 * 1 - Start
 * 2 - Stop
 * 3 - Send Message 
 * Any object used here should be in Alert common area
 * since this bo is linked to alert services
 */
public interface AlertEngLoggingBO {

	/**
	 * @param messageCO
	 * @throws BaseException
	 */
	public void deleteReceiverData(AlertNtfCO messageCO) throws BaseException;
	
	/**
	 * log Message Producer task
	 * @param task
	 * @return
	 * @throws BaseException 
	 */
	public Integer logEngRequest(ALRT_ENG_REQUESTVO engRequest) throws BaseException;

	/**
	 * @param taskList
	 * @param receiverType
	 * @return
	 * @throws BaseException
	 */
	@SuppressWarnings("rawtypes")
	public boolean logEngMessages(List<AlertNtfCO> taskList, String receiverType) throws BaseException;
	

	/**
	 * @param message
	 * @param messageInformation
	 * @throws BaseException
	 */
	public boolean logMsgDetail(AlertMessageCO message, List<ALRT_ENG_MSG_DETAILVO> messageInformation) throws BaseException;
	
	/**
	 * @param msgComMode
	 * @return 
	 * @throws BaseException
	 */
	public Integer logMsgByCommMode(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws BaseException;

	/**
	 * @param requestVO
	 * @throws BaseException
	 */
	public Integer updateRequestStatus(ALRT_ENG_REQUESTVO requestVO) throws BaseException;
	
	/**
	 * Update Message Information ( starting time , ending time ... )
	 * @param msgVO
	 * @throws BaseException
	 */
	public Integer updateMsgStatus(ALRT_ENG_MSGVO msgVO) throws BaseException;
	
	/**
	 * Update Message By Communication mode Information ( starting time , ending time ... )
	 * @param msgVO
	 * @throws BaseException
	 */
	public boolean updateMsgByCommModeStatus(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws BaseException;
}
