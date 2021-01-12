package com.path.alert.dao.conductor;

import java.util.HashMap;

import com.path.lib.common.exception.DAOException;

public interface AlertConductorDAO {
    
    public HashMap<String, Object> executeDynQuery(String theSql,String tableName) throws DAOException;

}
