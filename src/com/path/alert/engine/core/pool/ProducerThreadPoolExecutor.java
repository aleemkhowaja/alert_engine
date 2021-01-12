package com.path.alert.engine.core.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mohammad Ali Mezzawi

 */
public class ProducerThreadPoolExecutor extends ThreadPoolExecutor {
	

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	public ProducerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param linkedBlockingQueue
	 * @param threadFactory
	 */
	public ProducerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, 
			TimeUnit unit,LinkedBlockingQueue<Runnable> linkedBlockingQueue, ThreadFactory threadFactory) {
		super( corePoolSize, maximumPoolSize,keepAliveTime,unit,linkedBlockingQueue,threadFactory);
	}
	
	
    /**
     * @param nThreads
     * @param threadFactory
     * @return
     */
    public static ProducerThreadPoolExecutor newFixedThreadPool(int corePoolSize, int maximumPoolSize) {
    	
        return new ProducerThreadPoolExecutor(corePoolSize, maximumPoolSize,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
}
