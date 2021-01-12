package com.path.alert.engine.core;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.path.alert.engine.policy.AlertReqProcessingPolicy;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;

/**
 * This class is the house of all Engine System and Subsystem configuration
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AlertEngConfig {
	
	
	private AlertReqProcessingPolicy reqProcessingPolicy = new AlertReqProcessingPolicy();
	/**
	 * enable flag, If enabled the engine will start on this server
	 */
	private boolean enabled;
	
	/**
	 * failover flag, If enabled the engine will start in Master/slave topology
	 */
	private boolean failover;
	
	/**
	 * Check if we should use the default sender
	 */
	private boolean defaultSender;
	
	/**
	 * Check if we should use the default sender
	 */
	private boolean defaultSMSSender;
	
	/**
	 * enable flag, If enabled the scheduler will start on this server
	 */
	private boolean schedulerEnabled;
	
	/**
	 * Hold the number of thread used by the scheduler per (scheduler) instance
	 */
	private int nbOfWorkerPerScheduler;
	
	/**
	 * Hold the maximum number of messages that can be dispatched to an individual consumer at once
	 * it should be never set to zero since it will affect the Engine performance
	 */
	private int prefetchLimit;
	
	/**
	 * Hold the Engine broker protocol
	 */
	private String brokerProtocol;
	
	/**
	 * Hold the Engine broker Url (localhost:61613,localhost:61613)
	 */
	private String brokerUrlsList;
	
	/**
	 * Hold the parsed broker Url ( connector tcp://localhost:61613)
	 * @note : never create a getter/setter , this should remain private
	 */
	private String brokerUrl;
	
	/**
	 * Hold the path of the shared file system
	 */
	private String sharedFileSystemPath;
	
	/**
	 * Max number of instant consumer
	 */
	private int instantNbOfConsumer;
	
	/**
	 * Max number of bulk consumer
	 */
	private int bulkNbOfConsumer;
	
	/**
	 * Max number of Worker in Bulk thread pool
	 * shouldn't bypass 4
	 */
	private int bulkNbOfWorker;
	
	/**
	 * Max number of Instantly session 
	 */
	private int instantMaxNbOfSession;
	
	/**
	 * Max number of bulk session 
	 */
	private int bulkMaxNbOfSession;
	
	/**
	 * Bulk Queue Reader Rate period
	 */
	private int blukQReaderPeriod;

	/**
	 * instant Queue Reader Rate period
	 */
	private int instantQReaderPeriod;
	
	/**
	 * This method will populate the Alert Eng configuration values
	 * @throws Exception 
	 */
	public void prepareConfiguration(List<HashMap<String,Object>> configurations) throws BaseException
	{
		// convert list ot keyed Map
		HashMap<String,HashMap<String,Object>> configs = new HashMap<String,HashMap<String,Object>>();
		for(HashMap<String,Object> arg : configurations) {
			
			configs.put((String) arg.get("CTRL_REFERENCE"), arg);
		}
		// set properties
		
		defaultSender = configs.get(AlertEngineConstants.DEFAULT_SENDER_CONFIG) != null ?
				NumberUtil.nullEmptyToValue((BigDecimal) configs.get(AlertEngineConstants.DEFAULT_SENDER_CONFIG)
						.get("CTRL_VALUE"), BigDecimal.ZERO).equals(BigDecimal.ZERO) :
							true;
		
		defaultSMSSender = configs.get(AlertEngineConstants.SMS_DEFAULT_SENDER_CONFIG) != null ?
				NumberUtil.nullEmptyToValue((BigDecimal) configs.get(AlertEngineConstants.SMS_DEFAULT_SENDER_CONFIG)
						.get("CTRL_VALUE"), BigDecimal.ZERO).equals(BigDecimal.ZERO) :
							true;
		
		// load properties from the property file
		loadProperties();
		
		// Populate calculate value based on load the property
		buildBrokerUrl();
	}
	
	
	/**
	 * Load properties value from property file
	 * @throws Exception
	 */
	private void loadProperties(){
		
		// common property value
		enabled = getPropertyBoolValue("alertEngine.enabled", "yes");
		brokerUrlsList = getPropertyValue("alertEngine.borkerUrl", "localhost:61613");
		brokerProtocol = getPropertyValue("alertEngine.brokerProtocol", 
				AlertEngineConstants.DEFAULT_TRANSPORT_PROTOCOL);
		sharedFileSystemPath = getPropertyValue("alertEngine.sharedFileSystemPath", "target/amq-in-action/kahadb");
		
		// consumer container configuration
		prefetchLimit = getPropertyIntValue("alertEngine.prefetchLimit", "1");
		instantNbOfConsumer = getPropertyIntValue("alertEngine.instant.concurrentConsumers","5");
		bulkNbOfConsumer = getPropertyIntValue("alertEngine.bulk.concurrentConsumers","5");
		
		// number of worker in Bulk mode thread pool ( how many concurrent task to execute )
		bulkNbOfWorker = getPropertyIntValue("alertEngine.bulk.bulkNbOfWorker","2");
		
		// Producer sessions info
		instantMaxNbOfSession = getPropertyIntValue("alertEngine.instant.concurrentSession","5");
		bulkMaxNbOfSession = getPropertyIntValue("alertEngine.bulk.concurrentSession","5");
		
		// scheduler Info
		nbOfWorkerPerScheduler = getPropertyIntValue("alertEngine.scheduler.nbOfWorker", "2");

		schedulerEnabled = getPropertyBoolValue("alertEngine.scheduler.enabled", "false");
		setInstantQReaderPeriod(getPropertyIntValue("alertEngine.instant.queueReader.period", "5"));
		setBlukQReaderPeriod(getPropertyIntValue("alertEngine.bulk.queueReader.period", "30"));
		
		// request processing policy
		reqProcessingPolicy.setChunkSize(new BigDecimal(
				getPropertyIntValue("alertEngine.chunkSize","50")));
		reqProcessingPolicy.setMaxInstantlyReceiver(new BigDecimal(
				getPropertyIntValue("alertEngine.maxInstantRecLimit", "50")));
	}
	

	/**
	 * @return
	 */
	public String returnBrokerUrl() throws Exception{
		
		if(!StringUtil.nullToEmpty(brokerUrl)
				.trim().equalsIgnoreCase("")) {
			return brokerUrl;
		}
		// build broker url
		buildBrokerUrl();
		
		return brokerUrl;
	}
	

	/**
	 * Loop through the given list of the brokers url
	 * and return the local url based on IP or hostname
	 * @return
	 * @throws Exception 
	 */
	public String returnBrokerConnector() throws Exception {
		
		String hostIp = InetAddress.getLocalHost().getHostAddress();
		String hostName = StringUtil.nullToEmpty(
			InetAddress.getLocalHost().getHostName()).toLowerCase();
		String localBrokerURL = null;
		String[] listOfBrokersUrl = brokerUrlsList.split(",");
		
		for(String url : listOfBrokersUrl){
			if(url.contains(hostIp) 
				|| url.toLowerCase(Locale.ENGLISH).contains(hostName)){
				localBrokerURL = url;
				break;
			}
		}
		
		
		// build the connector
		if(localBrokerURL != null ){
			StringBuilder sb = new StringBuilder();
			sb.append(returnTransportProtocol()).append("://")
			.append(localBrokerURL);
			localBrokerURL = sb.toString();
			
		}
		
		return localBrokerURL;	
	}
	
	
	/**
	 * Build the broker url using given Broker Urls List
	 * and broker transport protocol
	 * @return
	 * @throws Exception 
	 */
	private void buildBrokerUrl() throws BaseException{
		
		if(StringUtil.nullToEmpty(getBrokerUrlsList())
				.trim().equalsIgnoreCase("")) {
			throw new BaseException("[AlertEngine][Configuration] No broker url is defined");
		}
		// Get the list of broker
		String[] urls = getBrokerUrlsList().split(",");
		StringBuilder sb = new StringBuilder();
		
		// if we have multiple urls this mean we should apply the master/slave topology
		failover = urls.length > 1;
		
		for(String url : urls){
			sb.append(returnTransportProtocol()).append("://")
				.append(url)
				.append(",");
		}
		
		// remove the last comma
		sb.deleteCharAt(sb.length() - 1);
		
		// failover:(tcp://broker1:61616,tcp://broker2:61616,tcp://broker3:61616)
		if(failover) {
			sb.insert(0, AlertEngineConstants.FAILOVER_WRAPPER_PREFIX)
				.append(AlertEngineConstants.FAILOVER_WRAPPER_SUFFIX);
		}
		
		// append the send and dispatch asynch options
		brokerUrl = sb.append("?jms.useAsyncSend=true&jms.dispatchAsync=true")
			.toString();
	}
	
	
	/**
	 * Return the transport protocol.
	 * If null the default transport protocol will be returned
	 * @return
	 */
	private String returnTransportProtocol() {
		
		//@todo refactor this part
		if(StringUtil.nullToEmpty(getBrokerProtocol()).length() > 0) {
			return getBrokerProtocol();
		}
		setBrokerProtocol(StringUtil.nullEmptyToValue(brokerProtocol,
				AlertEngineConstants.DEFAULT_TRANSPORT_PROTOCOL));
		
		return getBrokerProtocol();
	}
	
	
	/**
	 * @return the requestProcessingPolicy
	 */
	public AlertReqProcessingPolicy getReqProcessingPolicy() {
		return reqProcessingPolicy;
	}


	/**
	 * @param reqProcessingPolicy the requestProcessingPolicy to set
	 */
	public void setReqProcessingPolicy(AlertReqProcessingPolicy reqProcessingPolicy) {
		this.reqProcessingPolicy = reqProcessingPolicy;
	}


	/**
	 * Return the property value
	 * @param propName
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	private String getPropertyValue(String propName, String defaultValue) {
		try {
			return  StringUtil.nullEmptyToValue(PathPropertyUtil.returnPathPropertyFromFile(
					"PathAlertEngineRemoting",propName), defaultValue).trim();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	

	/**
	 * Return the integer value of a property
	 * @param propName
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	private int getPropertyIntValue( String propName, String defaultValue ){
		return Integer.valueOf( getPropertyValue(propName,defaultValue));
	}

	
	/**
	 * Return the boolean value of a property
	 * @param propName
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	private boolean getPropertyBoolValue( String propName, String defaultValue ){
		return getPropertyValue(propName,defaultValue).equalsIgnoreCase(ConstantsCommon.TRUE);
	}

	
	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}
	

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
	/**
	 * @return
	 */
	public boolean isDefaultSender() {
		return defaultSender;
	}
	
	
	/**
	 * @param defaultSender
	 */
	public void setDefaultSender(boolean defaultSender) {
		this.defaultSender = defaultSender;
	}
	
	/**
	 * @return
	 */
	public boolean isDefaultSMSSender() {
		return defaultSMSSender;
	}

	/**
	 * @param defaultSMSSender
	 */
	public void setDefaultSMSSender(boolean defaultSMSSender) {
		this.defaultSMSSender = defaultSMSSender;
	}


	/**
	 * @return the schedulerEnabled
	 */
	public boolean isSchedulerEnabled() {
		return schedulerEnabled;
	}


	/**
	 * @param schedulerEnabled the schedulerEnabled to set
	 */
	public void setSchedulerEnabled(boolean schedulerEnabled) {
		this.schedulerEnabled = schedulerEnabled;
	}


	/**
	 * @return
	 */
	public int getNbOfWorkerPerScheduler() {
		return nbOfWorkerPerScheduler;
	}


	/**
	 * @param nbOfWorkerPerScheduler
	 */
	public void setNbOfWorkerPerScheduler(int nbOfWorkerPerScheduler) {
		this.nbOfWorkerPerScheduler = nbOfWorkerPerScheduler;
	}


	/**
	 * @return
	 */
	public int getPrefetchLimit() {
		return prefetchLimit;
	}


	/**
	 * @param prefetchLimit
	 */
	public void setPrefetchLimit(int prefetchLimit) {
		this.prefetchLimit = prefetchLimit;
	}
	
	/**
	 * check if Failover topology should be applied
	 * @note : Never create a setter for this attribute , since 
	 * it should remain encapsulate.
	 * @return
	 */
	public boolean isFailover() {
		return failover;
	}


	/**
	 * @return
	 */
	public String getBrokerUrlsList() {
		return brokerUrlsList;
	}


	/**
	 * @param brokerUrlsList
	 */
	public void setBrokerUrlsList(String brokerUrlsList) {
		this.brokerUrlsList = brokerUrlsList;
	}


	/**
	 * @return
	 */
	public String getBrokerProtocol() {
		return brokerProtocol;
	}


	/**
	 * @param brokerProtocol
	 */
	public void setBrokerProtocol(String brokerProtocol) {
		this.brokerProtocol = brokerProtocol;
	}


	public String getSharedFileSystemPath() {
		return sharedFileSystemPath;
	}


	public void setSharedFileSystemPath(String sharedFileSystemPath) {
		this.sharedFileSystemPath = sharedFileSystemPath;
	}


	public String getBrokerUrl() {
		return brokerUrl;
	}


	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}


	public int getInstantNbOfConsumer() {
		return instantNbOfConsumer;
	}


	public void setInstantNbOfConsumer(int instantNbOfConsumer) {
		this.instantNbOfConsumer = instantNbOfConsumer;
	}


	public int getBulkNbOfConsumer() {
		return bulkNbOfConsumer;
	}


	public void setBulkNbOfConsumer(int bulkNbOfConsumer) {
		this.bulkNbOfConsumer = bulkNbOfConsumer;
	}

	/**
	 * @return the bulkNbOfWorker
	 */
	public int getBulkNbOfWorker() {
		return bulkNbOfWorker;
	}


	/**
	 * @param bulkNbOfWorker the bulkNbOfWorker to set
	 */
	public void setBulkNbOfWorker(int bulkNbOfWorker) {
		this.bulkNbOfWorker = bulkNbOfWorker;
	}
	

	public int getInstantMaxNbOfSession() {
		return instantMaxNbOfSession;
	}


	public void setInstantMaxNbOfSession(int instantMaxNbOfSession) {
		this.instantMaxNbOfSession = instantMaxNbOfSession;
	}


	public int getBulkMaxNbOfSession() {
		return bulkMaxNbOfSession;
	}


	/**
	 * @param bulkMaxNbOfSession
	 */
	public void setBulkMaxNbOfSession(int bulkMaxNbOfSession) {
		this.bulkMaxNbOfSession = bulkMaxNbOfSession;
	}


	/**
	 * @return the blukQReaderPeriod
	 */
	public int getBlukQReaderPeriod() {
		return blukQReaderPeriod;
	}


	/**
	 * @param blukQReaderPeriod the blukQReaderPeriod to set
	 */
	public void setBlukQReaderPeriod(int blukQReaderPeriod) {
		this.blukQReaderPeriod = blukQReaderPeriod;
	}


	/**
	 * @return the instantQReaderPeriod
	 */
	public int getInstantQReaderPeriod() {
		return instantQReaderPeriod;
	}


	/**
	 * @param instantQReaderPeriod the instantQReaderPeriod to set
	 */
	public void setInstantQReaderPeriod(int instantQReaderPeriod) {
		this.instantQReaderPeriod = instantQReaderPeriod;
	}
}
