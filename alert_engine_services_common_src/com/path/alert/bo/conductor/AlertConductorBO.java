package com.path.alert.bo.conductor;

import java.util.HashMap;

import com.path.alert.vo.fixed.FixedDynQueryCO;
import com.path.lib.common.exception.BaseException;

public interface AlertConductorBO {
    
    public HashMap<String, Object> executeDynQuery(FixedDynQueryCO fixedDynQueryCO) throws BaseException;

}
