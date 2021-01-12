package com.path.alert.vo.engine;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.alert.vo.alerts.ALRT_EVT_COMM_INTRNLVO;
import com.path.lib.vo.BaseVO;

/**
 * This class represent a collection of the message for different 
 * communication mode type
 * @author MohammadAliMezzawi
 *
 */
public class AlertMessageListCO extends BaseVO{
    
    private ArrayList<AlertMessageCO> alertMessageList;
    private BigDecimal errorCode;
    private String errorDesc;
    private ALRT_EVT_COMM_INTRNLVO alrt_EVT_COMM_INTRNLVO;
    
    public ArrayList<AlertMessageCO> getAlertMessageList() {
        return alertMessageList;
    }
    public void setAlertMessageList(ArrayList<AlertMessageCO> alertMessageList) {
        this.alertMessageList = alertMessageList;
    }
    public BigDecimal getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(BigDecimal errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorDesc() {
        return errorDesc;
    }
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
    /**
     * @return the alrt_EVT_COMM_INTRNLVO
     */
    public ALRT_EVT_COMM_INTRNLVO getAlrt_EVT_COMM_INTRNLVO()
    {
        return alrt_EVT_COMM_INTRNLVO;
    }
    /**
     * @param alrt_EVT_COMM_INTRNLVO the alrt_EVT_COMM_INTRNLVO to set
     */
    public void setAlrt_EVT_COMM_INTRNLVO(ALRT_EVT_COMM_INTRNLVO alrt_EVT_COMM_INTRNLVO)
    {
        this.alrt_EVT_COMM_INTRNLVO = alrt_EVT_COMM_INTRNLVO;
    }
}
