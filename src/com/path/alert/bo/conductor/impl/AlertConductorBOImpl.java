package com.path.alert.bo.conductor.impl;

import java.util.HashMap;

import com.path.alert.bo.conductor.AlertConductorBO;
import com.path.alert.dao.conductor.AlertConductorDAO;
import com.path.alert.vo.fixed.FixedDynQueryCO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;

public class AlertConductorBOImpl extends BaseBO implements AlertConductorBO {

    private AlertConductorDAO alertConductorDAO;
    @Override
    public HashMap<String, Object> executeDynQuery(FixedDynQueryCO fixedDynQueryCO) throws BaseException {
	// TODO Auto-generated method stub
	String query = "SELECT " + fixedDynQueryCO.getColumnsList() + " FROM "+
		fixedDynQueryCO.getTableName() + " " + fixedDynQueryCO.getWhereCond();
	
	

    
	return alertConductorDAO.executeDynQuery(query, fixedDynQueryCO.getTableName());
    }
    /**
     * @return the alertConductorDAO
     */
    public AlertConductorDAO getAlertConductorDAO() {
	return alertConductorDAO;
    }
    /**
     * @param alertConductorDAO the alertConductorDAO to set
     */
    public void setAlertConductorDAO(AlertConductorDAO alertConductorDAO) {
	this.alertConductorDAO = alertConductorDAO;
    }

}
