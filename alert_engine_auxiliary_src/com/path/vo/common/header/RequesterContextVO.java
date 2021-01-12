package com.path.vo.common.header;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.path.lib.common.util.DateTimeAdapter;

public class RequesterContextVO {

    private String userID;
    private String hostName;
    private String ChannelID;
    private String hashKey;
    private String password;
    private String langId;
    private Date   requesterTimeStamp;
    
    @XmlElement(required = true)
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getChannelID() {
		return ChannelID;
	}
	public void setChannelID(String channelID) {
		ChannelID = channelID;
	}
	
	public String getHashKey() {
		return hashKey;
	}
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	
	@XmlElement(required = true)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@XmlElement(required = true)
	public String getLangId() {
		return langId;
	}
	public void setLangId(String langId) {
		this.langId = langId;
	}
	@XmlSchemaType(name="dateTime")
	    @XmlJavaTypeAdapter(value=DateTimeAdapter.class, type=Date.class)
	@XmlElement(required = true)
	public Date getRequesterTimeStamp() {
		return requesterTimeStamp;
	}
	public void setRequesterTimeStamp(Date requesterTimeStamp) {
		this.requesterTimeStamp = requesterTimeStamp;
	}
}
