package com.path.alert.engine.core.container;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.util.Assert;

import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.core.listener.AlertMessageListener;
import com.path.alert.engine.core.sender.AlertDefaultSender;
import com.path.lib.log.Log;

/**
 * This class represent the Consumer container.
 * 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AlertConsumerContainer extends SimpleMessageListenerContainer {

	/**
	 * Number of prefetch message per consumer
	 */
	private int prefetchLimit;

	/**
	 * Hold reference to the alert engine
	 */
	private AlertEngine engine;

	/**
	 * 
	 */
	private String brokerUrl;

	/**
	 * Consumer container name
	 */
	private String name;
	
	/**
	 * Hold logger reference
	 */
	private final Log log = Log.getInstance();
	
	/**
	 * @param containerName
	 */
	public AlertConsumerContainer(String containerName ) {
		super();
		this.setName(containerName);
	}


	/* (non-Javadoc)
	 * @see org.springframework.jms.listener.AbstractJmsListeningContainer#start()
	 */
	public void start() {
	
		// if broker url is off
		Assert.hasLength(getBrokerUrl(),"Broker url can't be empty");
			
		// connection factory
		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory(getBrokerUrl());
		
		//connectionFactory.setProducerWindowSize(90110416);
		// apply prefetch policy
		ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
		prefetchPolicy.setQueuePrefetch(prefetchLimit);
		connectionFactory.setPrefetchPolicy(prefetchPolicy);
		
		this.setConnectionFactory(connectionFactory);
		
		// Spring AbstractMessage container will handle the acknowledgement
		this.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		
		// @todo add consumer name to the thread pool
		// initiate the Sender
		AlertDefaultSender sender = new AlertDefaultSender("defaultSender for:"+name);
		applyConfiguration(sender);
		
		// set Message listener
		this.setMessageListener(new AlertMessageListener(sender));
		super.start();
	}


	/**
	 * @note : to check if we need to shutdown then stop
	 * @note: In case we need to apply the Pooled conx factory we should
	 * check the POC
	 */
	public void shutDownNow()
	{
		/**
		 * In case we will implement the sender pool later
		 * check the Engine Phase 1
		 * We should stop the execution of the pool first
		 * to release the consumer listener which is waiting the tasks
		 * if we don't do that the shutdown of the consumer listener container
		 * will be blocked until all the Message consumer tasks are done.
		 */
				
		/**
		 * kill all consumers.
		 * Blocking until all Listener are released
		 */
		try{
			
			log.debug(formatLog("Shutting down is ongoing"));
			
			super.shutdown();
			super.stop();
			
			log.debug(formatLog("Shutdown Consumer container"));
			
		}catch (Exception e) {
			// Usually it will be a runtime exception "JmsException"
			log.error(e,formatLog("Failed to shutdown the broker "));
		}
	}
	
	
	/**
	 * This method return all information about A broker as string
	 * @todo try to add Concurrent Consumer [%d]
	 * @return
	 */
	public String toString() {
		return String.format("[name] [%s] , Broker Url: [%s], prefetchLimit: [%s] ", 
				getName(), getBrokerUrl(), getPrefetchLimit());
	}
	
	/**
	 * This method will apply the sender configuration using Engine configuration
	 * All Configuration should be set at this level
	 * @param sender
	 */
	private void applyConfiguration(AlertDefaultSender sender) {
		
		sender.getConfig().setDefaultSmtp(engine.getConfiguration().isDefaultSender());
		sender.getConfig().setDefaultSms(engine.getConfiguration().isDefaultSMSSender());
	}

	/**
	 * @param alertEngine
	 */
	public void setEngine(AlertEngine alertEngine) {
		this.engine = alertEngine;
		
	}

	
	/**
	 * @return
	 */
	public AlertEngine getEngine() {
		return engine;
	}

	
	public String getBrokerUrl() {
		return brokerUrl;
	}


	/**
	 * @param localBorkerUrl
	 */
	public void setBrokerUrl(String borkerUrl) {
		this.brokerUrl = borkerUrl;
		
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
	 * @return
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Custom logging function
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertConsumerContainer] ")
			.append(this)
			.append(message);
		
		return sb.toString();
	}
	

}
