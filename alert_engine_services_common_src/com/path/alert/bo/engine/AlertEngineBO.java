package com.path.alert.bo.engine;

import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.lib.common.exception.BaseException;

/**
 * This will hold the engine core
 * @author Mohammad Ali Mezzawi
 * 
 * @Note
 * This interface should only contain
 * 1 - Start
 * 2 - Stop
 * 3 - Send Message 
 * Any object used here should be in Alert common area
 * since this bo is linked to alert services
 */
public interface AlertEngineBO {
	
	/**
	 * Start the engine
	 * @throws BaseException
	 */
	public void start() throws BaseException;
	
	/**
	 * Start the engine
	 * @throws BaseException 
	 */
	public void start(boolean forceStart) throws BaseException;
	
	/**
	 * Shutdown the engine
	 * @throws BaseException 
	 */
	public void shutdown() throws BaseException;
	
	/**
	 * Shutdown the engine
	 * @throws BaseException 
	 */
	public String returnEngineStatus() throws BaseException;
	
	/**
	 * @param notificationCO
	 * @throws BaseException
	 */
	public boolean sendMessage(AlertNotificationCO notificationCO) throws BaseException;
}
