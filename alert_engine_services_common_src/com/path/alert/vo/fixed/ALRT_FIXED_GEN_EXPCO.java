package com.path.alert.vo.fixed;

import com.path.lib.vo.BaseVO;

public class ALRT_FIXED_GEN_EXPCO extends BaseVO {

    private String columnName;
    private String fixedOperator;
    private String newValue;
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getFixedOperator() {
        return fixedOperator;
    }
    public void setFixedOperator(String fixedOperator) {
        this.fixedOperator = fixedOperator;
    }
    public String getNewValue() {
        return newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    
}
