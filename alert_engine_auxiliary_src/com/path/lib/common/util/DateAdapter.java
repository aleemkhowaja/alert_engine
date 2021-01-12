package com.path.lib.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * @author MohammadAliMezzawi
 *
 */
public class DateAdapter extends XmlAdapter<String, Date> {
	
	/**
	 * Default date pattern
	 */
	private String pattern = "yyyy-MM-dd";

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	public String marshal(Date date) throws Exception {
		if(date == null)
			return null;
		return new SimpleDateFormat(pattern).format(date);
	}

	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	public Date unmarshal(String dateString) throws Exception {
		if(dateString == null || dateString.isEmpty())
		{
			return new SimpleDateFormat(pattern).parse("1900-01-01");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf.parse(dateString);
	}
}
