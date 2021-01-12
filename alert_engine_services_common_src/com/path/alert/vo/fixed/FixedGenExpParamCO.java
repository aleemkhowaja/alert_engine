package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class FixedGenExpParamCO extends BaseVO {
    
    private String columnName;
    private BigDecimal evtId;
    private String value;
    private String operationType;
    
    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public BigDecimal getEvtId() {
        return evtId;
    }
    public void setEvtId(BigDecimal evtId) {
        this.evtId = evtId;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    

}
