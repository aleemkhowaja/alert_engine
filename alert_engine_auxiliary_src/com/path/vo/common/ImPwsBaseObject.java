package com.path.vo.common;

import com.path.lib.vo.BaseObject;
import com.path.vo.common.header.RequesterContextVO;
import com.path.vo.common.header.VendorContextVO;

public class ImPwsBaseObject extends BaseObject
{
    private static final long serialVersionUID = 1L;
    
    private RequesterContextVO requesterContext = new RequesterContextVO();
    private VendorContextVO vendorContext = new VendorContextVO();

    public VendorContextVO getVendorContext()
    {
	return vendorContext;
    }

    public void setVendorContext(VendorContextVO vendorContextVO)
    {
	this.vendorContext = vendorContextVO;
    }


    public RequesterContextVO getRequesterContext()
    {
	return requesterContext;
    }

    public void setRequesterContext(RequesterContextVO requesterContext)
    {
	this.requesterContext = requesterContext;
    }
}
