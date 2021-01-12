package com.path.alert.vo.notification;

import java.math.BigDecimal;

public class ALRT_EVT_REPORT_ATTACHCO {
    
    private BigDecimal EVT_ID;
    private String COMMUNICATION_TYPE;
    private BigDecimal REPORT_ID;
    public BigDecimal getEVT_ID() {
        return EVT_ID;
    }
    public void setEVT_ID(BigDecimal eVT_ID) {
        EVT_ID = eVT_ID;
    }
    public String getCOMMUNICATION_TYPE() {
        return COMMUNICATION_TYPE;
    }
    public void setCOMMUNICATION_TYPE(String cOMMUNICATION_TYPE) {
        COMMUNICATION_TYPE = cOMMUNICATION_TYPE;
    }
    public BigDecimal getREPORT_ID() {
        return REPORT_ID;
    }
    public void setREPORT_ID(BigDecimal rEPORT_ID) {
        REPORT_ID = rEPORT_ID;
    }
    
    

}
