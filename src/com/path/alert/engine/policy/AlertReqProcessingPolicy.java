package com.path.alert.engine.policy;

import java.math.BigDecimal;

/**
 * This class will hold the Request processing policies
 * @author MohammadAliMezzawi
 *
 */
public class AlertReqProcessingPolicy {
	
	/**
	 * the number of receiver by Tasklet
	 */
	private BigDecimal chunkSize;
	
	/**
	 * maximum instantly receiver by request
	 * before considering the request as bulk
	 */
	private BigDecimal maxInstantlyReceiver;
	
	/**
	 * @return the chunkSize
	 */
	public BigDecimal getChunkSize() {
		return chunkSize;
	}

	/**
	 * @param chunkSize the chunkSize to set
	 */
	public void setChunkSize(BigDecimal chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 * @return the maxInstantlyReceiver
	 */
	public BigDecimal getMaxInstantlyReceiver() {
		return maxInstantlyReceiver;
	}

	/**
	 * @param maxInstantlyReceiver the maxInstantlyReceiver to set
	 */
	public void setMaxInstantlyReceiver(BigDecimal maxInstantlyReceiver) {
		this.maxInstantlyReceiver = maxInstantlyReceiver;
	}
	
	
}
