package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;
import com.path.vo.alert.notification.Account;

public class AlertReceiverCO extends BaseVO {
    
    private BigDecimal id;
    private BigDecimal queueID;
    private BigDecimal compCode;
    private Account account;
    private BigDecimal cifNO;
    private String imalUserId;
    private String endChannelUser;
    private String fbID;
    private String twID;
    private BigDecimal subscriberID;
    private String email;
    private String mobile;
  
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public BigDecimal getCifNO() {
        return cifNO;
    }
    public void setCifNO(BigDecimal cifNO) {
        this.cifNO = cifNO;
    }
   
    public String getFbID() {
        return fbID;
    }
    public void setFbID(String fbID) {
        this.fbID = fbID;
    }
    public String getTwID() {
        return twID;
    }
    public void setTwID(String twID) {
        this.twID = twID;
    }
    public String getEndChannelUser() {
        return endChannelUser;
    }
    public void setEndChannelUser(String endChannelUser) {
        this.endChannelUser = endChannelUser;
    }
    public BigDecimal getSubscriberID() {
        return subscriberID;
    }
    public void setSubscriberID(BigDecimal subscriberID) {
        this.subscriberID = subscriberID;
    }
    public String getImalUserId() {
        return imalUserId;
    }
    public void setImalUserId(String imalUserId) {
        this.imalUserId = imalUserId;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the id
     */
    public BigDecimal getId() {
	return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(BigDecimal id) {
	this.id = id;
    }
    /**
     * @return the queueID
     */
    public BigDecimal getQueueID() {
	return queueID;
    }
    /**
     * @param queueID the queueID to set
     */
    public void setQueueID(BigDecimal queueID) {
	this.queueID = queueID;
    }
    /**
     * @return the compCode
     */
    public BigDecimal getCompCode() {
	return compCode;
    }
    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(BigDecimal compCode) {
	this.compCode = compCode;
    }
    
    
    

}
