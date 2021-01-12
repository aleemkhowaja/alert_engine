package com.path.alert.engine.core.container;

import java.util.List;

import com.path.alert.engine.core.task.AlertRequestTasklet;
import com.path.alert.vo.engine.AlertNtfCO;

public interface MessageContainer {
	
	/**
	 * Start the producer container  
	 */
	public void start() throws Exception;
	
	
	/**
	 * Shut the producer container
	 */
	public void shutDownNow();
	
	/**
	 * Submit a tasklet to the container
	 * @param requestTasklet
	 * @return
	 */
	public <V> boolean submit(AlertRequestTasklet<V> requestTasklet);
	
	
	/**
	 * Submit a list of tasklet to the container
	 * @param requestTasklet
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean submit(List<AlertRequestTasklet> requestTasklet);


	/**
	 * Send notification to the JMS
	 * @param ntf
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean sendMsg(AlertNtfCO ntf);


	/**
	 *  Send list of notification to the JMS
	 * @param listOfNtf
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean sendMsg(List<AlertNtfCO> listOfNtf);
}
