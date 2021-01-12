package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class ALRT_QUEUE_DETAILSCO extends BaseVO {
    
    private BigDecimal queueId;
    private String tableName;
    private String newValues;
    private byte[] oldValues;
    private String whereSyntax;
    private String masterYN;
    public BigDecimal getQueueId() {
        return queueId;
    }
    public void setQueueId(BigDecimal queueId) {
        this.queueId = queueId;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getNewValues() {
        return newValues;
    }
    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }
    public byte[] getOldValues() {
        return oldValues;
    }
    public void setOldValues(byte[] oldValues) {
        this.oldValues = oldValues;
    }
    public String getWhereSyntax() {
        return whereSyntax;
    }
    public void setWhereSyntax(String whereSyntax) {
        this.whereSyntax = whereSyntax;
    }
    public String getMasterYN() {
        return masterYN;
    }
    public void setMasterYN(String masterYN) {
        this.masterYN = masterYN;
    }
    
    

}
