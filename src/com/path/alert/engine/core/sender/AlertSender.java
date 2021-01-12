package com.path.alert.engine.core.sender;

import com.path.alert.vo.engine.AlertMessageCO;
import com.path.alert.vo.engine.AlertMessageListCO;
import com.path.lib.common.exception.BaseException;

/**
 * This Interface represent the main functionality required by a sender
 * @author MohammadAliMezzawi
 *
 */
public interface AlertSender {

	/**
	 * @param message
	 * @return
	 * @throws BaseException 
	 */
	public boolean send(AlertMessageCO message) throws BaseException;
	
	/**
	 * @param message
	 * @return
	 */
	public boolean send(AlertMessageListCO alertMessageListCO) throws BaseException;
	
}
