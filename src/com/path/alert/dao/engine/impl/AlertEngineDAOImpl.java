package com.path.alert.dao.engine.impl;

import java.util.HashMap;
import java.util.List;

import com.path.alert.dao.engine.AlertEngineDAO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;


/**
 * This class represent the DAO for Alert Engine
 * @author MohammadAliMezzawi
 *
 */
public class AlertEngineDAOImpl extends BaseDAO implements AlertEngineDAO
{
	/**
	 * Query for Alert configuration
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> returnAlertConfig() throws DAOException {
		// TODO Auto-generated method stub
		return (List<HashMap<String, Object>>)getSqlMap().
			queryForList("alertEngineMapper.returnAlertConfig", null);
	}
}
