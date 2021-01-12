package com.path.alert.dao.conductor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.path.alert.dao.conductor.AlertConductorDAO;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.vo.notification.QueryCO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;

public class AlertConductorDAOImpl extends BaseDAO implements AlertConductorDAO {

    @Override
    public HashMap<String, Object> executeDynQuery(String theSql,String tableName) throws DAOException {
	// TODO Auto-generated method stub
	/*DbCall db = new DbCall(datasourceJNDI);
	Object object;
	LinkedHashMap<String, Object> lhmSqlResult = null;
	try{
	    ResultSet rs = db.executeQuery(theSql.toString());
		// get the column count since being dynamic, we don't know the
		// columns being retrieved
		ResultSetMetaData rsmd = rs.getMetaData();
		int NumOfCol = rsmd.getColumnCount();
		int indx;
		

		while(rs.next())
		{
		    rsmd = rs.getMetaData();
		    lhmSqlResult = new LinkedHashMap<String, Object>();
		    // loop through the columns
		    for(indx = 1; indx <= NumOfCol; indx++)
		    {
			object = rs.getObject(indx);
			//Chady.A BUG#320994
			if (object instanceof Date) 
			{
			    lhmSqlResult.put(tableName + "." + rsmd.getColumnLabel(indx), DateUtil.format((Date)object, "yyyy"));
			}
			else
			{
			    lhmSqlResult.put(tableName + "." + rsmd.getColumnLabel(indx), object);
			}
		    }
		}
		rs.close();
		
	}
	catch(Exception e){
	    throw new DAOException(e);
	}
	
	finally{
	    
	    db.freeResources();
	}
	
	return lhmSqlResult;*/
	QueryCO queryCO = new QueryCO();
	queryCO.setQuery(theSql);
	/**
	 * set the connection co Because the data will insert inside the
	 * ALRT_RECEIVER_DETAILS and ALRT_RECEIVER_DATA tables from core and we
	 * need to check if the IMAL_CORE_YN='N' in primary db (it means alert
	 * has standalone database) then we need to add the connection object of
	 * core db
	 */
	queryCO.setConnCO(AlertEngine.getInstance().getConnCO());
	    
	ArrayList<Map<String,Object>> mapList = (ArrayList<Map<String,Object>>)getSqlMap().queryForList("alertConductorMapper.selectDynamic", queryCO);
	ArrayList<Map<String,Object>> mapResList = new ArrayList<Map<String,Object>>();
	for(Map<String,Object> map : mapList){
	    Map<String,Object> mapRes = new HashMap<String,Object>();
	    for (Map.Entry me : map.entrySet()) {
		mapRes.put(tableName + "." + me.getKey(), me.getValue() );
		//map.remove(me.getKey());
	        }
	    mapResList.add(mapRes);
	   // map.put(key, value).
	   // hashMap.put("New_Key", hashMap.remove("Old_Key"));
	}
	//if (mapResList.size() > 0)
	if ( ! mapResList.isEmpty()) {
	    return (HashMap<String, Object>) mapResList.get(0);
	} else {
		return new HashMap<>();
	    }  
    }

}
