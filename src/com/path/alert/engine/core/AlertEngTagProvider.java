package com.path.alert.engine.core;

import java.math.BigDecimal;
import java.util.HashMap;
import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.log.Log;

/**
 * @author MohammadAliMezzawi
 *
 */
public final class AlertEngTagProvider {

	/**
	 * Hold reference to the instance
	 */
	private static AlertEngTagProvider instance;
	
	/**
	 * tags info
	 */
	private volatile HashMap<String, FixedTagCO> tagsInfo = new HashMap<String, FixedTagCO>();
	
	/**
	 * Reference to the BO
	 */
	private FixedEventBO fixedEventBO;

	/**
	 * Monitor Object
	 */
	private static Object monitorObj = new Object();

	/**
	 * Create instance of Data provider
	 * 
	 * @return
	 */
	public static AlertEngTagProvider getInstance() {
		if (instance == null) {
			synchronized (monitorObj) {
				if (instance == null) {
					instance = new AlertEngTagProvider();
					instance.setFixedEventBO((FixedEventBO) ApplicationContextProvider
						.getApplicationContext().getBean("fixedEventBO"));
				}
			}
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @param fixedEventId 
	 * @param tagName
	 * @return
	 */
	public FixedTagCO getTagInfo(BigDecimal fixedEventId, String tagName) {

		if (null == tagsInfo.get(tagName)) {
			populateTag(fixedEventId, tagName);
		}
		return tagsInfo.get(tagName);
	}

	
	/**
	 * Flush the cached data
	 */
	public void clearData() {
		synchronized (monitorObj) {
			tagsInfo = new HashMap<String, FixedTagCO>();
		}
	}
	
	
	/**
	 * Retrieve the tag info from the DB
	 * @param fixedEventId 
	 * @param tagName
	 */
	private void populateTag(BigDecimal fixedEventId, String tagName) {
		synchronized (monitorObj) {
			try {
				if (null == tagsInfo.get(tagName)) {
				    	FixedTagCO tag = null;
				    	
				    	/**
				    	 * check if fixed event is not null 
				    	 * then retrieve tag based on fixed event id
				    	 */
				    	if(!NumberUtil.isEmptyDecimal(fixedEventId))
				    	{
				    	    tag = fixedEventBO.selectFixedTags(fixedEventId, tagName);
				    	}
					
					if (null != tag) {
						tagsInfo.put(tag.getTagName(), tag);
					} else {
						// @note : i doubt this change
						tagsInfo.put(tagName, new FixedTagCO());
					}
				}
			} catch (Exception e) {
				Log.getInstance().error(e, "Error While populating info for tag " + tagName);
			}
		}
	}
	
	
	/**
	 * @param fixedEventBO
	 */
	private void setFixedEventBO(FixedEventBO fixedEventBO) {
		this.fixedEventBO = fixedEventBO;
	}
}
