package com.path.alert.bo.fixed.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.path.alert.bo.conductor.AlertConductorBO;
import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.bo.notification.AlertNotificationConstant;
import com.path.alert.dao.fixed.FixedEventDAO;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.utils.AlertEngUtils;
import com.path.alert.vo.fixed.ALRT_QUEUE_DETAILSCO;
import com.path.alert.vo.fixed.ALRT_QUEUE_ONLINECO;
import com.path.alert.vo.fixed.AlertReceiverCO;
import com.path.alert.vo.fixed.FixedDynQueryCO;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.bo.common.CommonLibBO;
import com.path.bo.common.CommonMethods;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.log.Log;

public class FixedEventBOImpl extends BaseBO implements FixedEventBO
{

    public FixedEventDAO fixedEventDAO;

    public AlertConductorBO alertConductorBO;

    public CommonLibBO commonLibBO;

    protected Log logger = Log.getInstance();

    public void setCommonLibBO(CommonLibBO commonLibBO)
    {
	this.commonLibBO = commonLibBO;
    }

    public FixedEventDAO getFixedEventDAO()
    {
	return fixedEventDAO;
    }

    public void setFixedEventDAO(FixedEventDAO fixedEventDAO)
    {
	this.fixedEventDAO = fixedEventDAO;
    }
    
    /**
     * Get 
     */
    public ArrayList<AlertNotificationCO> selectFromQueueOnline(String bulkYN) throws BaseException
    {
	// lock rows
	logger.debug("In selectFromQueueOnline bulk => " + bulkYN);
	ALRT_QUEUE_ONLINECO alrtQueue = new ALRT_QUEUE_ONLINECO();
	alrtQueue.setMachineIpName(AlertEngUtils.getComputerName());
	alrtQueue.setBulkYN(bulkYN);
	
	/**
	 * set the connection co
	 * Because the data will insert inside the ALRT_QUEUE_ONLINE table from core
	 * and we need to check if the IMAL_CORE_YN='N' in primary db (it means alert has standalone database) then we need to
	 * add the connection object of core db
	 */
	alrtQueue.setConnCO(AlertEngine.getInstance().getConnCO());
	
	
	alrtQueue = fixedEventDAO.updateAlrtQueueStatus(alrtQueue);

	// Get Online rows
	ArrayList<ALRT_QUEUE_ONLINECO> alrtQueueList = fixedEventDAO.selectFromQueueOnline(alrtQueue);
	logger.debug("In selectFromQueueOnline getting from queue result =>" 
		+ Arrays.toString(alrtQueueList.toArray()));
	
	// Start preparing the notification receiver details by looping on alert queue online
	ArrayList<AlertNotificationCO> alertNotList = new ArrayList<AlertNotificationCO>();
	
	for(ALRT_QUEUE_ONLINECO alert : alrtQueueList)
	{
	    AlertNotificationCO alertNotificationCO = new AlertNotificationCO();
	    alertNotificationCO.setQueueId(alert.getID());
	    alertNotificationCO.setEventId(alert.getEvtId());
	    alertNotificationCO.setBulkMode(alert.getBulkYN().equals(ConstantsCommon.YES));
	    alertNotificationCO.setLangCode(alert.getLangCode());
	    // Get Receiver details info
	    logger.debug("In selectFromQueueOnline for queue id =>" + alert.getID());
	    
	    AlertReceiverCO receiverCO = new AlertReceiverCO();
	    receiverCO.setQueueID(alert.getID());
	    
	    /**
	     * set the connection co
	     * Because the data will insert inside the ALRT_RECEIVER_DETAILS and ALRT_RECEIVER_DATA tables from core
	     * and we need to check if the IMAL_CORE_YN='N' in primary db (it means alert has standalone database) then we need to
	     * add the connection object of core db
	     */
	    receiverCO.setConnCO(AlertEngine.getInstance().getConnCO());
		
	    ArrayList<AlertReceiverCO> alertReceiverCOList = fixedEventDAO.getReceiverDetails(receiverCO);
	    logger.debug("In selectFromQueueOnline after getting receiver details, result => "
		    + Arrays.toString(alrtQueueList.toArray()));
	    
	    // i hate this approach , this should be rewritten later
	    HashMap<String, ArrayList<BigDecimal>> receiverList = new HashMap<String, ArrayList<BigDecimal>>();
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_SUB, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_CIF, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_USER_ID, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_CHANNEL, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_FB, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_TW, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_MAIL, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_MOBILE, new ArrayList<BigDecimal>());
	    receiverList.put(AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT, new ArrayList<BigDecimal>());
	    alertNotificationCO.setReceiverList(receiverList);
	    
	    // if we can send the receiver type from the sql it would be great
	    // @todo we may add receiver type at db level ( alert queue receiver details )
	    for(AlertReceiverCO alertReceiverCO : alertReceiverCOList)
	    {
		alertNotificationCO.setCompCode(alertReceiverCO.getCompCode());
		if(null != alertReceiverCO.getAccount())
		{
		    alertNotificationCO.getAccountList().add(alertReceiverCO.getAccount());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_ACCOUNT)
		    	.add(alertReceiverCO.getId());
		    continue;
		}
		

		if(null != alertReceiverCO.getCifNO())
		{
		    alertNotificationCO.getCifList().add(alertReceiverCO.getCifNO());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_CIF)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getEmail())
		{
		    alertNotificationCO.getEmailsList().add(alertReceiverCO.getEmail());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_MAIL)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getEndChannelUser())
		{
		    alertNotificationCO.getChannelUserIdsList().add(alertReceiverCO.getEndChannelUser());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_CHANNEL)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getFbID())
		{
		    alertNotificationCO.getFacebookSocialIdsList().add(alertReceiverCO.getFbID());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_FB)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getImalUserId())
		{
		    alertNotificationCO.getImalUserIdsList().add(alertReceiverCO.getImalUserId());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_USER_ID)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getMobile())
		{
		    alertNotificationCO.getMobilesList().add(alertReceiverCO.getMobile());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_MOBILE)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getTwID())
		{
		    alertNotificationCO.getTwitterSocialIdsList().add(alertReceiverCO.getTwID());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_TW)
		    	.add(alertReceiverCO.getId());
		    continue;
		}

		if(null != alertReceiverCO.getSubscriberID())
		{
		    alertNotificationCO.getSubscriberIdsList().add(alertReceiverCO.getSubscriberID());
		    alertNotificationCO.getReceiverList()
		    	.get(AlertNotificationConstant.RECEIVER_TYPE_SUB)
		    	.add(alertReceiverCO.getId());
		    continue;
		}
	    }
	    alertNotList.add(alertNotificationCO);
	}
	
	logger.debug(
		"In selectFromQueueOnline filling account list, result => " + Arrays.toString(alertNotList.toArray()));
	return alertNotList;
    }

    public void setAlertConductorBO(AlertConductorBO alertConductorBO)
    {
	this.alertConductorBO = alertConductorBO;
    }

    @Override
    public HashMap<String, Object> getHash(BigDecimal evtID, BigDecimal receiverID) throws BaseException
    {
	// TODO Auto-generated method stub

	Map<String, Object> listHash = new HashMap<String, Object>();

	// ArrayList<BigDecimal> allEvts =
	// fixedEventDAO.returnAllEvtByFixed(alrtQueueRec);
	// columnNames =
	// fixedEventDAO.getColumnsName(alrtQueueRec.getFixedEvtID());
	ArrayList<ALRT_QUEUE_DETAILSCO> alrtDetails = new ArrayList<ALRT_QUEUE_DETAILSCO>();
	logger.info("getting queue details: receiverID =>" + receiverID + ",evtID=>" + evtID);
	
	AlertReceiverCO receiverCO = new AlertReceiverCO();
	receiverCO.setId(receiverID);
	
	/**
	 * set the connection co
	 * Because the data will insert inside the ALRT_RECEIVER_DETAILS and ALRT_RECEIVER_DATA tables from core
	 * and we need to check if the IMAL_CORE_YN='N' in primary db (it means alert has standalone database) then we need to
	 * add the connection object of core db
	 */
	receiverCO.setConnCO(AlertEngine.getInstance().getConnCO());
	    
	alrtDetails = fixedEventDAO.returnReceiverData(receiverCO);
	for(ALRT_QUEUE_DETAILSCO alrt : alrtDetails)
	{
	    if(null != alrt.getNewValues())
	    {
		try
		{

		    /*
		     * json = convertStringToJson(new
		     * String(alrt.getNewValues(),
		     * FileUtil.DEFAULT_FILE_ENCODING));
		     */
		    /*
		     * hashColumns.putAll(jsonToMap(json,alrt.getTableName()));
		     */
		    logger.info("Json object is sent for alert details with table:" + alrt.getTableName() + "=>"
			    + alrt.getNewValues());
		    listHash.putAll((HashMap<String, Object>) CommonMethods.returnJsonObjectFromStr(HashMap.class,
			    alrt.getNewValues()));
		    
		    
		    logger.info("HashMap from json" + listHash);
		}
		catch(Exception e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	    else
	    {
		if(null != alrt.getWhereSyntax())
		{
		    logger.info("Where Cond is filled for alert details with table:" + alrt.getTableName() + "=>"
			    + alrt.getWhereSyntax());
		    FixedTagCO fixedTagCO = new FixedTagCO();
		    BigDecimal fixedEvtId = fixedEventDAO.getFixedEvtId(evtID);
		    logger.info(evtID + " has fixed evt id:" + fixedEvtId);
		    fixedTagCO.setFixed_evt_id(fixedEvtId);
		    fixedTagCO.setTableName(alrt.getTableName());
		    
		    /**
		     * set the connection co Because the data will insert inside
		     * the ALRT_RECEIVER_DETAILS and ALRT_RECEIVER_DATA tables
		     * from core and we need to check if the IMAL_CORE_YN='N' in
		     * primary db (it means alert has standalone database) then
		     * we need to add the connection object of core db
		     */
		    fixedTagCO.setConnCO(AlertEngine.getInstance().getConnCO());
			    
		    ArrayList<String> columnTags = fixedEventDAO.returnColumnTags(fixedTagCO);
		    String columns = String.join(",", columnTags);
		    logger.info("Columns defined for fx evt id =>" + columns);
		    FixedDynQueryCO fixedDynQueryCO = new FixedDynQueryCO();
		    fixedDynQueryCO.setColumnsList(columns);
		    fixedDynQueryCO.setWhereCond(alrt.getWhereSyntax());
		    fixedDynQueryCO.setTableName(alrt.getTableName());
		    
		    /**
		     * set the connection co Because the data will insert inside
		     * the ALRT_RECEIVER_DETAILS and ALRT_RECEIVER_DATA tables
		     * from core and we need to check if the IMAL_CORE_YN='N' in
		     * primary db (it means alert has standalone database) then
		     * we need to add the connection object of core db
		     */
		    fixedDynQueryCO.setConnCO(AlertEngine.getInstance().getConnCO());
		    
		    listHash.putAll(alertConductorBO.executeDynQuery(fixedDynQueryCO));
		    logger.info("HashMap from json" + listHash);
		}
	    }
	}

	return (HashMap<String, Object>) listHash;
    }

    @Override
    public String selectExpression(BigDecimal evt_id) throws BaseException
    {
	// TODO Auto-generated method stub
	return fixedEventDAO.selectExpression(evt_id);
    }

    @Override
    public FixedTagCO selectFixedTags(BigDecimal fixedEventId, String tagName) throws BaseException {
	
	FixedTagCO co = new FixedTagCO();
	co.setFixed_evt_id(fixedEventId);
	co.setTagName(tagName);
	return fixedEventDAO.selectFixedTags(co);	
    }

}
