package com.path.alert.bo.smsservice;
/**
 * Automatically Generated Interface Code
 * interface 
 */

import java.util.HashMap;

import com.path.alert.vo.smsservice.SendSMSResp;
import com.path.lib.common.exception.BaseException;



public interface SMSServiceWsBO{
/**
 * @description  Automatically Generated SendSMS service
 * @param SendSMSReq
 * @return
*/

public HashMap<String,Object> sendSMS( HashMap<String,Object> msgHm) throws Exception;

/**
 * Update Processed = 0 while sending sms failed
 * @param sendSMSResp
 * @return
 * @throws BaseException
 */
public Integer updateProcessedCodeForSms(SendSMSResp sendSMSResp) throws BaseException;

}
