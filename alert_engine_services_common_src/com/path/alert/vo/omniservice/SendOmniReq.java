package com.path.alert.vo.omniservice;
import com.path.vo.common.header.ServiceContextAllVO;
import javax.xml.bind.annotation.XmlElement;
import com.path.vo.common.ImBaseRequest;
import javax.xml.bind.annotation.XmlType;

/**
 * @AutoGenerated Code
 * @description class SendSMSReq extends ImBaseRequest
 */

@XmlType(propOrder={})
public class SendOmniReq extends ImBaseRequest
{
	private ServiceContextAllVO serviceContext = new ServiceContextAllVO();
	private String as_mobile_phone;
	private String as_message_body;

	public void setServiceContext(ServiceContextAllVO serviceContext)
	{
	   this.serviceContext = serviceContext;
	}
	public ServiceContextAllVO getServiceContext()
	{
	   return serviceContext;
	}
	public void setAs_mobile_phone(String as_mobile_phone)
	{
	   this.as_mobile_phone = as_mobile_phone;
	}
	@XmlElement(required = true)
	public String getAs_mobile_phone()
	{
	   return as_mobile_phone;
	}
	public void setAs_message_body(String as_message_body)
	{
	   this.as_message_body = as_message_body;
	}
	@XmlElement(required = true)
	public String getAs_message_body()
	{
	   return as_message_body;
	}
}
