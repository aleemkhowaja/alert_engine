package com.path.alert.vo.omniservice;

import java.math.BigDecimal;

import com.path.struts2.lib.common.GridParamsSC;

public class PushNotificationSC extends GridParamsSC
{
    private String title;
    private String body;
    private String notifData;
    private String landingPage;
    private String deviceToken;
    private String workingUdid;
    private String uDID;
    private BigDecimal ocUserId;
    private String channelUserName;
    private BigDecimal appId;
    private BigDecimal channelId;

    public String getTitle()
    {
	return title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getBody()
    {
	return body;
    }

    public void setBody(String body)
    {
	this.body = body;
    }

    public String getNotifData()
    {
	return notifData;
    }

    public void setNotifData(String notifData)
    {
	this.notifData = notifData;
    }

    public String getLandingPage()
    {
	return landingPage;
    }

    public void setLandingPage(String landingPage)
    {
	this.landingPage = landingPage;
    }

    public String getDeviceToken()
    {
	return deviceToken;
    }

    public void setDeviceToken(String deviceToken)
    {
	this.deviceToken = deviceToken;
    }

    public String getWorkingUdid()
    {
	return workingUdid;
    }

    public String getuDID()
    {
	return uDID;
    }

    public BigDecimal getOcUserId()
    {
	return ocUserId;
    }

    public void setWorkingUdid(String workingUdid)
    {
	this.workingUdid = workingUdid;
    }

    public void setuDID(String uDID)
    {
	this.uDID = uDID;
    }

    public void setOcUserId(BigDecimal ocUserId)
    {
	this.ocUserId = ocUserId;
    }

    public BigDecimal getAppId()
    {
	return appId;
    }

    public BigDecimal getChannelId()
    {
	return channelId;
    }

    public void setAppId(BigDecimal appId)
    {
	this.appId = appId;
    }

    public void setChannelId(BigDecimal channelId)
    {
	this.channelId = channelId;
    }

    public String getChannelUserName()
    {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName)
    {
        this.channelUserName = channelUserName;
    }
    
    

}