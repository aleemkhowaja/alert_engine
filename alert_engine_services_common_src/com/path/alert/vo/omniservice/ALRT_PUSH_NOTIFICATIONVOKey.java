package com.path.alert.vo.omniservice;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

public class ALRT_PUSH_NOTIFICATIONVOKey extends BaseVO {
    /**
     * This field corresponds to the database column ALRT_PUSH_NOTIFICATION.PUSH_NOTIFICATION_ID
     */
    private BigDecimal PUSH_NOTIFICATION_ID;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ALRT_PUSH_NOTIFICATION.PUSH_NOTIFICATION_ID
     *
     * @return the value of ALRT_PUSH_NOTIFICATION.PUSH_NOTIFICATION_ID
     */
    public BigDecimal getPUSH_NOTIFICATION_ID() {
        return PUSH_NOTIFICATION_ID;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ALRT_PUSH_NOTIFICATION.PUSH_NOTIFICATION_ID
     *
     * @param PUSH_NOTIFICATION_ID the value for ALRT_PUSH_NOTIFICATION.PUSH_NOTIFICATION_ID
     */
    public void setPUSH_NOTIFICATION_ID(BigDecimal PUSH_NOTIFICATION_ID) {
        this.PUSH_NOTIFICATION_ID = PUSH_NOTIFICATION_ID;
    }
}