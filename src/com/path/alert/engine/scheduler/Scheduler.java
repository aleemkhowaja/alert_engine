package com.path.alert.engine.scheduler;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.path.lib.log.Log;

/**
 * This class Serve as the core of the scheduling engine
 * 
 * @author Mohammad Ali Mezzawi
 */
public final class Scheduler {
	
	/**
	 * Scheduler name
	 */
	private final String name;
	
	
	/**
	 * Hold the number of worker per scheduler instance
	 */
	private int nbofWorkers;
	
	/**
	 * List of scheduledExecutor
	 */
	private HashMap<String,ScheduledThreadPoolExecutor> scheduledExecutors = new 
		HashMap<String, ScheduledThreadPoolExecutor>();
	
	/**
	 * Hold logger reference
	 */
	private final Log logger = Log.getInstance();
	
	/**
	 * List of scheduled tasks
	 */
	@SuppressWarnings("rawtypes")
	private final HashMap<Runnable, ScheduledFuture> timerTasks = new HashMap<Runnable,
			ScheduledFuture>();
	
	/**
	 * @constructor
	 * @param name
	 */
	public Scheduler(String name) {
		this.name = name;
		logger.debug(formatLog("Create Scheduler "));
	}

	
	/**
	 * @todo check if we need a case executor name isn't given
	 * @param executorName
	 * @return
	 */
	private ScheduledExecutorService getScheduledExecutorService(String executorName)
	{
		if(scheduledExecutors.get(executorName) != null) {
			return scheduledExecutors.get(executorName);
		}
		
		
		ScheduledThreadPoolExecutor serviceExecutor = (ScheduledThreadPoolExecutor) 
				Executors.newScheduledThreadPool(nbofWorkers);
		serviceExecutor.setRemoveOnCancelPolicy(true);
		scheduledExecutors.put(executorName,serviceExecutor);
		
		return serviceExecutor;
	}
	
	
	/**
	 * @todo Do we need to synchronize it?,
	 * shouldn't we wait for the termination any hanging execution
	 */
	public void scheduleAtFixedRate(Runnable command,long initialDelay,
            long period,TimeUnit unit, String executorName){
		
		ScheduledExecutorService executor = getScheduledExecutorService(executorName);
		
		// Scheduler fixed rate has different behavior than Time at fixed rate
		ScheduledFuture<?> timedTask = executor.scheduleAtFixedRate(command,
				initialDelay, period, unit);
		timerTasks.put(command, timedTask);
		logger.debug(formatLog(String.format("schedule At FixedRate[%s] [%s]",period,unit )));
	}
	
	
    /**
     * This method will shutdown the Scheduler
     */
    public void shutdown() {
        // loop over all executor and shutdown them
//    	if(scheduledExecutors == null || scheduledExecutors.size() <= 0)
    	if(scheduledExecutors == null || scheduledExecutors.isEmpty()) {
    		return;
    	}
    	// clear timer tasks
    	timerTasks.clear();
    	
    	// shutdown executor
    	for(Entry<String, ScheduledThreadPoolExecutor> entry 
				: scheduledExecutors.entrySet() ){
			//@todo handle the cancel exception in scheduler
    		logger.debug(formatLog(String.format("Shutdown Scheduler pool [%s]",entry.getKey())));
    		
			 ScheduledExecutorService scheduler = entry.getValue();
			 scheduler.shutdownNow();    
		}
    	
    	// clear executor
    	scheduledExecutors.clear();
    	logger.debug(formatLog("Shutdown Scheduler"));
    }
    


	/**
	 * @todo Do we need to synchronize it?
	 * i think it's better to do so
	 */
	public void cancel(Runnable task) {
		ScheduledFuture<?> timedTask = timerTasks.remove(task);
		if (timedTask != null) {
			//@todo do we need to interrupt it while running ( true||false) ??
			timedTask.cancel(false);
		}
	}
	
	
	/**
	 * This method return all information about scheduler as string
	 * @return
	 */
	public String toString() {
		return String.format("[Scheduler] [%s] Task Scheduled: %d, executor: %d", 
				getName(), getTimerTaskCount(),getExecutorCount());
	}
	
	/**
	 * This method return the count of scheduler executor
	 * @return
	 */
	public int getExecutorCount() {
		return scheduledExecutors.size();
	}
	
	/**
	 * Return the scheduler name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method return the count of scheduled task
	 * @return
	 */
	private int getTimerTaskCount() {
		return timerTasks.size();
	}
	
	
	/**
	 * @return
	 */
	public int getNbofWorkers() {
		return nbofWorkers;
	}


	/**
	 * @param nbofWorkers
	 */
	public void setNbofWorkers(int nbofWorkers) {
		this.nbofWorkers = nbofWorkers;
	}

	
	/**
	 * Custom logging function
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AlertEngine][Scheduler][")
			.append(name).append("]")
			.append(this)
			.append(message);
		
		return sb.toString();
	}
}
