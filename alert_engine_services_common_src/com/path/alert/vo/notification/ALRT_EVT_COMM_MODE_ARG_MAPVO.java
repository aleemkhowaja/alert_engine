package com.path.alert.vo.notification;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class ALRT_EVT_COMM_MODE_ARG_MAPVO  extends BaseVO
{
    private String COMMUNICATION_TYPE;
    private BigDecimal EVT_ID;
    private String ARG_NAME;
    private String MAPPING_TAG_NAME;
    private String MAPPING_VALUE;
    private BigDecimal ATTACH_REPORT_ID;
    private BigDecimal QUERY_REPORT_ID;
    
    public String getCOMMUNICATION_TYPE()
    {
        return COMMUNICATION_TYPE;
    }
    public void setCOMMUNICATION_TYPE(String cOMMUNICATION_TYPE)
    {
        COMMUNICATION_TYPE = cOMMUNICATION_TYPE;
    }
    public BigDecimal getEVT_ID()
    {
        return EVT_ID;
    }
    public void setEVT_ID(BigDecimal eVT_ID)
    {
        EVT_ID = eVT_ID;
    }
    public String getARG_NAME()
    {
        return ARG_NAME;
    }
    public void setARG_NAME(String aRG_NAME)
    {
        ARG_NAME = aRG_NAME;
    }
    public String getMAPPING_TAG_NAME()
    {
        return MAPPING_TAG_NAME;
    }
    public void setMAPPING_TAG_NAME(String mAPPING_TAG_NAME)
    {
        MAPPING_TAG_NAME = mAPPING_TAG_NAME;
    }
    public String getMAPPING_VALUE()
    {
        return MAPPING_VALUE;
    }
    public void setMAPPING_VALUE(String mAPPING_VALUE)
    {
        MAPPING_VALUE = mAPPING_VALUE;
    }
    /**
     * @return the aTTACH_REPORT_ID
     */
    public BigDecimal getATTACH_REPORT_ID() {
	return ATTACH_REPORT_ID;
    }
    /**
     * @param aTTACH_REPORT_ID the aTTACH_REPORT_ID to set
     */
    public void setATTACH_REPORT_ID(BigDecimal aTTACH_REPORT_ID) {
	ATTACH_REPORT_ID = aTTACH_REPORT_ID;
    }
    /**
     * @return the qUERY_REPORT_ID
     */
    public BigDecimal getQUERY_REPORT_ID() {
	return QUERY_REPORT_ID;
    }
    /**
     * @param qUERY_REPORT_ID the qUERY_REPORT_ID to set
     */
    public void setQUERY_REPORT_ID(BigDecimal qUERY_REPORT_ID) {
	QUERY_REPORT_ID = qUERY_REPORT_ID;
    }
    
    
    
}
