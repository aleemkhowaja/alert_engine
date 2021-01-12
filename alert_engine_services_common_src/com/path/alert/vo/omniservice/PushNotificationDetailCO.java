package com.path.alert.vo.omniservice;

import java.math.BigDecimal;
import java.util.Date;

import com.path.lib.vo.BaseVO;

public class PushNotificationDetailCO extends BaseVO
{
	private String title;
    private String body;
    private String notifData;
    private String landingPage;
    private BigDecimal pushNotificationId;    
    private Date	notificationDate;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getNotifData() {
		return notifData;
	}
	public void setNotifData(String notifData) {
		this.notifData = notifData;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public BigDecimal getPushNotificationId() {
		return pushNotificationId;
	}
	public void setPushNotificationId(BigDecimal pushNotificationId) {
		this.pushNotificationId = pushNotificationId;
	}
	public Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
    
    
}