package com.path.vo.common.header;

import java.util.Date;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.path.lib.common.util.DateTimeAdapter;

public class RequestContextVO {

	private String 	requestID;
	private Date 	coreRequestTimeStamp;
	String requestKernelDetails;

	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	@XmlSchemaType(name="dateTime")
	@XmlJavaTypeAdapter(value=DateTimeAdapter.class, type=Date.class)
	public Date getCoreRequestTimeStamp() {
		return coreRequestTimeStamp;
	}
	public void setCoreRequestTimeStamp(Date coreRequestTimeStamp) {
		this.coreRequestTimeStamp = coreRequestTimeStamp;
	}
	public String getRequestKernelDetails() {
		return requestKernelDetails;
	}
	public void setRequestKernelDetails(String requestKernelDetails) {
		this.requestKernelDetails = requestKernelDetails;
	}
}
