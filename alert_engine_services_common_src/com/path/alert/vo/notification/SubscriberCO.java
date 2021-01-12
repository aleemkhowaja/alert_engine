/**
 * SubscriberCO.java - Dec 24, 2018  
 *
 * Copyright 2018, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * @author: Raed Saad
 *
 */
package com.path.alert.vo.notification;

import java.math.BigDecimal;

public class SubscriberCO {
	private BigDecimal subScriberId;
	private String lang;
	private String briefName;
	private String middleName;
	private String longName;
	private String status;
	private String address;
	private String emails;
	private String mobiles;
	private String defaultBriefName;
	private String defaultMiddleName;
	private String defaultLongName;
	private String defaultAddress;
	private BigDecimal cifNo;
	private SubscriptionCO subscriptionCO;
	private String channelEndUser;
	private String oldMobiles;
	private String userId;
	
	
	
	
	
	public String getDefaultBriefName() {
	    return defaultBriefName;
	}
	public void setDefaultBriefName(String defaultBriefName) {
	    this.defaultBriefName = defaultBriefName;
	}
	public String getDefaultMiddleName() {
	    return defaultMiddleName;
	}
	public void setDefaultMiddleName(String defaultMiddleName) {
	    this.defaultMiddleName = defaultMiddleName;
	}
	public String getDefaultLongName() {
	    return defaultLongName;
	}
	public void setDefaultLongName(String defaultLongName) {
	    this.defaultLongName = defaultLongName;
	}
	public String getDefaultAddress() {
	    return defaultAddress;
	}
	public void setDefaultAddress(String defaultAddress) {
	    this.defaultAddress = defaultAddress;
	}
	public String getEmails()
	{
	    return emails;
	}
	public void setEmails(String emails)
	{
	    this.emails = emails;
	}
	public String getMobiles()
	{
	    return mobiles;
	}
	public void setMobiles(String mobiles)
	{
	    this.mobiles = mobiles;
	}
	public String getLang()
	{
	    return lang;
	}
	public void setLang(String lang)
	{
	    this.lang = lang;
	}
	public String getBriefName()
	{
	    return briefName;
	}
	public void setBriefName(String briefName)
	{
	    this.briefName = briefName;
	}
	public String getMiddleName()
	{
	    return middleName;
	}
	public void setMiddleName(String middleName)
	{
	    this.middleName = middleName;
	}
	public String getLongName()
	{
	    return longName;
	}
	public void setLongName(String longName)
	{
	    this.longName = longName;
	}
	public String getStatus()
	{
	    return status;
	}
	public void setStatus(String status)
	{
	    this.status = status;
	}
	public String getAddress()
	{
	    return address;
	}
	public void setAddress(String address)
	{
	    this.address = address;
	}
	
	public BigDecimal getSubScriberId() {
		return subScriberId;
	}
	public void setSubScriberId(BigDecimal subScriberId) {
		this.subScriberId = subScriberId;
	}
	
	
	public BigDecimal getCifNo() {
		return cifNo;
	}
	public void setCifNo(BigDecimal cifNo) {
		this.cifNo = cifNo;
	}
	/**
	 * @return the subscriptionCO
	 */
	public SubscriptionCO getSubscriptionCO() {
	    return subscriptionCO;
	}
	/**
	 * @param subscriptionCO the subscriptionCO to set
	 */
	public void setSubscriptionCO(SubscriptionCO subscriptionCO) {
	    this.subscriptionCO = subscriptionCO;
	}
	public String getChannelEndUser()
	{
	    return channelEndUser;
	}
	public void setChannelEndUser(String channelEndUser)
	{
	    this.channelEndUser = channelEndUser;
	}
	/**
	 * @return the oldMobiles
	 */
	public String getOldMobiles()
	{
	    return oldMobiles;
	}
	/**
	 * @param oldMobiles the oldMobiles to set
	 */
	public void setOldMobiles(String oldMobiles)
	{
	    this.oldMobiles = oldMobiles;
	}
	/**
	 * @return the userId
	 */
	public String getUserId()
	{
	    return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId)
	{
	    this.userId = userId;
	}
}
