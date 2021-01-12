package com.path.alert.engine.core.container;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.path.alert.engine.logger.AlertEngLogger;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.path.lib.log.PathFormatter;

public abstract class AbstractMsgContainer implements MessageContainer  {
	
	/**
	 * Container name
	 */
	protected String name;

	/**
	 * 
	 */
	protected final Object producersMonitor = new Object();
	
	/**
	 * Status of the Container
	 */
	protected volatile boolean running;

	/**
	 * 
	 */
	protected String brokerUrl;
	
	/**
	 * Destination
	 */
	protected String destination;
	
	/**
	 * 
	 */
	protected Connection sharedConnection;
	
	/**
	 * 
	 */
	private CachingConnectionFactory cachedconnectionFactory;
	
	/**
	 * 
	 */
	private int maxConcurrentSession = 1;
	
	/**
	 * 
	 */
	protected JmsTemplate jmsTemplate;
	
	/**
	 * Hold logger reference
	 */
	protected Log logger = Log.getInstance();
	
	
	
	public AbstractMsgContainer(String containerName) {
		this.name = containerName;
	}
	
	@Override
	public void start() throws Exception {
		
	    	logger.debug(formatLog("started"));
		
		// initialize the connection factory
		initConnectionFactory();
		
		// set Jms template
		jmsTemplate = new JmsTemplate(cachedconnectionFactory);
		jmsTemplate.setDefaultDestinationName(destination);
		jmsTemplate.setDeliveryPersistent(false);
		// mark as running
		running = true;
		
	}
	
	/**
	 * Send list of AlertNtfCO messages to the JMS
	 */
	@SuppressWarnings("rawtypes")
	public boolean sendMsg(List<AlertNtfCO> listOfNtf) {
		
		synchronized (producersMonitor) { 
			if(!running) {
				return false;
			}
			for(AlertNtfCO ntfCO :  listOfNtf ){
			    
			    try{
				jmsTemplate.convertAndSend(ntfCO);
			    }catch(Exception e){
				
				// failure of one message doesn't mean the fail of all
				ntfCO.getMsgVO().setSTATUS(AlertEngineConstants.STATUS_FAILED_MSG);
				ntfCO.getMsgVO().setOUTPUT_MSG("Exception Found while Sending message to the AMQ =>" 
						+ StringUtil.substring(PathFormatter.toString(e, false), 0 , 3900));
				AlertEngLogger.getInstance().updateMsgStatus(ntfCO.getMsgVO());
				
				logger.error(e,formatLog("[MessageID]["+ntfCO.getMsgVO().getMSG_ID()
					+"] Failed to send message to the AMQ"));
			    }
			}
		}
		
		return true;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean sendMsg(AlertNtfCO ntf) {
	    List<AlertNtfCO> listOfNtf = new ArrayList<AlertNtfCO>();
	    listOfNtf.add(ntf);
	    return sendMsg(listOfNtf);
	}
	
	@Override
	public void shutDownNow() {
		// drain the pool
		synchronized (producersMonitor) { 
			
			if(!running) {
				return;
			}
			// @todo later test if this close the producer
			// and the sessins bla bla ..
			cachedconnectionFactory.destroy();
			
			// mark as stopped
			running = false;
			
		}
	}
	
	
	/**
	 * Initialize the connection factory
	 */
	protected void initConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		//connectionFactory.setProducerWindowSize(90110416);
		cachedconnectionFactory = new CachingConnectionFactory(connectionFactory);
		cachedconnectionFactory.setCacheConsumers(false);
		cachedconnectionFactory.setSessionCacheSize(this.getMaxConcurrentSession());
		
		
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
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return
	 */
	public String getBrokerUrl() {
		return brokerUrl;
	}


	/**
	 * @param localBorkerUrl
	 */
	public void setBrokerUrl(String localBorkerUrl) {
		brokerUrl = localBorkerUrl;
	}
	
	/**
	 * @return the maxConcurrentSession
	 */
	public int getMaxConcurrentSession() {
		return maxConcurrentSession;
	}

	/**
	 * @param maxConcurrentSession the maxConcurrentSession to set
	 */
	public void setMaxConcurrentSession(int maxConcurrentSession) {
		this.maxConcurrentSession = maxConcurrentSession;
	}
	
	/**
	 * Custom logging function
	 * @param alrt_ENG_REQUESTVO 
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertProducerContainer] ")
			.append(this.name)
			.append(message);
		
		return sb.toString();
	}
}
