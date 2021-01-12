package com.path.alert.vo.notification;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class QueryCO extends BaseVO
{
    private String columnName;
    private String query;
    private String eventName;
    private BigDecimal evtID;
    private BigDecimal compCode;
    private String status;
    
    //private ArrayList<BigDecimal> cifLists;
    
    
    
    public String getEventName()
    {
        return eventName;
    }
    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }
    /*public ArrayList<BigDecimal> getCifLists()
    {
        return cifLists;
    }
    public void setCifLists(ArrayList<BigDecimal> cifLists)
    {
        this.cifLists = cifLists;
    }*/
    public String getColumnName()
    {
        return columnName;
    }
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    public String getQuery()
    {
        return query;
    }
    public void setQuery(String query)
    {
        this.query = query;
    }
    /**
     * @return the evtID
     */
    public BigDecimal getEvtID() {
	return evtID;
    }
    /**
     * @param evtID the evtID to set
     */
    public void setEvtID(BigDecimal evtID) {
	this.evtID = evtID;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
    

}
