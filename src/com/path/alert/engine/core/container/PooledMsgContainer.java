package com.path.alert.engine.core.container;

import java.util.List;

import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.core.pool.ProducerThreadPoolExecutor;
import com.path.alert.engine.core.task.AlertRequestTasklet;

abstract public class PooledMsgContainer extends AbstractMsgContainer {
	
	/**
	 * Thread pool executor 
	 */
	private ProducerThreadPoolExecutor threadPool;
	
	/**
	 * @param containerName
	 */
	public PooledMsgContainer(String containerName) {
		super(containerName);
//		threadPool = ProducerThreadPoolExecutor.
//				newFixedThreadPool(getConcurrentProducers(), getMaxConcurrentProducers(),name+"-ProducerPool",
//						getSessions(),getProducers());
		int nbOfWorker = AlertEngine.getInstance().getConfiguration().getBulkNbOfWorker();
		threadPool = ProducerThreadPoolExecutor.newFixedThreadPool(nbOfWorker, nbOfWorker);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean submit(List<AlertRequestTasklet> requestTasklet) {
		boolean result = true;
		
		for(AlertRequestTasklet tasklet: requestTasklet){
			result &= submit(tasklet);
		}
		
		return result;
	}
	
	/**
	 * Submit task to the task pool to be executed by the worker
	 * @param MessageProducerTask task
	 */
	public <V> boolean submit(AlertRequestTasklet<V> requestTasklet) {
		
		synchronized (producersMonitor) {
			// set container info
			requestTasklet.setMessageContainer(this);
			
			if(!threadPool.isShutdown()) {
				threadPool.submit(requestTasklet);
			}
		}
		
		// should we wait ???
		return true;
	}
	
	
	@Override
	public void shutDownNow() {
		
		// super shutdown
		super.shutDownNow();
		
		synchronized (producersMonitor) {
			
			// shutdown tasks
			threadPool.shutdownNow();
			
		}
	}
	
}
