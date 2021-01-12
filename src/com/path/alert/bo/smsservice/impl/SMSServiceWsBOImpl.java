package com.path.alert.bo.smsservice.impl;

import java.util.HashMap;

import com.path.alert.bo.smsservice.SMSServiceWsBO;
import com.path.alert.dao.smsservice.SMSServiceWsDAO;
import com.path.alert.vo.smsservice.SendSMSReq;
import com.path.alert.vo.smsservice.SendSMSResp;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.PathPropertyUtil;

public class SMSServiceWsBOImpl extends BaseBO implements SMSServiceWsBO
{
    private SMSServiceWsDAO sMSServiceWsDAO;

    /**
     * @description Automatically generated method sendSMS service
     *              implementation
     * @param SendSMSReq
     * @return SendSMSResp
     */
    @Override
    public HashMap<String, Object> sendSMS(HashMap<String, Object> msgHm) throws Exception
    {
	SendSMSReq sendSMSReq = new SendSMSReq();
	PathPropertyUtil.copyProperties(msgHm, sendSMSReq);
	SendSMSResp sendSMSResp = new SendSMSResp();
	PathPropertyUtil.copyProperties(sendSMSReq, sendSMSResp);
	try
	{
	    sendSMSResp = sMSServiceWsDAO.sendSMS(sendSMSResp);
	}
	catch(Exception e)
	{
	    throw new BOException(e);
	}
	return PathPropertyUtil.convertToMap(sendSMSResp);
    }

    @Override
    public Integer updateProcessedCodeForSms(SendSMSResp sendSMSResp) throws BaseException
    {
	return sMSServiceWsDAO.updateProcessedCodeForSms(sendSMSResp);
    }
    public void setSMSServiceWsDAO(SMSServiceWsDAO sMSServiceWsDAO)
    {
	this.sMSServiceWsDAO = sMSServiceWsDAO;
    }

    public SMSServiceWsDAO getSMSServiceWsDAO()
    {
	return sMSServiceWsDAO;
    }
}
