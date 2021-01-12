package com.path.alert.vo.omniservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.path.lib.vo.BaseVO;

public class PushNotificationCO extends BaseVO
{
    private String udid;
    private String deviceToken;
    private BigDecimal notifDeviceToekenId;    
    private Date	tokenCreateDate;
    private List<PushNotificationDetailCO>  pushNotificationDetailList = new ArrayList<PushNotificationDetailCO>();
    private Integer outputCode;
    private String outputNotification;
    private String outputNotificationDetails;
    private String outputType;
    private BigDecimal ocUserId;
    private BigDecimal appId;
    
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public BigDecimal getNotifDeviceToekenId() {
		return notifDeviceToekenId;
	}
	public void setNotifDeviceToekenId(BigDecimal notifDeviceToekenId) {
		this.notifDeviceToekenId = notifDeviceToekenId;
	}
	public Date getTokenCreateDate() {
		return tokenCreateDate;
	}
	public void setTokenCreateDate(Date tokenCreateDate) {
		this.tokenCreateDate = tokenCreateDate;
	}
	public List<PushNotificationDetailCO> getPushNotificationDetailList() {
		return pushNotificationDetailList;
	}
	public void setPushNotificationDetailList(List<PushNotificationDetailCO> pushNotificationDetailList) {
		this.pushNotificationDetailList = pushNotificationDetailList;
	}
	public Integer getOutputCode() {
		return outputCode;
	}
	public String getOutputNotification() {
		return outputNotification;
	}
	public String getOutputNotificationDetails() {
		return outputNotificationDetails;
	}
	public String getOutputType() {
		return outputType;
	}
	public void setOutputCode(Integer outputCode) {
		this.outputCode = outputCode;
	}
	public void setOutputNotification(String outputNotification) {
		this.outputNotification = outputNotification;
	}
	public void setOutputNotificationDetails(String outputNotificationDetails) {
		this.outputNotificationDetails = outputNotificationDetails;
	}
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	public BigDecimal getOcUserId() {
		return ocUserId;
	}
	public BigDecimal getAppId() {
		return appId;
	}
	public void setOcUserId(BigDecimal ocUserId) {
		this.ocUserId = ocUserId;
	}
	public void setAppId(BigDecimal appId) {
		this.appId = appId;
	}
	
}