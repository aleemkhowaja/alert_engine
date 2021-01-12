package com.path.alert.vo.notification;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class EvtCO extends BaseVO {
    
    private String evtType;
    private String descEng;
    private String descArab;
    private BigDecimal batchId;
    private String evtBatchType;
    private String batchTypeQryCol;
    private BigDecimal compCode;
    private BigDecimal eventID;
    private BigDecimal fixedEventID;
    private String status;
    private String eventExp;
    private String sourceOfContact;
    private String skipSubscription;
    
    public BigDecimal getFixedEventID() {
        return fixedEventID;
    }
    public void setFixedEventID(BigDecimal fixedEventID) {
        this.fixedEventID = fixedEventID;
    }
    
    /**
     * @return the descEng
     */
    public String getDescEng()
    {
        return descEng;
    }
    /**
     * @return the descArab
     */
    public String getDescArab()
    {
        return descArab;
    }
    /**
     * @param descEng the descEng to set
     */
    public void setDescEng(String descEng)
    {
        this.descEng = descEng;
    }
    /**
     * @param descArab the descArab to set
     */
    public void setDescArab(String descArab)
    {
        this.descArab = descArab;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getEventExp() {
        return eventExp;
    }
    public void setEventExp(String eventExp) {
        this.eventExp = eventExp;
    }
    public String getEvtType() {
        return evtType;
    }
    public void setEvtType(String evtType) {
        this.evtType = evtType;
    }
    public BigDecimal getBatchId() {
        return batchId;
    }
    public void setBatchId(BigDecimal batchId) {
        this.batchId = batchId;
    }
    public String getEvtBatchType() {
	return evtBatchType;
    }
    public void setEvtBatchType(String evtBatchType) {
	this.evtBatchType = evtBatchType;
    }
    public String getBatchTypeQryCol() {
	return batchTypeQryCol;
    }
    public void setBatchTypeQryCol(String batchTypeQryCol) {
	this.batchTypeQryCol = batchTypeQryCol;
    }
    public BigDecimal getCompCode() {
	return compCode;
    }
    public void setCompCode(BigDecimal compCode) {
	this.compCode = compCode;
    }
    /**
     * @return the eventID
     */
    public BigDecimal getEventID() {
	return eventID;
    }
    /**
     * @param eventID the eventID to set
     */
    public void setEventID(BigDecimal eventID) {
	this.eventID = eventID;
    }
    /**
     * @return the sourceOfContact
     */
    public String getSourceOfContact() {
	return sourceOfContact;
    }
    /**
     * @param sourceOfContact the sourceOfContact to set
     */
    public void setSourceOfContact(String sourceOfContact) {
	this.sourceOfContact = sourceOfContact;
    }
    /**
     * @return the skipSubscription
     */
    public String getSkipSubscription() {
	return skipSubscription;
    }
    /**
     * @param skipSubscription the skipSubscription to set
     */
    public void setSkipSubscription(String skipSubscription) {
	this.skipSubscription = skipSubscription;
    }
    

}
