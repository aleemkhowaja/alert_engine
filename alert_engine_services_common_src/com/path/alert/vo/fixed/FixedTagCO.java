package com.path.alert.vo.fixed;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class FixedTagCO  extends BaseVO{
    
    private String tableName;
    private BigDecimal fixed_evt_id;
    private String columnType;
    private String tagName;
    /**
     * @return the fixed_evt_id
     */
    public BigDecimal getFixed_evt_id() {
	return fixed_evt_id;
    }
    /**
     * @param fixed_evt_id the fixed_evt_id to set
     */
    public void setFixed_evt_id(BigDecimal fixed_evt_id) {
	this.fixed_evt_id = fixed_evt_id;
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
    /**
     * @return the columnType
     */
    public String getColumnType() {
	return columnType;
    }
    /**
     * @param columnType the columnType to set
     */
    public void setColumnType(String columnType) {
	this.columnType = columnType;
    }
    /**
     * @return the tagName
     */
    public String getTagName() {
	return tagName;
    }
    /**
     * @param tagName the tagName to set
     */
    public void setTagName(String tagName) {
	this.tagName = tagName;
    }
    

}
