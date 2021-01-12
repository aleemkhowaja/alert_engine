package com.path.alert.vo.engine;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.lib.vo.BaseVO;

/**
 * This class represent the final prepare Message to be sent
 * @author MohammadAliMezzawi
 *
 */
public class AlertMessageCO extends BaseVO
{
    private String messageBody;
    private String messageSubject;
    private String communicationType;
    private String[] attachementName;
    private ArrayList<byte[]> attachements;
    private String emails;
    private String mobiles;
    
    @SuppressWarnings("rawtypes")
    private AlertNtfCO alertNtfCO;
	
    private BigDecimal errorCode;
    private String errorDesc;
	
	
    
    
    public BigDecimal getErrorCode() {
	    return errorCode;
	}
	public void setErrorCode(BigDecimal errorCode) {
	    this.errorCode = errorCode;
	}
	public String getErrorDesc() {
	    return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
	    this.errorDesc = errorDesc;
	}
    public String[] getAttachementName()
    {
        return attachementName;
    }
    public void setAttachementName(String[] attachementName)
    {
        this.attachementName = attachementName;
    }
    public String getEmails()
    {
        return emails;
    }
    public void setEmails(String emails)
    {
        this.emails = emails;
    }
    public String getMobiles()
    {
        return mobiles;
    }
    public void setMobiles(String mobiles)
    {
        this.mobiles = mobiles;
    }
    public String getMessageBody()
    {
        return messageBody;
    }
    public void setMessageBody(String messageBody)
    {
        this.messageBody = messageBody;
    }
    public String getMessageSubject()
    {
        return messageSubject;
    }
    public void setMessageSubject(String messageSubject)
    {
        this.messageSubject = messageSubject;
    }
    public String getCommunicationType()
    {
        return communicationType;
    }
    public void setCommunicationType(String communicationType)
    {
        this.communicationType = communicationType;
    }
    public ArrayList<byte[]> getAttachements()
    {
        return attachements;
    }
    public void setAttachements(ArrayList<byte[]> attachements)
    {
        this.attachements = attachements;
    }
	
    @SuppressWarnings("rawtypes")
    public void setNtfCO(AlertNtfCO alertNtfCO) {
	this.alertNtfCO = alertNtfCO;
    }
    
    @SuppressWarnings("rawtypes")
    public AlertNtfCO getNtfCO() {
	return this.alertNtfCO;
    }
}
