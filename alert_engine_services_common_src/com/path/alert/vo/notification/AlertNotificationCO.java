package com.path.alert.vo.notification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.path.lib.vo.BaseVO;
import com.path.vo.alert.notification.Account;

public class AlertNotificationCO extends BaseVO
{

    /**
     * CO which contains all possible combination between subscriber and event
     */
    private static final long serialVersionUID = 1L;

    /**
     * @note: this is a temporary solution needed for fixed event online queue
     */
    private HashMap<String, ArrayList<BigDecimal>> receiverList = new HashMap<String, ArrayList<BigDecimal>>();

    private ArrayList<BigDecimal> cifList = new ArrayList<BigDecimal>();

    private ArrayList<BigDecimal> subscriberIdsList = new ArrayList<BigDecimal>();

    private ArrayList<String> channelUserIdsList = new ArrayList<String>();

    private ArrayList<String> facebookSocialIdsList = new ArrayList<String>();

    private ArrayList<String> twitterSocialIdsList = new ArrayList<String>();

    private ArrayList<String> imalUserIdsList = new ArrayList<String>();

    private ArrayList<String> emailsList = new ArrayList<String>();

    private ArrayList<String> mobilesList = new ArrayList<String>();

    private ArrayList<Account> accountList = new ArrayList<Account>();
    
    private ArrayList<BigDecimal> groupIdsList = new ArrayList<BigDecimal>();

    private BigDecimal eventId;
    private String includeAllSubInGrpYN;
    
    /**
     * company code
     * added by hojeij as per joseph on 12/04/2019 tp retrive by comp code
     */
    private BigDecimal compCode;

    private HashMap<String, String> tagEvent;

    private String eventName;

    ArrayList<LinkedHashMap> resultBatch = new ArrayList<LinkedHashMap>();
    
    private EvtCO evtCO = new EvtCO();
    
    private BigDecimal groupId;
    
    private String langCode;
    
    public ArrayList<LinkedHashMap> getResultBatch()
    {
	return resultBatch;
    }

    public void setResultBatch(ArrayList<LinkedHashMap> resultQ)
    {
	this.resultBatch = resultQ;
    }

    /**
     * Flag that determine if the notification is in bulk mode or instantly
     */
    private boolean bulkMode;

    private BigDecimal queueId;

    public ArrayList<BigDecimal> getCifList()
    {
	return cifList;
    }

    public void setCifList(ArrayList<BigDecimal> cifList)
    {
	this.cifList = cifList;
    }

    public ArrayList<BigDecimal> getSubscriberIdsList() {
		return subscriberIdsList;
	}

	public void setSubscriberIdsList(ArrayList<BigDecimal> subscriberIdsList) {
		this.subscriberIdsList = subscriberIdsList;
	}

	public ArrayList<String> getChannelUserIdsList()
    {
	return channelUserIdsList;
    }

    public void setChannelUserIdsList(ArrayList<String> channelUserIdsList)
    {
	this.channelUserIdsList = channelUserIdsList;
    }

    public ArrayList<String> getFacebookSocialIdsList()
    {
	return facebookSocialIdsList;
    }

    public void setFacebookSocialIdsList(ArrayList<String> facebookSocialIdsList)
    {
	this.facebookSocialIdsList = facebookSocialIdsList;
    }

    public ArrayList<String> getTwitterSocialIdsList()
    {
	return twitterSocialIdsList;
    }

    public void setTwitterSocialIdsList(ArrayList<String> twitterSocialIdsList)
    {
	this.twitterSocialIdsList = twitterSocialIdsList;
    }

    public ArrayList<String> getImalUserIdsList()
    {
	return imalUserIdsList;
    }

    public void setImalUserIdsList(ArrayList<String> imalUserIdsList)
    {
	this.imalUserIdsList = imalUserIdsList;
    }

    public ArrayList<String> getEmailsList()
    {
	return emailsList;
    }

    public void setEmailsList(ArrayList<String> emailsList)
    {
	this.emailsList = emailsList;
    }

    public ArrayList<String> getMobilesList()
    {
	return mobilesList;
    }

    public void setMobilesList(ArrayList<String> mobilesList)
    {
	this.mobilesList = mobilesList;
    }

    public BigDecimal getEventId()
    {
	return eventId;
    }

    public void setEventId(BigDecimal eventId)
    {
	this.eventId = eventId;
    }

    public HashMap<String, String> getTagEvent()
    {
	return tagEvent;
    }

    public void setTagEvent(HashMap<String, String> tagEvent)
    {
	this.tagEvent = tagEvent;
    }

    public String getEventName()
    {
	return eventName;
    }

    public void setEventName(String eventName)
    {
	this.eventName = eventName;
    }

    public ArrayList<Account> getAccountList()
    {
	return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList)
    {
	this.accountList = accountList;
    }
    
    /**
     * @return the groupIdsList
     */
    public ArrayList<BigDecimal> getGroupIdsList()
    {
        return groupIdsList;
    }

    /**
     * @param groupIdsList the groupIdsList to set
     */
    public void setGroupIdsList(ArrayList<BigDecimal> groupIdsList)
    {
        this.groupIdsList = groupIdsList;
    }

    /**
     * Get bulk Mode flag
     * 
     * @return
     */
    public boolean isBulkMode()
    {
	return bulkMode;
    }

    /**
     * Set Bulk Mode flag
     * 
     * @param bulkMode
     */
    public void setBulkMode(boolean bulkMode)
    {
	this.bulkMode = bulkMode;
    }

    /**
     * @return the queueId
     */
    public BigDecimal getQueueId()
    {
	return queueId;
    }

    /**
     * @param queueId the queueId to set
     */
    public void setQueueId(BigDecimal queueId)
    {
	this.queueId = queueId;
    }

    /**
     * @return the receiverList
     */
    public HashMap<String, ArrayList<BigDecimal>> getReceiverList()
    {
	return receiverList;
    }

    /**
     * @param receiverList the receiverList to set
     */
    public void setReceiverList(HashMap<String, ArrayList<BigDecimal>> receiverList)
    {
	this.receiverList = receiverList;
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

    /**
     * @return the evtCO
     */
    public EvtCO getEvtCO() {
	return evtCO;
    }

    /**
     * @param evtCO the evtCO to set
     */
    public void setEvtCO(EvtCO evtCO) {
	this.evtCO = evtCO;
    }

    /**
     * @return the groupId
     */
    public BigDecimal getGroupId()
    {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(BigDecimal groupId)
    {
        this.groupId = groupId;
    }

    public String getIncludeAllSubInGrpYN()
    {
        return includeAllSubInGrpYN;
    }

    public void setIncludeAllSubInGrpYN(String includeAllSubInGrpYN)
    {
        this.includeAllSubInGrpYN = includeAllSubInGrpYN;
    }

    /**
     * @return the langCode
     */
    public String getLangCode()
    {
        return langCode;
    }

    /**
     * @param LandCode the LandCode to set
     */
    public void setLangCode(String langCode)
    {
        this.langCode = langCode;
    }
    
}
