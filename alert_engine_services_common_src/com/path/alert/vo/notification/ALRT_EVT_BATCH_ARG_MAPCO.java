package com.path.alert.vo.notification;

import java.math.BigDecimal;

public class ALRT_EVT_BATCH_ARG_MAPCO {
    
    private BigDecimal eventID;
    private BigDecimal compCode;
    private String ARG_NAME;
    private String MAPPING_TAG_NAME;
    private String MAPPING_VALUE;
    
    public String getARG_NAME() {
        return ARG_NAME;
    }
    public void setARG_NAME(String aRG_NAME) {
        ARG_NAME = aRG_NAME;
    }
    public String getMAPPING_TAG_NAME() {
        return MAPPING_TAG_NAME;
    }
    public void setMAPPING_TAG_NAME(String mAPPING_TAG_NAME) {
        MAPPING_TAG_NAME = mAPPING_TAG_NAME;
    }
    public String getMAPPING_VALUE() {
        return MAPPING_VALUE;
    }
    public void setMAPPING_VALUE(String mAPPING_VALUE) {
        MAPPING_VALUE = mAPPING_VALUE;
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
    
    
    

}
