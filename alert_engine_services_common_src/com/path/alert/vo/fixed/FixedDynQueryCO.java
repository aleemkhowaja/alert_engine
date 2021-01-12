package com.path.alert.vo.fixed;

import com.path.lib.vo.BaseVO;

public class FixedDynQueryCO extends BaseVO {
    
    private String tableName;
    private String columnsList;
    private String whereCond;
    public String getColumnsList() {
        return columnsList;
    }
    public void setColumnsList(String columnsList) {
        this.columnsList = columnsList;
    }
    public String getWhereCond() {
        return whereCond;
    }
    public void setWhereCond(String whereCond) {
        this.whereCond = whereCond;
    }
    /**
     * @return the tableName
     */
    public String getTableName() {
	return tableName;
    }
    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
    

}
