package com.path.alert.dao.smsservice.impl;
import com.path.alert.dao.smsservice.SMSServiceWsDAO;
import com.path.alert.vo.smsservice.SendSMSResp;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;
import com.path.lib.common.util.StringUtil;

public class SMSServiceWsDAOImpl extends BaseDAO implements SMSServiceWsDAO {

	/**
	* @description Automatically generated method sendSMS service implementation
	* @param SendSMSReq
	* @return SendSMSResp
   	 */
	@Override
	public SendSMSResp sendSMS(SendSMSResp sendSMSResp) throws Exception
	{
		getSqlMap().update("SMSServiceWsMapper.sendSMS", sendSMSResp);
		sendSMSResp.getServiceResponse().setStatusDesc(StringUtil.replaceProcedureMessage(sendSMSResp.getServiceResponse().getStatusDesc()));
		return sendSMSResp;
	}
	
	@Override
	public Integer updateProcessedCodeForSms(SendSMSResp sendSMSResp) throws DAOException
	{
	    return getSqlMap().update("SMSServiceWsMapper.updateProcessedCodeForSms", sendSMSResp);
	}
	
	
	
   }
