package com.path.alert.engine.core;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.path.alert.engine.scheduler.Scheduler;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.engine.utils.AlertEngineStatus;
import com.path.alert.engine.utils.StopWatch;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.log.Log;
import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.engine.broker.AlertAMQBroker;
import com.path.alert.engine.core.container.AlertBulkMsgContainer;
import com.path.alert.engine.core.container.AlertConsumerContainer;
import com.path.alert.engine.core.container.AlertInstantMsgContainer;
import com.path.alert.engine.core.task.AlertRequestTask;
import com.path.alert.engine.reader.AlertBulkQueueReader;
import com.path.alert.engine.reader.AlertInstantQueueReader;
import com.path.struts2.lib.common.ConnectionCO;

/**
 * This will hold the engine core
 * @author Mohammad Ali Mezzawi
 * 
 * @todo this should inherit from BaseBO
 */
public class AlertEngine {
	
	/**
	 * Hold monitor object
	 */
	private static Object mutex = new Object();
	
	/**
	 * Alert Engine instance
	 */
	private static AlertEngine _instance;
	
	/**
	 * Engine status
	 */
	private volatile AlertEngineStatus status;
	
	/**
	 * Engine Configuration
	 */
	private AlertEngConfig configuration;
	
	/**
	 * Scheduler
	 */
	private Scheduler scheduler;
	
	/**
	 * broker
	 */
	private AlertAMQBroker broker;
	
	/**
	 * HashMap of Consumer Containers
	 */
	private HashMap<String,AlertConsumerContainer> consumerContainers;
	
	/**
	 * Instant Msg Request handler container.
	 */
	private AlertInstantMsgContainer instantMsgContainer;
	
	/**
	 * Bulk Msg Request handler container.
	 */
	private AlertBulkMsgContainer bulkMsgContainer;
	
	/**
	 * 
	 */
	private final Log logger = Log.getInstance();
	
	
	/**
	 * Connection co for core db
	 */
	private ConnectionCO connCO;
	
	
	/**
	 * constructor
	 */
	public static AlertEngine getInstance() {
		
		if (_instance == null) {
			
			//@todo getMutex change to static ??
			synchronized (mutex) {
				if (_instance == null) {
					_instance = new AlertEngine();
				}
			}
		}

		return _instance;
	}
	
	
	/**
	 * constructor
	 */
	private AlertEngine(){
		// nothing to implement for now
	}
	
	
	/**
	 * Start the engine
	 */
	public void start()
	{
		/**
		 * Alert Engine and Alert Application are considered as one module,
		 * which mean whenever alert application is deployed, Alert engine should 
		 * be ON by default.
		 */
		try{
			synchronized (mutex) {
				
				logger.debug("[AlertEngine] Check if we should Run engine on this Node");
				
				// check Property file if we should run the engine
				// @todo check if engine already started and return
				if(!configuration.isEnabled()){
					logger.debug("[AlertEngine] Engine is stopped ???!!!");
					return;
				}
				
				// if engine is already started or is currently in breaking stage then do nothing
				if( isRunning() || isShutdowning()) {
					return;
				}
				// @todo control racing condition if multiple engine are starting
				// no need to control racing since engine will start manually ???!!!
				logger.debug("[AlertEngine] init Engine");
				setStatus(AlertEngineStatus.RUNNING);
				
				// init the engine needed data
				init();
				
				// start the broker
				kickoffBroker();
				
				// start Executor
				kickoffConsumerContainer();
				
				// start Instant Request Handler container
				kickoffInstantlyMsgContainer();
				
				// start Bulk Request Handler container
				kickoffBulkMsgContainer();
				
				// start heart beats
				startScheduledTasks();
				
			}
		
		}catch (Exception e) {
			
			/**
			 * in case of exception we should always try to shutdown
			 * the Engine smoothly
			 */
			//@todo make shutdown safe
			shutdown();
			
			logger.error(e,"[AlertEngine] Error while starting Engine");
			// failed to start the alert engine
		}
		
	}

	/**
	 * 
	 */
	private void init() {
		
		/**
		 * Make sure to clear Alert eng tag data whenever we
		 * start the engine
		 */
		AlertEngTagProvider.getInstance().clearData();
		
	}


	/**
	 * This method will shutdown the engine ( the core and sub components )
	 * @todo Should we implement shutdown now ???
	 */
	public void shutdown()
	{
		synchronized (mutex) {
			try {
				/**
				 * @note : before shutting down check if
				 * the system was kicked off successfully
				 * 
				 * @todo implement clustering
				 */
				if( !isRunning() ) {
					return;
				}
				logger.debug("[AlertEngine] Shutdown Engine");
				setStatus(AlertEngineStatus.SHUTDOWNING);
	
				// stop scheduler
				if(getScheduler() != null) {
					getScheduler().shutdown();
				}
				// stop consumer container
				if(getInstantMsgContainer() != null) {
					getInstantMsgContainer().shutDownNow();
				}
				// shutdown bulk msg container
				if(getBulkMsgContainer() != null) {
					getBulkMsgContainer().shutDownNow();
				}
				
				
				//shutdown list of consumers
//				if(getConsumerContainers() != null &&				
//						getConsumerContainers().size() > 0){
				if(getConsumerContainers() != null && 
						( ! getConsumerContainers().isEmpty() )){
					
					for(Entry<String, AlertConsumerContainer> entry 
							: getConsumerContainers().entrySet() ) {
						 //String key = entry.getKey();
						 entry.getValue().shutDownNow();	
					}
				}
	
				// force interrupt all thread
				if(getBroker() != null) {
					getBroker().shutdown();
				}
				setStatus(AlertEngineStatus.SHUTDOWN);
				
			} catch (Exception e) {
				logger.error(e,"[AlertEngine] Error while Shutting down the Engine");
			}
		}
	}
	
	
	/**
	 * This method will send a message by creating a message task
	 * which will handled asynch.
	 * 
	 * @todo do we need persistence
	 * @param notificationCO
	 */
	public boolean sendMessage(AlertNotificationCO notificationCO, boolean waitResponse )
	{
		boolean result = false;
		
		// create stop watch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("Engine.sendMessage", "Start sending Message");
		
		// start processing the request
		AlertRequestTask taskProcessor = new AlertRequestTask();
		taskProcessor.setSynchMode(waitResponse);
		taskProcessor.setNotificationCO(notificationCO);
		result = taskProcessor.processRequest();
		
		// Stop stopWatch
		stopWatch.stop("Engine.sendMessage", "Done sending Message");
		return result;
	}


	/**
	 * This method will send a message by creating a message task
	 * which will handled asynch.
	 * 
	 * @todo do we need persistence
	 * @param notificationCO
	 */
	public boolean sendMessage(AlertNotificationCO notificationCO)
	{
		// @todo fix this later
		return sendMessage(notificationCO, true);
	}


	/**
	 * check if engine is already running
	 * @return
	 */
	public boolean isRunning() {
		return getStatus() == AlertEngineStatus.RUNNING;
	}

	
	/**
	 * Check if engine is currently in breaking phase
	 * @return
	 */
	public boolean isShutdowning() {
		return getStatus() == AlertEngineStatus.SHUTDOWNING;
	}
	

	
	/**
	 * This method will kickoff the instantly msg request container
	 * which will handle the translation of the request to JMS Message
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	private void kickoffInstantlyMsgContainer() throws Exception {
		/**
		 * Getting Need properties from the properties file
		 * 	1- prefetch limit : number of message prefetch by consumer
		 * 	2- pooled connections : number of maximum connection created in the pool
		 */
		
		logger.debug("[AlertEngine] kickoff Request container");
		
		instantMsgContainer = new AlertInstantMsgContainer("InstantMsgHandler");
		instantMsgContainer.setBrokerUrl(configuration.returnBrokerUrl());
		instantMsgContainer.setDestination(AlertEngineConstants.INSTANT_MSG_DESTINATION);
		instantMsgContainer.setMaxConcurrentSession(configuration.getInstantMaxNbOfSession());
		// ----------- @todo configure number of cached session 
		// ----------- @todo check producer id --------
		instantMsgContainer.start();
		
	}
	
	
	/**
	 * This method will kickoff the Bulk msg request container
	 * which will handle the translation of the request to JMS Message
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	private void kickoffBulkMsgContainer() throws Exception {
		/**
		 * Getting Need properties from the properties file
		 * 	1- prefetch limit : number of message prefetch by consumer
		 * 	2- pooled connections : number of maximum connection created in the pool
		 */
		
		logger.debug("[AlertEngine] kickoff Bulk Request container");
		
		bulkMsgContainer = new AlertBulkMsgContainer("BulkMsgHandler");
		bulkMsgContainer.setBrokerUrl(configuration.returnBrokerUrl());
		bulkMsgContainer.setDestination(AlertEngineConstants.BULK_MSG_DESTINATION);
		bulkMsgContainer.setMaxConcurrentSession(configuration.getBulkMaxNbOfSession());
		// ----------- @todo configure number of cached session 
		// ----------- @todo check producer id --------
		bulkMsgContainer.start();
		
	}
	
	/**
	 * Create the Jms borker and Kickoff it
	 * @throws Exception
	 */
	private void kickoffBroker() throws Exception {
		
		/**
		 * @note all configuration should be set
		 * at the level of the broker class
		 * @note Engine shouldn't know anything about
		 * the initialization of the AMQbroker
		 */
		logger.debug("[AlertEngine] Try to kickoff Broker");
		String borkerUrls = PathPropertyUtil.returnPathPropertyFromFile(
				"PathAlertEngineRemoting","alertEngine.borkerUrl");
		
		if(borkerUrls.trim().isEmpty()) {
			throw new Exception("[AlertEngine] kickoff Broker => No Broker Url defined");
		}
		broker = new AlertAMQBroker();
		broker.setEngine(this);
		broker.setConnectorUrl(configuration.returnBrokerConnector());
		broker.setSharedFileSystemPath(configuration.getSharedFileSystemPath());
		broker.setFailover(configuration.isFailover());
		broker.start();
	}


	/**
	 * Kickoff consumers container which 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private void kickoffConsumerContainer() throws Exception
	{
		logger.debug("[AlertEngine] kickoff Consumer [ Instant / Bulk ]");
		
		// initialize the consumer containers hash
		consumerContainers = new HashMap<String,AlertConsumerContainer>();
		
		// Kickoff the online consumers
		kickOffInstantMsgConsumers();
		
		// kick off the offline consumers
		kickoffBulkMsgConsumers();
		
	}

	
	/**
	 * Kickoff Bulk Msg consumers
	 * @throws Exception 
	 */
	private void kickoffBulkMsgConsumers() throws Exception {
		
		logger.debug("[AlertEngine] KickOff Bulk Msg consumers");
		// instantiate Alert consumers container
		AlertConsumerContainer bulkMsgConsContainer = new AlertConsumerContainer("offlineConsumer");
		
		// configure the consumers Container
		bulkMsgConsContainer.setBrokerUrl(configuration.returnBrokerUrl());
		bulkMsgConsContainer.setPrefetchLimit(configuration.getPrefetchLimit());
		bulkMsgConsContainer.setConcurrentConsumers(configuration.getBulkNbOfConsumer());
		bulkMsgConsContainer.setDestinationName(AlertEngineConstants.BULK_MSG_DESTINATION);
		
		// Start the consumer container
		bulkMsgConsContainer.setEngine(this);
		bulkMsgConsContainer.start();
		
		consumerContainers.put(AlertEngineConstants.BULK_MSG_CONSUMER_KEY,bulkMsgConsContainer);
	}


	/**
	 * Kickoff Instant Msg consumers
	 * @throws Exception 
	 */
	private void kickOffInstantMsgConsumers() throws Exception
	{
		logger.debug("[AlertEngine] KickOff Instant Msg consumers");
		// instantiate Alert consumers container
		AlertConsumerContainer instantMsgConsContainer = new AlertConsumerContainer("InstantConsumerContainer");
		
		// configure the consumers Container
		instantMsgConsContainer.setBrokerUrl(configuration.returnBrokerUrl());
		instantMsgConsContainer.setPrefetchLimit(configuration.getPrefetchLimit());
		instantMsgConsContainer.setConcurrentConsumers(configuration.getInstantNbOfConsumer());
		instantMsgConsContainer.setDestinationName(AlertEngineConstants.INSTANT_MSG_DESTINATION);
		
		// Start the consumer container
		instantMsgConsContainer.setEngine(this);
		instantMsgConsContainer.start();
		
		// push it to the list of consumers container
		consumerContainers.put(AlertEngineConstants.INSTANT_MSG_CONSUMER_KEY,instantMsgConsContainer);
	}
	
	
	/**
	 * This method will run the engine scheduler
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private void startScheduledTasks()
	{
		if(!configuration.isSchedulerEnabled()){
			logger.debug("[AlertEngine] Scheduler is disabled");
			return;
		}
		
		logger.debug("[AlertEngine] Start Scheduler");
		
		// set scheduler
		Scheduler scheduler = new Scheduler("ALERT_ENGINE_SCHEDULER");
		scheduler.setNbofWorkers(configuration.getNbOfWorkerPerScheduler());
		setScheduler(scheduler);
		
	    FixedEventBO fixedEventBO = (FixedEventBO) ApplicationContextProvider.getApplicationContext()
		.getBean("fixedEventBO");
	    
		// i hate this approach :'(
		// Create Bulk Queue reader
		AlertBulkQueueReader queueBulkReader = new AlertBulkQueueReader();
		queueBulkReader.setEngine(this);
		queueBulkReader.setFixedEventBO(fixedEventBO);
		
		// fixed Rate in Scheduler grant that no concurrent task are executed
		getScheduler().scheduleAtFixedRate(queueBulkReader, 0, configuration.getBlukQReaderPeriod(),
				TimeUnit.SECONDS, AlertEngineConstants.ALERT_QUEUE_SCHEDULER_KEY);
		
		logger.debug(String.format("[AlertEngine]Scheduler Bulk Queue started at fixed rate  %d seconds",
				configuration.getBlukQReaderPeriod()));
		
		// Create Instant Queue reader
		AlertInstantQueueReader queueInstantReader = new AlertInstantQueueReader();
		queueInstantReader.setEngine(this);
		queueInstantReader.setFixedEventBO(fixedEventBO);
		
		getScheduler().scheduleAtFixedRate(queueInstantReader, 0, configuration.getInstantQReaderPeriod(),
				TimeUnit.SECONDS, AlertEngineConstants.ALERT_QUEUE_SCHEDULER_KEY);
		
		logger.debug(String.format("[AlertEngine] Scheduler Instant Queue started at fixed rate  %d seconds",
				configuration.getInstantQReaderPeriod()));
		
	}


	/**
	 * 
	 * @return
	 */
	public AlertEngineStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(AlertEngineStatus status) {
		this.status = status;
	}


	/**
	 * @return
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}


	/**
	 * @param scheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}


	/**
	 * @return
	 */
	public AlertAMQBroker getBroker() {
		return broker;
	}


	/**
	 * @param broker
	 */
	public void setBroker(AlertAMQBroker broker) {
		this.broker = broker;
	}

	
	/**
	 * @return
	 */
	public HashMap<String,AlertConsumerContainer> getConsumerContainers() {
		return consumerContainers;
	}


	/**
	 * @param consumerContainers
	 */
	public void setConsumerContainers(HashMap<String,AlertConsumerContainer> consumerContainers) {
		this.consumerContainers = consumerContainers;
	}


	/**
	 * @return the instantMsgContainer
	 */
	public AlertInstantMsgContainer getInstantMsgContainer() {
		return instantMsgContainer;
	}


	/**
	 * @param instantMsgContainer the instantMsgContainer to set
	 */
	public void setInstantMsgContainer(AlertInstantMsgContainer instantMsgContainer) {
		this.instantMsgContainer = instantMsgContainer;
	}


	/**
	 * @return the bulkMsgContainer
	 */
	public AlertBulkMsgContainer getBulkMsgContainer() {
		return bulkMsgContainer;
	}


	/**
	 * @param bulkMsgContainer the bulkMsgContainer to set
	 */
	public void setBulkMsgContainer(AlertBulkMsgContainer bulkMsgContainer) {
		this.bulkMsgContainer = bulkMsgContainer;
	}


	/**
	 * @return the configuration
	 */
	public AlertEngConfig getConfiguration() {
		return configuration;
	}


	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(AlertEngConfig configuration) {
		this.configuration = configuration;
	}


	/**
	 * @return the connCO
	 */
	public ConnectionCO getConnCO()
	{
	    return connCO;
	}


	/**
	 * @param connCO the connCO to set
	 */
	public void setConnCO(ConnectionCO connCO)
	{
	    this.connCO = connCO;
	}
	
	
	
}
