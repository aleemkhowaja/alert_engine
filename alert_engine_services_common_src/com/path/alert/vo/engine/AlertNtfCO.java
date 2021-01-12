package com.path.alert.vo.engine;

import java.math.BigDecimal;
import java.util.HashMap;

import com.path.alert.vo.notification.EvtCO;
import com.path.alert.vo.notification.SubscriberCO;
import com.path.dbmaps.vo.ALRT_ENG_MSGVO;
import com.path.lib.vo.BaseVO;

/**
 * This class represent a single notification
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AlertNtfCO<V> extends BaseVO {

	private BigDecimal alrtReceiverID;

	private SubscriberCO subscriberCO;

	/**
	 * Receiver Id
	 */
	private V receiverId;

	/**
	 * Receiver type ( CIF , Imal User ... )
	 */
	private String receiverType;

	/**
	 * Used in Message listener
	 * 
	 * @todo check with Hojeij and Paty
	 */
	private String eventName;

	/**
	 * company code added by hojeij as per joseph on 12/04/2019 tp retrive by
	 * comp code
	 */
	private BigDecimal compCode;

	/**
	 * event id added by hojeij as per joseph on 12/04/2019 tp retrive by evt id
	 * and not event name
	 */
	private BigDecimal eventID;

	/**
	 * 
	 */
	private BigDecimal queueId;

	/**
	 * Sent Dynamically
	 * Hold tags set dynamically at the event level
	 */
	private HashMap<String, String> tagEvent;
	
	/**
	 * Batch Tags.
	 * In case the event is batch this will be populated
	 */
	private HashMap<String, Object> batchTags;

	/**
	 * 
	 */
	private ALRT_ENG_MSGVO msgVO = new ALRT_ENG_MSGVO();

	private EvtCO evtCO = new EvtCO();

	private BigDecimal errorCode;

	private String errorDesc;
	
	/**
	 * Lang Code.
	 * In case the skip subscription
	 */
	private String langCode;
	 

	/**
	 * @param receiverId
	 */
	public void setReceiverId(V receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return
	 */
	public V getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverType
	 */
	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	/**
	 * @return
	 */
	public String getReceiverType() {
		return receiverType;
	}

	/**
	 * @return the msgVO
	 */
	public ALRT_ENG_MSGVO getMsgVO() {
		return msgVO;
	}

	/**
	 * @param msgVO
	 *            the msgVO to set
	 */
	public void setMsgVO(ALRT_ENG_MSGVO msgVO) {
		this.msgVO = msgVO;
	}

	public String getEventName() {
		return eventName;
	}

	/**
	 * 
	 * @param eventName
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the tagEvent
	 */
	public HashMap<String, String> getTagEvent() {
		return tagEvent;
	}

	/**
	 * @param tagEvent
	 * the tagEvent to set
	 */
	public void setTagEvent(HashMap<String, String> tagEvent) {
		this.tagEvent = tagEvent;
	}

	/**
	 * @return the batchTags
	 */
	public HashMap<String, Object> getBatchTags() {
		return batchTags;
	}

	/**
	 * @param batchTags the batchTags to set
	 */
	public void setBatchTags(HashMap<String, Object> batchTags) {
		this.batchTags = batchTags;
	}

	public BigDecimal getQueueId() {
		return queueId;
	}

	/**
	 * Need by Hojeij
	 * 
	 * @param queueId
	 */
	public void setQueueId(BigDecimal queueId) {
		this.queueId = queueId;
	}

	/**
	 * @return the alrtReceiverID
	 */
	public BigDecimal getAlrtReceiverID() {
		return alrtReceiverID;
	}

	/**
	 * @param alrtReceiverID
	 *            the alrtReceiverID to set
	 */
	public void setAlrtReceiverID(BigDecimal alrtReceiverID) {
		this.alrtReceiverID = alrtReceiverID;
	}

	/**
	 * @return the eventID
	 */
	public BigDecimal getEventID() {
		return eventID;
	}

	/**
	 * @param eventID
	 *            the eventID to set
	 */
	public void setEventID(BigDecimal eventID) {
		this.eventID = eventID;
	}

	/**
	 * @return the compCode
	 */
	public BigDecimal getCompCode() {
		return compCode;
	}

	/**
	 * @param compCode
	 *            the compCode to set
	 */
	public void setCompCode(BigDecimal compCode) {
		this.compCode = compCode;
	}

	/**
	 * @return the errorDesc
	 */
	public String getErrorDesc() {
		return errorDesc;
	}

	/**
	 * @param errorDesc
	 *            the errorDesc to set
	 */
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	/**
	 * @return the errorCode
	 */
	public BigDecimal getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(BigDecimal errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the subscriberCO
	 */
	public SubscriberCO getSubscriberCO() {
		return subscriberCO;
	}

	/**
	 * @param subscriberCO
	 *            the subscriberCO to set
	 */
	public void setSubscriberCO(SubscriberCO subscriberCO) {
		this.subscriberCO = subscriberCO;
	}

	/**
	 * @return the evtCO
	 */
	public EvtCO getEvtCO() {
		return evtCO;
	}

	/**
	 * @param evtCO
	 *            the evtCO to set
	 */
	public void setEvtCO(EvtCO evtCO) {
		this.evtCO = evtCO;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode()
	{
	    return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode)
	{
	    this.langCode = langCode;
	}
}
