package com.path.vo.common.header;

public class ServiceContextAllVO extends ServiceContextVO
{
	private String businessArea;
	private String businessDomain;
	private String operationName;
	private String serviceDomain;
	private String serviceID;
	private String version;

	
	public void setBusinessArea(String businessArea)
	{
	   this.businessArea = businessArea;
	}
	public String getBusinessArea()
	{
	   return businessArea;
	}
	
	public void setBusinessDomain(String businessDomain)
	{
	   this.businessDomain = businessDomain;
	}
	public String getBusinessDomain()
	{
	   return businessDomain;
	}
	
	public void setOperationName(String operationName)
	{
	   this.operationName = operationName;
	}
	public String getOperationName()
	{
	   return operationName;
	}

	public void setServiceDomain(String serviceDomain)
	{
	   this.serviceDomain = serviceDomain;
	}
	public String getServiceDomain()
	{
	   return serviceDomain;
	}
	
	public void setServiceID(String serviceID)
	{
	   this.serviceID = serviceID;
	}
	public String getServiceID()
	{
	   return serviceID;
	}
	
	public void setVersion(String version)
	{
	   this.version = version;
	}
	public String getVersion()
	{
	   return version;
	}
}
