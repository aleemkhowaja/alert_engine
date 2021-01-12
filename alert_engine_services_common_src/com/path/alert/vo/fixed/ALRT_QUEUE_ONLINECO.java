package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class ALRT_QUEUE_ONLINECO  extends BaseVO{
    
    private BigDecimal ID;
    private BigDecimal fixedEvtID;
    private BigDecimal evtId;
    private String status;
    private Long sessionID;
    private String bulkYN;
    private String machineIpName;
    private String langCode;
    
    public BigDecimal getID() {
        return ID;
    }
    public void setID(BigDecimal iD) {
        ID = iD;
    }
    public BigDecimal getFixedEvtID() {
        return fixedEvtID;
    }
    public void setFixedEvtID(BigDecimal fixedEvtID) {
        this.fixedEvtID = fixedEvtID;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the evtId
     */
    public BigDecimal getEvtId() {
	return evtId;
    }
    /**
     * @param evtId the evtId to set
     */
    public void setEvtId(BigDecimal evtId) {
	this.evtId = evtId;
    }
    /**
     * @return the sessionID
     */
    public long getSessionID() {
	return sessionID;
    }
    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(long sessionID) {
	this.sessionID = sessionID;
    }
    /**
     * @return the bulkYN
     */
    public String getBulkYN() {
	return bulkYN;
    }
    /**
     * @param bulkYN the bulkYN to set
     */
    public void setBulkYN(String bulkYN) {
	this.bulkYN = bulkYN;
    }

    /**
     * @return the machineIpName
     */
    public String getMachineIpName()
    {
	return machineIpName;
    }

    /**
     * @param machineIpName the machineIpName to set
     */
    public void setMachineIpName(String machineIpName)
    {
	this.machineIpName = machineIpName;
    }
    /**
     * @return the langCode
     */
    public String getLangCode()
    {
        return langCode;
    }
    /**
     * @param langCode the langCode to set
     */
    public void setLangCode(String langCode)
    {
        this.langCode = langCode;
    }
}