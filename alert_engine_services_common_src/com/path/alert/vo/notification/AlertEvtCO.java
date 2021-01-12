package com.path.alert.vo.notification;

import java.math.BigDecimal;

public class AlertEvtCO
{
    private BigDecimal evtID;
    private String evt_type;
    private String status;
    private BigDecimal fixedEvtID;
    private String messageBody;
    private String messageSubject;
    private String defaultMessageBody;
    private String defaultMessageSubject;   
    private BigDecimal queryId; 
    private BigDecimal reportId;
    private String templateType;
    private String eventName;
    private String langCode;
    private String communicationType;
    private BigDecimal subscriberID;
    private BigDecimal subscriptionID;
    private String sendTo;
    
    
    
    
    public BigDecimal getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(BigDecimal subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getCommunicationType()
    {
        return communicationType;
    }

    public void setCommunicationType(String communicationType)
    {
        this.communicationType = communicationType;
    }

    

    public String getLangCode()
    {
        return langCode;
    }

    public void setLangCode(String langCode)
    {
        this.langCode = langCode;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public BigDecimal getEvtID()
    {
        return evtID;
    }

    public void setEvtID(BigDecimal evtID)
    {
        this.evtID = evtID;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public BigDecimal getFixedEvtID()
    {
        return fixedEvtID;
    }

    public void setFixedEvtID(BigDecimal fixedEvtID)
    {
        this.fixedEvtID = fixedEvtID;
    }

    
    public String getDefaultMessageBody()
    {
        return defaultMessageBody;
    }

    public void setDefaultMessageBody(String defaultMessageBody)
    {
        this.defaultMessageBody = defaultMessageBody;
    }

    public String getDefaultMessageSubject()
    {
        return defaultMessageSubject;
    }

    public void setDefaultMessageSubject(String defaultMessageSubject)
    {
        this.defaultMessageSubject = defaultMessageSubject;
    }

    public String getTemplateType()
    {
        return templateType;
    }

    public void setTemplateType(String templateType)
    {
        this.templateType = templateType;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    public void setMessageBody(String messageBody)
    {
        this.messageBody = messageBody;
    }

    public String getMessageSubject()
    {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject)
    {
        this.messageSubject = messageSubject;
    }

    public BigDecimal getQueryId()
    {
        return queryId;
    }

    public void setQueryId(BigDecimal queryId)
    {
        this.queryId = queryId;
    }

    public BigDecimal getReportId()
    {
        return reportId;
    }

    public void setReportId(BigDecimal reportId)
    {
        this.reportId = reportId;
    }
    
    public String getEvt_type()
    {
        return evt_type;
    }

    public void setEvt_type(String evt_type)
    {
        this.evt_type = evt_type;
    }

    public BigDecimal getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(BigDecimal subscriberID) {
        this.subscriberID = subscriberID;
    }

    /**
     * @return the sendTo
     */
    public String getSendTo()
    {
        return sendTo;
    }

    /**
     * @param sendTo the sendTo to set
     */
    public void setSendTo(String sendTo)
    {
        this.sendTo = sendTo;
    }
    
}
