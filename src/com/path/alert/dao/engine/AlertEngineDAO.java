package com.path.alert.dao.engine;

import java.util.HashMap;
import java.util.List;

import com.path.lib.common.exception.DAOException;


public interface AlertEngineDAO
{
	/**
	 * Query for Alert configuration
	 * @return
	 * @throws DAOException
	 */
	List<HashMap<String, Object>> returnAlertConfig() throws DAOException;

}
