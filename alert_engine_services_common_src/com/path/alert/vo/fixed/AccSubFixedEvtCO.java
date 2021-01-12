package com.path.alert.vo.fixed;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.lib.vo.BaseVO;
import com.path.vo.alert.notification.Account;

public class AccSubFixedEvtCO extends BaseVO{
    
    private ArrayList<BigDecimal> eventList;
    
    private Account fromAccount;
    
    private Account toAccount;

    public ArrayList<BigDecimal> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<BigDecimal> eventList) {
        this.eventList = eventList;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
    
    

}
