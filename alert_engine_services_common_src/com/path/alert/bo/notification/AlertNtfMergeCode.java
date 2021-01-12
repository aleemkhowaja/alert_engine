package com.path.alert.bo.notification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.path.alert.engine.core.AlertEngTagProvider;
import com.path.alert.engine.core.AlertEngine;
import com.path.alert.engine.utils.AlertEngineConstants;
import com.path.alert.vo.fixed.FixedTagCO;
import com.path.alert.vo.notification.ALRT_EVT_COMM_MODE_ARG_MAPVO;
import com.path.alert.vo.notification.EvtCO;
import com.path.alert.vo.notification.SubscriberCO;
import com.path.bo.common.CommonLibBO;
import com.path.dbmaps.vo.BRANCHESVO;
import com.path.dbmaps.vo.COMPANIESVO;
import com.path.dbmaps.vo.CTSTRXTYPEVO;
import com.path.dbmaps.vo.CURRENCIESVO;
import com.path.dbmaps.vo.GEN_LEDGERVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.DateUtil;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;

/**
 * This class will hold all the available merges code for this message. It will
 * handle the type casting and replacement of the codes.
 * 
 * @author MohammadAliMezzawi
 */
public class AlertNtfMergeCode {
	
	/**
	 * Reference to AlertNotificationBO
	 */
	private AlertNotificationBO alertNotificationBO;
	
	/**
	 * Reference to Common Lib bo
	 */
	private CommonLibBO commonLibBO;
	
	/**
	 * Hold reference to the event CO
	 */
	private EvtCO evtCO;
	
	/**
	 * Hold the default tags that are part of the subscriber and event info
	 */
	private HashMap<String, Object> defaultTags = new HashMap<>();
	
	/**
	 * Hold the Dynamic tags (Custom) set while sending the notification
	 */
	private HashMap<String, String> eventCustomTags = new HashMap<>();
	
	/**
	 * Hold the Receiver details tags (Fixed events tags)
	 */
	private HashMap<String, Object> fixedEventTags = new HashMap<>();
	
	/**
	 * Hold the Batch tags (Batch events tags)
	 */
	private HashMap<String, Object> batchEventTags = new HashMap<>();
	
	/**
	 * @param evtCO
	 */
	public AlertNtfMergeCode(EvtCO evtCO) {
		
		commonLibBO = (CommonLibBO) ApplicationContextProvider
			.getApplicationContext().getBean("commonLibBO");
		
		alertNotificationBO = (AlertNotificationBO)ApplicationContextProvider
			.getApplicationContext().getBean("alertNotificationBO");
		
		this.evtCO = evtCO;
	}

	/**
	 * Load the default tags from the subscriber and event co
	 * @param subscriberCO
	 * @param evtCO
	 */
	public void loadDefaultTags(SubscriberCO subscriberCO) {
		
		/**
		 * Subscriber info tags
		 */
		// Subscriber Id
		BigDecimal subscriberId = NumberUtil.emptyDecimalToNull(subscriberCO.getSubScriberId());
		defaultTags.put(AlertNotificationConstant.SUBSCRIBER_ID, subscriberId);
		
		// brief name
		String briefName = StringUtil.nullEmptyToValue(subscriberCO.getBriefName(),
					StringUtil.nullEmptyToValue(subscriberCO.getDefaultBriefName(), ""));
		defaultTags.put(AlertNotificationConstant.BRIEF_NAME, briefName);
		
		// middle name
		String middleName = StringUtil.nullEmptyToValue(subscriberCO.getMiddleName(),
				StringUtil.nullEmptyToValue(subscriberCO.getDefaultMiddleName(), ""));
		defaultTags.put(AlertNotificationConstant.MIDDLE_NAME, middleName);
		
		// long name
		String longName = StringUtil.nullEmptyToValue(subscriberCO.getLongName(),
				StringUtil.nullEmptyToValue(subscriberCO.getDefaultLongName(), ""));
		defaultTags.put(AlertNotificationConstant.LONG_NAME, longName);
		
		// Address
		String address = StringUtil.nullEmptyToValue(subscriberCO.getAddress(),
				StringUtil.nullEmptyToValue(subscriberCO.getDefaultAddress(), ""));
		defaultTags.put(AlertNotificationConstant.ADDRESS, address);
		
		/**
		 * Event info tags
		 */
		// Event Description En
		String eventDescEn = StringUtil.nullEmptyToValue(evtCO.getDescEng(), "");
		defaultTags.put(AlertNotificationConstant.ALRT_EVT_DESC_ENG, eventDescEn);
		
		// Event Description Ar
		String eventDescAr = StringUtil.nullEmptyToValue(evtCO.getDescArab(), "");
		defaultTags.put(AlertNotificationConstant.ALRT_EVT_DESC_ARAB, eventDescAr);

		// Comp code
		BigDecimal compCode = NumberUtil.emptyDecimalToNull(evtCO.getCompCode());
		defaultTags.put(AlertNotificationConstant.COMPANY_CODE, compCode);
	}

	
	/**
	 * Tag Set Dynamically ( custom )
	 * 
	 * @param tagEvent
	 */
	public void loadEventTag(HashMap<String, String> tagEvent) {
		
		if(null == tagEvent)
			return;
		
		// if the tag doesn't start with Custom we should add it
		Iterator<Entry<String, String>> iterator = tagEvent.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> tagElement = iterator.next();
			String key = tagElement.getKey();

			if (!key.toLowerCase(Locale.ENGLISH)
					.startsWith(AlertNotificationConstant.CUSTOM.toLowerCase(Locale.ENGLISH)))
				key = AlertNotificationConstant.CUSTOM.concat(key);

			eventCustomTags.put(key, tagElement.getValue());
		}
	}
	
	
	/**
	 * load Receiver Details tags.
	 * This type of tags is available in the fixed event only.
	 * @param fixedEventId 
	 * @param loadReceiverDetailsTags
	 * @throws BaseException 
	 */
	public void loadReceiverTags(BigDecimal fixedEventId, HashMap<String, Object> loadReceiverDetailsTags) throws BaseException {
		
		if(null == loadReceiverDetailsTags)
			return;
		
		Iterator<Entry<String, Object>> iterator = loadReceiverDetailsTags.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Object> tagElement = iterator.next();
			String tagName = tagElement.getKey();
			Object tagValue = tagElement.getValue();
			tagValue = returnTagObjValue(fixedEventId, tagName,tagValue);
			fixedEventTags.put(tagName, tagValue);
		}
		
		if(null == fixedEventTags || fixedEventTags.isEmpty())
			return;
		
		// @note we assume that comp code is either String or BigDecimal
		BigDecimal compCode = returnNumericValue(fixedEventTags.get("COMP_CODE"));
		BigDecimal branchCode = returnNumericValue(fixedEventTags.get("BRANCH_CODE"));
		BigDecimal desBranch = returnNumericValue(fixedEventTags.get("DEST_BR_CODE")); 
		
		// get Company tags
		loadReceiverCompTags(compCode);

		// get branch tags
		loadReceiverBranchTags(compCode,branchCode);
		
		// get destination branch tags
		loadReceiverDestinationBrTags(compCode,desBranch);

	    // prepare currency info
		BigDecimal currencyCode = returnNumericValue(fixedEventTags.get("CY_CODE"));
	   
		// load currency tags
		loadCurrencyTags( compCode, currencyCode );

		 // prepare trx type info
		BigDecimal trxType = returnNumericValue(fixedEventTags.get("TRX_TYPE"));
		
	    // ctstrxtype
		loadCtstrxtypeTags(compCode, trxType);

	    // gen_LEDGERVO
		BigDecimal glCode = returnNumericValue(fixedEventTags.get("GL_CODE"));
		
		loadGenLedgerTags(compCode, glCode);
		
		/**
		 * After getting all tags values apply the custom prefix if needed
		 */
		if(evtCO.getEvtType().equalsIgnoreCase(AlertNotificationConstant.EVT_TYPE_DYNAMIC)) {
			// if the tag doesn't start with Custom we should add it
			Iterator<Entry<String, Object>> tagIt = fixedEventTags.entrySet().iterator();
			HashMap<String, Object> prefixedTags = new HashMap<>();
			while (tagIt.hasNext()) {
				Entry<String, Object> tagElement = tagIt.next();
				String key = tagElement.getKey();

				if (!key.toLowerCase(Locale.ENGLISH)
						.startsWith(AlertNotificationConstant.CUSTOM.toLowerCase(Locale.ENGLISH)))
					key = AlertNotificationConstant.CUSTOM.concat(key);

				prefixedTags.put(key, tagElement.getValue());
			}
			
			fixedEventTags.clear();
			fixedEventTags.putAll(prefixedTags);
		}
	}
	
	
	/**
	 * Load Batch tags
	 * @param batchTags
	 */
	public void loadBatchTags(HashMap batchTags) {
		
		if(null == batchTags)
			return;
		
		// if the tag doesn't start with Custom we should add it
		Iterator<Entry<String, Object>> iterator = batchTags.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Object> tagElement = iterator.next();
			String key = tagElement.getKey();

			if (!key.toLowerCase(Locale.ENGLISH)
					.startsWith(AlertNotificationConstant.BATCH.toLowerCase(Locale.ENGLISH)))
				key = AlertNotificationConstant.BATCH.concat(key);

			batchEventTags.put(key, tagElement.getValue());
		}
	}


	/**
	 * This method will return all tags defined
	 *  1- Default tags
	 *  2- Event custom tags
	 *  3- Fixed Event tags
	 *  4- Batch tags
	 */
	public HashMap<String,Object> returnAllTags(){
		
		HashMap<String,Object> allTags = new HashMap<>();
		
		// merge default tags
		allTags.putAll(defaultTags);
		
		// merge event custom tags
		allTags.putAll(eventCustomTags);
		
		// merge event custom tags
		allTags.putAll(fixedEventTags);
		
		// merge Batch tags
		allTags.putAll(batchEventTags);
		
		return allTags;
	}

	/**
	 * 
	 * This method will return all tags defined same as {@link returnAllTags}
	 * but in addition it return the query tags based on the provided query id and communication
	 * type
	 * 
	 *  1- Default tags
	 *  2- Event custom tags
	 *  3- Fixed Event tags
	 *  4- Batch tags
	 *  5- Query tags
	 *  
	 * @param communicationType
	 * @param queryId
	 * @return
	 * @throws Exception
	 * @note should we cache the query result ???
	 */
	public HashMap<String,Object> returnAllTags(String communicationType, BigDecimal queryId) throws Exception {
		
		HashMap<String,Object> allTags = returnAllTags();
		
		if(null == communicationType || null == queryId )
			return allTags;
		
		HashMap<String, String> hashParams = prepareHashParamsCommMode(communicationType,allTags);
		ArrayList<LinkedHashMap> result = alertNotificationBO.retrieveQueryRes(queryId.toString(), hashParams, 1);
		
		if(null == result && null == result.get(0))
			return allTags;
		
		HashMap<String, Object> queryTags = new HashMap<>();
		Iterator<Entry<String, Object>> iterator = result.get(0).entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Object> tagElement = iterator.next();
			String key = tagElement.getKey();

			if (!key.toLowerCase(Locale.ENGLISH)
					.startsWith(AlertNotificationConstant.QUERY.toLowerCase(Locale.ENGLISH)))
				key = AlertNotificationConstant.QUERY.concat(key);

			queryTags.put(key, tagElement.getValue());
		}
		
		// now merge the query tags with others tags
		allTags.putAll(queryTags);
		return allTags;
	}
	
	
	/**
     * This method will replace all tags that are in a given message
     * 
     * @param messageToBeSent
     * @return
     * @throws BaseException
     */
    public String replaceTag(String messageToBeSent, String communicationType, BigDecimal queryId) throws Exception
    {
    	HashMap<String,Object> hm = returnAllTags(communicationType, queryId);
    	HashMap<String,String> hmValues = new HashMap<>();
    	
    	// now we need to get the string value
    	Iterator<Entry<String, Object>> iterator = hm.entrySet().iterator();
    	while (iterator.hasNext()) {
	        Entry pair = (Map.Entry)iterator.next();
	        String tagKey = AlertEngineConstants.TAG_LDELIMITER.concat((String)pair.getKey())
	        		.concat(AlertEngineConstants.TAG_RDELIMITER);
	        hmValues.put(tagKey,returnStrValue(pair.getValue()));
    	}
    	
    	// replace all merge codes ( tags )
    	messageToBeSent = replaceEach(messageToBeSent, hmValues.keySet().toArray(new String[hm.size()]),
    			hmValues.values().toArray(new String[hm.size()]));
    	
    	// remove all the tags that doesn't have a value
    	return messageToBeSent.replaceAll(AlertEngineConstants.MSG_TAG_REGEX, "");
	
    }
    

    /**
     * Cast the  Fixed event tags value to the appropriate type.
     * @param fixedEventId 
     * @param tagName
     * @param tagValue
     * @return
     * @throws BaseException
     */
    private Object returnTagObjValue(BigDecimal fixedEventId, String tagName, Object tagValue) throws BaseException {
    	
    	// value is already an object
    	if(!(tagValue instanceof String)) {
    		return tagValue;
    	}
    	
    	// if empty string we consider it as null
    	String valueStr= ((String)tagValue).trim();
    	if(StringUtil.isEmptyString(valueStr))
    		return null;
    	
    	// get the tag type to cast the object to
    	FixedTagCO tagCO = AlertEngTagProvider.getInstance().getTagInfo(fixedEventId, tagName);
    	String type = null == tagCO ? AlertEngineConstants.DATA_TYPE_VARCHAR : 
    		StringUtil.nullEmptyToValue(tagCO.getColumnType(),AlertEngineConstants.DATA_TYPE_VARCHAR);
       
    	// cast the value based on the column type
        Object value = null;
        switch(type) {
        
        	case AlertEngineConstants.DATA_TYPE_NUMERIC:
        		/**
        		 * In some cases the trigger is returning the tag value with leading/tailing
        		 * spaces which is causing a Number format exception
        		 */
        		value = valueStr.isEmpty()? null : new BigDecimal(valueStr);
        		break;
        		
        	case AlertEngineConstants.DATA_TYPE_DATE:
        		String datePattern = 1 == commonLibBO.returnIsSybase()?
        				"MMM d yyyy h:mma":"dd-MMM-yy";
        		value = DateUtil.parseDate(valueStr, datePattern);
        		value = DateUtil.format((Date)value);
        		break;
        		
        	case AlertEngineConstants.DATA_TYPE_VARCHAR:
        	default :
        		value = valueStr;
        		break;
        }
        
        return value; 
	}
    
    
	/**
	 * Load Receiver Destination Branch tags
	 * Fixed Event ( Json/condition tags )
	 * @param compCode
	 * @param desBranch
	 * @throws BaseException
	 */
	private void loadReceiverDestinationBrTags(BigDecimal compCode, BigDecimal desBranch) throws BaseException {

		if(null == compCode || null == desBranch)
			return;

		BRANCHESVO branchVO = returnBranchDetails(compCode, desBranch);
		
		fixedEventTags.put(AlertNotificationConstant.DEST_BRANCHES_LONG_DESC_ARAB, branchVO.getLONG_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.DEST_BRANCHES_BRIEF_DESC_ARAB, branchVO.getBRIEF_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.DEST_BRANCHES_LONG_DESC_ENG, branchVO.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.DEST_BRANCHES_BRIEF_DESC_ENG, branchVO.getBRIEF_DESC_ENG());
	}


	/**
	 * Load Receiver Branch tags
	 * Fixed Event ( Json/condition tags )
	 * @param compCode
	 * @param branchCode
	 * @throws BaseException
	 */
	private void loadReceiverBranchTags(BigDecimal compCode, BigDecimal branchCode) throws BaseException {
		
		if(null == compCode || null == branchCode)
			return;
		
		BRANCHESVO branchVO = returnBranchDetails(compCode,branchCode);
		fixedEventTags.put(AlertNotificationConstant.BRANCHES_LONG_DESC_ARAB, branchVO.getLONG_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.BRANCHES_BRIEF_DESC_ARAB, branchVO.getBRIEF_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.BRANCHES_LONG_DESC_ENG, branchVO.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.BRANCHES_BRIEF_DESC_ENG, branchVO.getBRIEF_DESC_ENG());
		
	}


	/**
	 * Load Receiver Comp tags
	 * Fixed Event ( Json/condition tags )
	 * @param compCode
	 * @throws BaseException 
	 */
	private void loadReceiverCompTags(BigDecimal compCode) throws BaseException {

		if(null == compCode)
			return;
		
		COMPANIESVO company = returnCompanyDetails(compCode);
		
		fixedEventTags.put(AlertNotificationConstant.COMPANIES_LONG_DESC_ARAB, company.getLONG_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.COMPANIES_BRIEF_DESC_ARAB, company.getBRIEF_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.COMPANIES_LONG_DESC_ENG, company.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.COMPANIES_BRIEF_DESC_ENG, company.getBRIEF_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.COMPANIES_BASE_CURRENCY, company.getBASE_CURRENCY());

	}
	
	/**
	 * Load GenLedger Details tags
	 * 
	 * @param compCode
	 * @param glCode
	 * @throws BaseException
	 */
	private void loadGenLedgerTags(BigDecimal compCode, BigDecimal glCode) throws BaseException {
		
		if (null == compCode || null == glCode)
			return;
		
		GEN_LEDGERVO gen_LEDGERVO = new GEN_LEDGERVO();
		gen_LEDGERVO.setGL_CODE(glCode);
		gen_LEDGERVO.setCOMP_CODE(compCode);
		
		/**
	     * set the connection co
	     */
		gen_LEDGERVO.setConnCO(AlertEngine.getInstance().getConnCO());
		
		gen_LEDGERVO = commonLibBO.returnGenralLegder(gen_LEDGERVO);
		
		if(null == gen_LEDGERVO)
			throw new BaseException("No General ledger is found for comp code /Gl code " 
					+ compCode.intValue() + " " + glCode.intValue());
		
		fixedEventTags.put(AlertNotificationConstant.GEN_LEDGER_LONG_DESC_ARAB, gen_LEDGERVO.getLONG_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.GEN_LEDGER_BRIEF_DESC_ARAB, gen_LEDGERVO.getBRIEF_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.GEN_LEDGER_LONG_DESC_ENG, gen_LEDGERVO.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.GEN_LEDGER_BRIEF_DESC_ENG, gen_LEDGERVO.getBRIEF_DESC_ENG());
	}

	/**
	 * Get Ctstrxtype tags
	 * 
	 * @param compCode
	 * @param trxType
	 * @throws BaseException
	 */
	private void loadCtstrxtypeTags(BigDecimal compCode, BigDecimal trxType) throws BaseException {
		if (null == compCode || null == trxType)
			return;

		CTSTRXTYPEVO ctstrxtype = new CTSTRXTYPEVO();
		ctstrxtype.setCODE(trxType);
		ctstrxtype.setCOMP_CODE(compCode);
		
		/**
	     * set the connection co
	     */
		ctstrxtype.setConnCO(AlertEngine.getInstance().getConnCO());

		ctstrxtype = commonLibBO.returnCtsTrxType(ctstrxtype);
		
		if(null == ctstrxtype)
			throw new BaseException("No Trx type is found for comp code /trxType code " 
					+ compCode.intValue() + " " + trxType.intValue());
		
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_JV_TYPE, ctstrxtype.getJV_TYPE());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_SHORT_DESC_ENG, ctstrxtype.getSHORT_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_SHORT_DESC_ARAB, ctstrxtype.getSHORT_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_CHQ_RELATED, ctstrxtype.getCHQ_RELATED());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_BS_CONTRA_FLAG, ctstrxtype.getBS_CONTRA_FLAG());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_LONG_DESC_ENG, ctstrxtype.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.CTSTRXTYPE_LONG_DESC_ARAB, ctstrxtype.getLONG_DESC_ARAB());
	}

	/**
	 * Return Currency Details
	 * 
	 * @param compCode
	 * @param currencyCode
	 * @return
	 * @throws BaseException
	 */
	private void loadCurrencyTags(BigDecimal compCode, BigDecimal currencyCode) throws BaseException {
		
		if (null == compCode || null == currencyCode)
			return;
		
		CURRENCIESVO currency = new CURRENCIESVO();
		currency.setCOMP_CODE(compCode);
		currency.setCURRENCY_CODE(currencyCode);

		/**
		 * set the connection co
		 */
		currency.setConnCO(AlertEngine.getInstance().getConnCO());
		currency = commonLibBO.returnCurrency(currency);
		
		if(null == currency)
			throw new BaseException("No Currency is found for comp code /currency code " 
					+ compCode.intValue() + " " + currencyCode.intValue());
		
		fixedEventTags.put(AlertNotificationConstant.CURRENCIES_LONG_DESC_ARAB, currency.getLONG_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.CURRENCIES_BRIEF_DESC_ARAB, currency.getBRIEF_DESC_ARAB());
		fixedEventTags.put(AlertNotificationConstant.CURRENCIES_LONG_DESC_ENG, currency.getLONG_DESC_ENG());
		fixedEventTags.put(AlertNotificationConstant.CURRENCIES_BRIEF_DESC_ENG, currency.getBRIEF_DESC_ENG());
	}
	
	
	/**
	 * Helper methods
	 */
    private COMPANIESVO returnCompanyDetails(BigDecimal compCode) throws BaseException
    {

	if(null == compCode)
	    return null;

	COMPANIESVO company = new COMPANIESVO();
	company.setCOMP_CODE(compCode);

	/**
     * set the connection co
     */
    company.setConnCO(AlertEngine.getInstance().getConnCO());
    company = commonLibBO.returnCompany(company);

	if(null == company)
	    throw new BaseException("No Company is found for comp code " + compCode.intValue());

	return company;

    }
	
	
    /**
     * Return Branch Details
     * @param compCod
     * @param branchCode
     * @return
     * @throws BaseException
     */
    private BRANCHESVO returnBranchDetails(BigDecimal compCode, BigDecimal branchCode) throws BaseException
    {

	BRANCHESVO branch = new BRANCHESVO();
	branch.setBRANCH_CODE(branchCode);
	branch.setCOMP_CODE(compCode);
	
	/**
     * set the connection co
     */
    branch.setConnCO(AlertEngine.getInstance().getConnCO());

	branch = commonLibBO.returnBranch(branch);

	if(null == branch)
	    throw new BaseException(
		    "No Company is found for comp/branch code " + compCode.intValue() + "/" + branchCode.intValue());

		return branch;
    }
	
	
	/**
	 * Prepare Query's parameters
	 * 
	 * @param communicationType
	 * @param allTagsValue 
	 * @return
	 * @throws Exception
	 * 
	 * @note : Query params are string ??? why ??
	 */
	private HashMap<String, String> prepareHashParamsCommMode(String communicationType, HashMap<String, Object> allTagsValue) throws Exception {
		HashMap<String, String> hmQryParam = new HashMap<String, String>();

		ALRT_EVT_COMM_MODE_ARG_MAPVO alrtArgs = new ALRT_EVT_COMM_MODE_ARG_MAPVO();
		alrtArgs.setCOMMUNICATION_TYPE(communicationType);
		alrtArgs.setEVT_ID(evtCO.getEventID());
		ArrayList<ALRT_EVT_COMM_MODE_ARG_MAPVO> argsList = alertNotificationBO.returnAlrtArgsQuery(alrtArgs);
		
		for (ALRT_EVT_COMM_MODE_ARG_MAPVO element : argsList) {
			
			// if Mapped element is a constant value
			if (!StringUtil.nullToEmpty(element.getMAPPING_VALUE()).isEmpty()) {
				hmQryParam.put(element.getARG_NAME(), element.getMAPPING_VALUE());
				continue;
			}
			
			// if Mapped element is a Tag
			if (!StringUtil.nullToEmpty(element.getMAPPING_TAG_NAME()).isEmpty()) {
				hmQryParam.put(element.getARG_NAME(), returnStrValue(allTagsValue.get(element.getMAPPING_TAG_NAME())));
			}
		}
		
		return hmQryParam;
	}
	
	
	/**
	 * Replace the given array of needles by the given replacement values.
	 * @param s
	 * @param searchList
	 * @param replacementList
	 * @return
	 */
	public static String replaceEach(final String text, final String searchList[], final String replacementList[]) {

		if (searchList.length != replacementList.length)
			throw new IllegalArgumentException("Search list and replacement list sizes do not match");

		String replaced = text;
		for (int i = 0; i < searchList.length; i++) {
			replaced = StringUtils.replaceIgnoreCase(replaced, searchList[i], replacementList[i]);
		}

		return replaced;
	}
	
    /**
     * Return String representation of a given value.
     * 
     * @param value
     * @return
     */
    private String returnStrValue(Object value) {
    	
    	if(null == value)
    		return "";
    	
    	if(value instanceof String) {
    		return (String)value;
    	}
    	if(value instanceof Date) {
    		return DateUtil.format((Date)value, "dd/MM/yyyy");
    	}
    	return value.toString();
	}
    
	/**
	 * Get Numeric value of a HashMap entry value
	 */
	private BigDecimal returnNumericValue(Object value) {
		
		 if( null == value )
			 return null;
		 
		 if(value instanceof BigDecimal )
			 return (BigDecimal) value;
		 
		 // @note : do we need to check if it's a number
		 if(value instanceof String)
			 return new BigDecimal((String)value);
		 
		return null;
	}
}