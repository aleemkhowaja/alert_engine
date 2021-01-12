package com.path.lib.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter extends XmlAdapter<String, Date>{

	private String pattern = "yyyy-MM-dd'T'HH:mm:ss";

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	public String marshal(Date date) throws Exception {
		if(date == null)
			return null;
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public Date unmarshal(String dateString) throws Exception
	{
		if(dateString.isEmpty())
			return new SimpleDateFormat(pattern).parse("1900-01-01T00:00:00");
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf.parse(dateString);
	}
}
