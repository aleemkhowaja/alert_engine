package com.path.alert.engine.broker;

import java.io.File;

import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.springframework.util.Assert;
import com.path.alert.engine.core.AlertEngine;
import com.path.bo.reporting.ReportingConstantsCommon;
import com.path.lib.log.Log;
import com.path.lib.common.util.FileUtil;
import com.path.lib.common.util.StringUtil;
/**
 * This class is the house of AMQ Broker behavior 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AlertAMQBroker implements AlertBroker{
	
	/**
	 * @todo keep in mind that this instance
	 * maybe will not be populated in cluster env
	 */
	private BrokerService broker;
	
	/**
	 * Hold the current broker url
	 */
	private String connectorUrl;
	
	/**
	 * Hold engine reference
	 */
	private AlertEngine engine;
	
	/**
	 * failover flag, If enabled the engine will start in Master/slave topology
	 */
	private boolean failover;
	
	/**
	 * sharedFileSystem hold the path of the shared file system
	 */
	private String sharedFileSystemPath;
	
	/**
	 * Hold logger reference
	 */
	private final Log logger = Log.getInstance();
	
	/**
	 * This method will start the amq broker
	 */
	public void start() throws Exception {
		
		/**
		 * @note in case we will start this
		 * broker directly we may need to sync
		 * start and stop ( between nodes, clusters)
		 * @note : above not isn't valid anymore, will be removed on code
		 * cleanup
		 */
		if(isBrokerAlive()){
			logger.debug(formatLog("Broker is Alive on Another node"));
			return;
		}
		
		broker = new BrokerService();
		broker.setBrokerName("AlertEngBroker");
		//@todo check if this the best practice
		broker.addConnector(getConnectorUrl());
		
		//@todo check this
		broker.setAdvisorySupport(false);
		
		// @todo issue occur when true
		broker.setPersistent(false);
		broker.setUseJmx(true);
		//broker.getPersistenceAdapter();
		// set configuration here
		
		logger.debug(formatLog("Before Broker started"));
		
		if(!failover){
			broker.start();
		}else{
			KahaDBPersistenceAdapter kahaStore = new KahaDBPersistenceAdapter();
			broker.setPersistenceAdapter(kahaStore);
			broker.setDataDirectory(FileUtil.getFileURLByName(ReportingConstantsCommon.repositoryFolder)
					.concat(File.separator).concat(StringUtil.nullEmptyToValue(sharedFileSystemPath,"alertEng")));
			// enable persistence on broker level is omitted at producer level
			broker.setPersistent(true);
			asynchronizeStart(broker);
		}
		
		logger.debug(formatLog("Broker started"));	
	}
	
	
	/**
	 * This method will shutdown the broker
	 */
	public void shutdown() {

		try {
			// broker can be null in case of testing ;)
			logger.debug(formatLog("Shutdown Broker"));
			if (broker != null) {
				broker.stop();
			}
		}catch (Exception e) {
			logger.error(e,formatLog("failure while shutting down the broker"));
		}
	}
	
	
	/**
	 * Ping the local broker to check if connection still alive
	 * @param brokerURL
	 * @return
	 * @throws Exception 
	 */
	public boolean isBrokerAlive() throws Exception {
		return isBrokerAlive(getConnectorUrl());
	}


	/**
	 * Ping broker to check if connection still alive
	 * @param brokerURL
	 * @return
	 */
	public boolean isBrokerAlive(String brokerUrl) {
		
		Assert.hasLength(brokerUrl,
				"[isBrokerAlive] Broker Url must not be null or empty");
		
		Connection connection = null;
		ActiveMQConnectionFactory factory = null;
		boolean isAlive = false;
		
		try {
			factory = new ActiveMQConnectionFactory(brokerUrl);
			//factory.setProducerWindowSize(90110416);
			connection = factory.createConnection();
			connection.close();
			isAlive = true;
		} catch (/* @todo should we use JMSException*/ Exception ex) {
			isAlive = false;
		} finally {
			
			// @todo do we need to nullify ??
			connection = null;
			factory = null;
		}
		
		return isAlive;
	}
	
	
	/**
	 * This method return all information about A broker as string
	 * @return
	 */
	public String toString() {
		return String.format("[connection] [%s] , Failer over: [%s], sharedFileSystemPath: [%s]", 
				getConnectorUrl(), isFailover(), getSharedFileSystemPath());
	}
	
	
	/**
	 * Start the broker in asynchronize mode
	 * @param broker 
	 */
	private void asynchronizeStart(BrokerService broker){
		
		Thread t = new Thread() {
		    public void run() {
		    	
		    	try {
		    		logger.debug(formatLog("Start Broker in asynchronize mode"));
					broker.start();
				} catch (Exception e) {
					logger.error(e, formatLog("Start Broker in asynchronize mode"));
				}
		    }
		};
		
		t.start();
	}
	
	
	/**
	 * Return Alert engine reference
	 * @return
	 */
	public AlertEngine getEngine() {
		return engine;
	}


	/**
	 * Set the alert engine reference
	 * @param engine
	 */
	public void setEngine(AlertEngine engine) {
		this.engine = engine;
	}


	/**
	 * @return
	 */
	public String getConnectorUrl() {
		return connectorUrl;
	}


	/**
	 * @param connectorUrl
	 */
	public void setConnectorUrl(String connectorUrl) {
		this.connectorUrl = connectorUrl;
	}


	public boolean isFailover() {
		return failover;
	}


	/**
	 * @param failover
	 */
	public void setFailover(boolean failover) {
		this.failover = failover;
	}
	
	
	public String getSharedFileSystemPath() {
		return sharedFileSystemPath;
	}


	public void setSharedFileSystemPath(String sharedFileSystemPath) {
		this.sharedFileSystemPath = sharedFileSystemPath;
	}

	
	/**
	 * Custom logging function
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][AlertBroker] ")
			.append(this)
			.append(message);
		
		return sb.toString();
	}
	
}
