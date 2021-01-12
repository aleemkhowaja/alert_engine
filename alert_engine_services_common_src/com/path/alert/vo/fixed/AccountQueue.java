package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;
import com.path.vo.alert.notification.Account;

public class AccountQueue extends BaseVO{
    
	/**
	 * 
	 */
    private Account account;
    
    private BigDecimal subscriberID;
    
        
    /**
     * 
     */
    private BigDecimal queueID;
   
    /**
     * 
     */
    private String eventName;
    
    /**
	 * company code
	 * added by hojeij as per joseph on 12/04/2019 tp retrive by comp code
    */
	
    private BigDecimal compCode;
	
	
    /**
	 
	 * event id
	 * added by hojeij as per joseph on 12/04/2019 tp retrive by evt id and not event name
	 
     */
	
    private BigDecimal eventID;
    
    private String tableName;
    
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    /**
     * 
     * @return
     */
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public BigDecimal getQueueID() {
        return queueID;
    }
    public void setQueueID(BigDecimal queueID) {
        this.queueID = queueID;
    }
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
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
	 * @return the subscriberID
	 */
	public BigDecimal getSubscriberID() {
	    return subscriberID;
	}
	/**
	 * @param subscriberID the subscriberID to set
	 */
	public void setSubscriberID(BigDecimal subscriberID) {
	    this.subscriberID = subscriberID;
	}
	
    
    

}
