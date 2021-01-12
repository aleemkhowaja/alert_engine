package com.path.alert.dao.smsservice;
/**
 * Automatically Generated Interface Code
 * interface 
 */

import com.path.alert.vo.smsservice.SendSMSResp;
import com.path.lib.common.exception.DAOException;


public interface SMSServiceWsDAO{
	/**
	 * @description  Automatically Generated SendSMS service
	 * @param SendSMSReq
	 * @return SendSMSResp
	*/
	public SendSMSResp sendSMS(SendSMSResp sendSMSResp) throws Exception;

	/**
	 * 
	 * @param sendSMSResp
	 * @return
	 * @throws DAOException
	 */
	public Integer updateProcessedCodeForSms(SendSMSResp sendSMSResp) throws DAOException;
	
	
	
}