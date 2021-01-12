package com.path.alert.vo.notification;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class SubscriptionCO extends BaseVO{
    
    private BigDecimal subscriptionID;
    private String sourceOfContact;
    
    
    public BigDecimal getSubscriptionID() {
        return subscriptionID;
    }
    public void setSubscriptionID(BigDecimal subscriptionID) {
        this.subscriptionID = subscriptionID;
    }
    public String getSourceOfContact() {
        return sourceOfContact;
    }
    public void setSourceOfContact(String sourceOfContact) {
        this.sourceOfContact = sourceOfContact;
    }
    
    

}
