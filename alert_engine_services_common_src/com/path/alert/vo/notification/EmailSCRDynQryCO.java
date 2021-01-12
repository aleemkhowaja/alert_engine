package com.path.alert.vo.notification;

import java.math.BigDecimal;

import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.struts2.lib.common.BaseSC;

public class EmailSCRDynQryCO  extends BaseSC{
    
    private String tableName;
    private BigDecimal subId;
    private String columnPrimary;
    private String cifCond;
    private String columnSelect;
    private ALRT_ENG_MSGVO msgVO;
    
    
    public ALRT_ENG_MSGVO getMsgVO() {
        return msgVO;
    }
    public void setMsgVO(ALRT_ENG_MSGVO msgVO) {
        this.msgVO = msgVO;
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
     * @return the subId
     */
    public BigDecimal getSubId() {
        return subId;
    }
    /**
     * @param subId the subId to set
     */
    public void setSubId(BigDecimal subId) {
        this.subId = subId;
    }
    
    /**
     * @return the columnPrimary
     */
    public String getColumnPrimary() {
	return columnPrimary;
    }
    /**
     * @param columnPrimary the columnPrimary to set
     */
    public void setColumnPrimary(String columnPrimary) {
	this.columnPrimary = columnPrimary;
    }
    /**
     * @return the cifCond
     */
    public String getCifCond() {
	return cifCond;
    }
    /**
     * @param cifCond the cifCond to set
     */
    public void setCifCond(String cifCond) {
	this.cifCond = cifCond;
    }
    /**
     * @return the columnSelect
     */
    public String getColumnSelect() {
	return columnSelect;
    }
    /**
     * @param columnSelect the columnSelect to set
     */
    public void setColumnSelect(String columnSelect) {
	this.columnSelect = columnSelect;
    }
    
    

}
