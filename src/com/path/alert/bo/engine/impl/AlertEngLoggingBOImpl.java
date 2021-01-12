package com.path.alert.bo.engine.impl;

import java.util.List;

import com.path.alert.bo.engine.AlertEngLoggingBO;
import com.path.alert.dao.engine.AlertEngLoggingDAO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.alert.vo.engine.AlertMessageCO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_COM_MODEVO;
import com.path.dbmaps.vo.ALRT_ENG_MSG_DETAILVO;
import com.path.dbmaps.vo.ALRT_ENG_REQUESTVO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;

public class AlertEngLoggingBOImpl extends BaseBO implements AlertEngLoggingBO {
	
	/**
	 * hold reference to the Engine DAO
	 */
	private AlertEngLoggingDAO alertEngLoggingDAO;
	
	@Override
	public void deleteReceiverData(AlertNtfCO messageCO) throws BaseException {
		alertEngLoggingDAO.deleteReceiverData(messageCO);
	}
	
	@Override
	public Integer logEngRequest(ALRT_ENG_REQUESTVO engRequest) throws BaseException {
		return alertEngLoggingDAO.logEngRequest(engRequest);
	}
	


	@SuppressWarnings("rawtypes")
	@Override
	public boolean logEngMessages(List<AlertNtfCO> taskList, String receiverType) throws BaseException{
		return alertEngLoggingDAO.logEngMessages(taskList,receiverType);
	}

	@Override
	public boolean logMsgDetail(AlertMessageCO message, List<ALRT_ENG_MSG_DETAILVO> messageInformation)
			throws BaseException {
		return alertEngLoggingDAO.logMsgDetail(message,messageInformation);
	}
	

	@Override
	public Integer updateRequestStatus(ALRT_ENG_REQUESTVO requestVO) throws BaseException {
		return alertEngLoggingDAO.updateRequestStatus(requestVO);
	}
	
	
	@Override
	public Integer updateMsgStatus(ALRT_ENG_MSGVO msgVO) throws BaseException {
		return alertEngLoggingDAO.updateMsgStatus(msgVO);
	}

	@Override
	public Integer logMsgByCommMode(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws BaseException {
		// set starting time
		// @note we may remove the generic dao
		msgComMode.setSTARTING_TIME(commonLibBO.returnSystemDateWithTime());
		return genericDAO.insert(msgComMode);
	}
	
	@Override
	public boolean updateMsgByCommModeStatus(ALRT_ENG_MSG_COM_MODEVO msgComMode) throws BaseException {
		return alertEngLoggingDAO.updateMsgByCommModeStatus(msgComMode);
	}
	
	/**
	 * @return the alertEngLoggingDAO
	 */
	public AlertEngLoggingDAO getAlertEngLoggingDAO() {
		return alertEngLoggingDAO;
	}


	/**
	 * @param alertEngLoggingDAO the alertEngLoggingDAO to set
	 */
	public void setAlertEngLoggingDAO(AlertEngLoggingDAO alertEngLoggingDAO) {
		this.alertEngLoggingDAO = alertEngLoggingDAO;
	}
}
