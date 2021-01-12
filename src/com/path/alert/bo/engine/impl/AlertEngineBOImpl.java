package com.path.alert.bo.engine.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.util.Assert;

import com.path.alert.bo.engine.AlertEngineBO;
import com.path.alert.dao.engine.AlertEngineDAO;
import com.path.alert.engine.core.AlertEngConfig;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.bo.common.CommonMethods;
import com.path.bo.common.ConstantsCommon;
import com.path.dbmaps.vo.S_APPVO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.BaseSC;
import com.path.struts2.lib.common.ConnectionCO;

/**
 * This will hold the engine core bo
 * @author Mohammad Ali Mezzawi
 * 
 */
public class AlertEngineBOImpl extends BaseBO implements AlertEngineBO{
	
	/**
	 * Hold reference to the engine
	 */
	private AlertEngine engine;
	
	
	/**
	 * hold reference to the Engine DAO
	 */
	private AlertEngineDAO alertEngineDAO;
	
	/**
	 * @throws BaseException
	 */
	public void start() throws BaseException {
		start(false);
	}
	
	
	/**
	 * Start the engine.
	 * Engine will be always initiated on the all servers but
	 * it will be functioning only where flag enabled is set true
	 * Or force started ( dev only )
	 */
	public void start( boolean forceStart) throws BaseException {
	    
		PathPropertyUtil.removeCachedPropFile(AlertEngineConstants.ENGINE_REMOTING_FILE);
		if(engine == null) {
			engine = AlertEngine.getInstance();
		}
		// is it synch ???
		AlertEngConfig configCO = new AlertEngConfig();
		
		// start the engine
		if(forceStart) {
			configCO.setEnabled(true);
		}
		// get engine configuration
		List<HashMap<String, Object>> configs = alertEngineDAO.returnAlertConfig();
		configCO.prepareConfiguration(configs);
		
		// start the engine
		engine.setConfiguration(configCO);
		
		//Prepare the engine core connection
		prepareEngineCoreConnection();
		
		//check is omni istalled in the db in which alert is working
		checkIsOmniInstalled();
		
		engine.start();
	}
	
	/**
	 * check is omni istalled in the db in which alert is working
	 * @throws BaseException
	 */
	private void checkIsOmniInstalled() throws BaseException
	{
	    S_APPVO sAppVO = new S_APPVO();
	    sAppVO.setAPP_NAME(AlertEngineConstants.OMNI_ADMIN_APP_NAME);
	    AlertEngineConstants.IS_OMNI_INSTALLED = commonLibBO.returnApplication(sAppVO) != null;
	}


	/**
	 *  Prepare the engine core connection
	 * @throws BaseException
	 */
	private void prepareEngineCoreConnection() throws BaseException
	{
	    /**
	     * Prepare the engine core connection.
	     * This connection will be used to query core tables
	     */
	    if(StringUtil.nullToEmpty(commonLibBO.returnPthCtrl1().getCORE_IMAL_YN())
		    .equals(ConstantsCommon.NO))
	    {
		BaseSC sc = new BaseSC();
		String jndiname = "services.jndi";
		sc.setUseConnection(Boolean.TRUE);
		CommonMethods.applyConnectionJNDIToSC(sc, jndiname);
		ConnectionCO conx = sc.getConnCO();
		engine.setConnCO(conx);
	    }
	}
	
	/**
	 * Shutdown the engine
	 */
	public void shutdown() throws BaseException {
		
		if(engine == null) {
			throw new BOException("Engine is not initiated");
		}
		PathPropertyUtil.removeCachedPropFile(AlertEngineConstants.ENGINE_REMOTING_FILE);
		// is it synch ??
		engine.shutdown();
	}
	
	
	/**
	 * @param notificationCO
	 */
	public boolean sendMessage(AlertNotificationCO notificationCO) throws BaseException {
		// safety
		Assert.notNull(engine, "Engine isn't initialized");
		
		// send message
		return engine.sendMessage(notificationCO);
	}
	

	@Override
	public String returnEngineStatus() throws BaseException {
		return engine.getStatus().getCode();
	}
	
	/**
	 * @return the alertEngineDAO
	 */
	public AlertEngineDAO getAlertEngineDAO() {
		return alertEngineDAO;
	}


	/**
	 * @param alertEngineDAO the alertEngineDAO to set
	 */
	public void setAlertEngineDAO(AlertEngineDAO alertEngineDAO) {
		this.alertEngineDAO = alertEngineDAO;
	}
	

	/**
	 * @return
	 */
	public AlertEngine getEngine() {
		return engine;
	}

	
	/**
	 * @param engine
	 */
	public void setEngine(AlertEngine engine) {
		this.engine = engine;
	}

}
