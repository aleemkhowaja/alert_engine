package com.path.vo.common;

import com.path.vo.common.header.RequestContextVO;

public class ImBaseRequest extends ImPwsBaseObject
{
	
	private RequestContextVO requestContext = new RequestContextVO();

	public RequestContextVO getRequestContext() {
		return requestContext;
	}

	public void setRequestContext(RequestContextVO requestContext) {
		this.requestContext = requestContext;
	}
	
	
}