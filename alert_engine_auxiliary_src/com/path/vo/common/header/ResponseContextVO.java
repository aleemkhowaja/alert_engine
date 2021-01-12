package com.path.vo.common.header;

import java.util.Date;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.path.lib.common.util.DateTimeAdapter;

@XmlType(propOrder = {
	    "responseID",
	    "requestID",
	    "coreRequestTimeStamp",
	    "coreResponseTimeStamp",
	})
public class ResponseContextVO {

	private String responseID;
	private String requestID;
	private Date coreRequestTimeStamp;
	private Date coreResponseTimeStamp;
	public String getResponseID() {
		return responseID;
	}
	public void setResponseID(String responseID) {
		this.responseID = responseID;
	}
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
	@XmlSchemaType(name="dateTime")
    @XmlJavaTypeAdapter(value=DateTimeAdapter.class, type=Date.class)
	public Date getCoreResponseTimeStamp() {
		return coreResponseTimeStamp;
	}
	public void setCoreResponseTimeStamp(Date coreResponseTimeStamp) {
		this.coreResponseTimeStamp = coreResponseTimeStamp;
	}
}
