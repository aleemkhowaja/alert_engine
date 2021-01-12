package com.path.alert.bo.notification;

import java.math.BigDecimal;
import java.util.HashMap;

public class AlertNotificationConstant
{
    public static final String commonReportingBOService = "commonReportingBOService";
    public static final String pathReportServiceProperty = "PathRemoting.properties";
    public static final String pathReportServiceURL = "reporting.serviceURL";
    
    // Columns name
    public static final String CIF_NO = "ALRT_SUB.CIF_NO";
    public static final String SUB_ID = "ALRT_SUB.ID";
    public static final String CHANNEL_END_USER_ID = "ALRT_SUB.CHANNEL_END_USR_ID";
    public static final String FACEBOOK_SOCIAL_ID = "ALRT_SUB.FACEBOOK_SOCIAL_ID";
    public static final String TWITTER_SOCIAL_ID = "ALRT_SUB.TWITTER_SOCIAL_ID";
    public static final String USERNAME = "ALRT_SUB.USR_ID";
    public static final String EMAIL_ID = "ALRT_SUB.EMAIL_ID";
    public static final String MOBILE_PHONE = "ALRT_SUB.MOBILE_PHONE";
    public static final String QUERY = "query.";
    public static final String BATCH = "batch.";
    public static final String SUB = "sub.";
    public static final String CUSTOM = "custom.";
    public static final String BRIEF_NAME = "BRIEF_NAME";
    public static final String MIDDLE_NAME = "MIDDLE_NAME";
    public static final String LONG_NAME = "LONG_NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String ALRT_EVT_DESC_ENG = "ALRT_EVT.DESC_ENG";
    public static final String ALRT_EVT_DESC_ARAB = "ALRT_EVT.DESC_ARAB";
    public static final String SUBSCRIBER_ID = "SUB_ID";
    public static final String COMPANY_CODE = "COMP_CODE";
    
    public static final String BRANCHES_LONG_DESC_ARAB = "BRANCHES.LONG_DESC_ARAB";
    public static final String BRANCHES_BRIEF_DESC_ARAB = "BRANCHES.BRIEF_DESC_ARAB";
    public static final String BRANCHES_LONG_DESC_ENG = "BRANCHES.LONG_DESC_ENG";
    public static final String BRANCHES_BRIEF_DESC_ENG = "BRANCHES.BRIEF_DESC_ENG";
    
    public static final String DEST_BRANCHES_LONG_DESC_ARAB = "DEST_BRANCHES.LONG_DESC_ARAB";
    public static final String DEST_BRANCHES_BRIEF_DESC_ARAB = "DEST_BRANCHES.BRIEF_DESC_ARAB";
    public static final String DEST_BRANCHES_LONG_DESC_ENG = "DEST_BRANCHES.LONG_DESC_ENG";
    public static final String DEST_BRANCHES_BRIEF_DESC_ENG = "DEST_BRANCHES.BRIEF_DESC_ENG";
    
    public static final String COMPANIES_LONG_DESC_ARAB = "COMPANIES.LONG_DESC_ARAB";
    public static final String COMPANIES_BRIEF_DESC_ARAB = "COMPANIES.BRIEF_DESC_ARAB";
    public static final String COMPANIES_LONG_DESC_ENG = "COMPANIES.LONG_DESC_ENG";
    public static final String COMPANIES_BRIEF_DESC_ENG = "COMPANIES.BRIEF_DESC_ENG";
    public static final String COMPANIES_BASE_CURRENCY = "COMPANIES.BASE_CURRENCY";
    
    public static final String CURRENCIES_LONG_DESC_ARAB = "CURRENCIES.LONG_DESC_ARAB";
    public static final String CURRENCIES_BRIEF_DESC_ARAB = "CURRENCIES.BRIEF_DESC_ARAB";
    public static final String CURRENCIES_LONG_DESC_ENG = "CURRENCIES.LONG_DESC_ENG";
    public static final String CURRENCIES_BRIEF_DESC_ENG = "CURRENCIES.BRIEF_DESC_ENG";
    
    public static final String GEN_LEDGER_LONG_DESC_ARAB = "GEN_LEDGER.LONG_DESC_ARAB";
    public static final String GEN_LEDGER_BRIEF_DESC_ARAB = "GEN_LEDGER.BRIEF_DESC_ARAB";
    public static final String GEN_LEDGER_LONG_DESC_ENG = "GEN_LEDGER.LONG_DESC_ENG";
    public static final String GEN_LEDGER_BRIEF_DESC_ENG = "GEN_LEDGER.BRIEF_DESC_ENG";
    
    
    public static final String CTSTRXTYPE_JV_TYPE = "CTSTRXTYPE.JV_TYPE";
    public static final String CTSTRXTYPE_SHORT_DESC_ENG = "CTSTRXTYPE.SHORT_DESC_ENG";
    public static final String CTSTRXTYPE_SHORT_DESC_ARAB = "CTSTRXTYPE.SHORT_DESC_ARAB";
    public static final String CTSTRXTYPE_CHQ_RELATED = "CTSTRXTYPE.CHQ_RELATED";
    public static final String CTSTRXTYPE_BS_CONTRA_FLAG = "CTSTRXTYPE.BS_CONTRA_FLAG";
    public static final String CTSTRXTYPE_LONG_DESC_ENG = "CTSTRXTYPE.LONG_DESC_ENG";
    public static final String CTSTRXTYPE_LONG_DESC_ARAB = "CTSTRXTYPE.LONG_DESC_ARAB";
    
    public static final String EVENT_STATUS_APPROVED = "P";
    
    public static final String NUMBER = "NUMBER";
    public static final String DATE = "DATE";
    public static final String STRING = "STRING";
    public static final String NBREC = "nbRec";
    public static final String currentAppName = "currentAppName";
    public static final String userName = "userName"; 
    public static final String companyCode = "companyCode";
    public static final String branchCode = "branchCode";
    public static final String DYNAMICTEMPLATE = "D";
    public static final String AMF_ADDRESS = "AMF_ADDRESS";
    public static final String LINE_NO = "LINE_NO";
    public static final String ACC_CIF = "ACC_CIF";
    public static final String CIF_ADDRESS = "CIF_ADDRESS";
    public static final String CIF_NOCOLUMN = "CIF_NO";
    public static final String CTSCARDS_MGT = "CTSCARDS_MGT";
    public static final String APPLICATION_ID = "APPLICATION_ID";
    public static final String CIF = "CIF";
    public static final String SRC_CONTACT_C = "C";//Account
    public static final String SRC_CONTACT_F = "F"; //CIF
    public static final String SRC_CONTACT_D = "D"; //CARD
    public static final String SRC_CONTACT_A = "A";//Alert
    public static final String EMAIL = "EMAIL";
    public static final String MOBILE = "MOBILE";
    public static final String EVT_TYPE_DYNAMIC = "D";
    public static final String BATCH_EVT_TYPE = "B";
    public static final String EvtBatchType_IU = "IU";
    public static final String EvtBatchType_CU = "CU";
    public static final String EvtBatchType_E = "E";
    public static final String EvtBatchType_M = "M";
    public static final String EvtBatchType_C = "C";
    
    
    // list of receiver types used by the Alert Services and Engine
    // Receiver type
    public static final String RECEIVER_TYPE_SUB = "SUB";
    public static final String RECEIVER_TYPE_CIF = "CIF";
    public static final String RECEIVER_TYPE_USER_ID = "USER_ID";
    public static final String RECEIVER_TYPE_CHANNEL = "CHANNEL";
    public static final String RECEIVER_TYPE_FB = "FACEBOOK";
    public static final String RECEIVER_TYPE_TW = "TWITTER";
    public static final String RECEIVER_TYPE_MAIL = "MAIL";
    public static final String RECEIVER_TYPE_MOBILE = "MOBILE";
    public static final String RECEIVER_TYPE_ACCOUNT = "ACCOUNT";
    public static final String RECEIVER_TYPE_GROUP = "GROUP";
    public static final String COMP_CODE = "Comp Code";
    
    public static final String OLD_MOBILE = "old";
    public static final String New_MOBILE = "new";
    public static final String BOTH_MOBILE = "both";
    public static final String FIXED_EVT_OLD_MOBILE = "CIF_ADDRESS.MOBILE.OLD";
    public static final String FIXED_EVT_NEW_MOBILE = "CIF_ADDRESS.MOBILE.NEW";
    
    public static final BigDecimal CIF_GENERIC_FILTER = new BigDecimal(10);
    
    
    //hash map to save the types of the tags
    public static final HashMap<String, String> hashTagsType = new HashMap<>();
    
    /**
     * Default Language Msg subject and Body
     */
    public static final String DEFAULT_LANG_FLAG = "D";
    
    
   /**
    * Internal Alerts custom tags  constants 
    */
    
    public static final String CUSTOM_COMP_CODE = "compCode";
    public static final String CUSTOM_BRANCH_CODE = "branchCode";
    public static final String CUSTOM_APP_NAME = "appName";
    public static final String CUSTOM_SENDER_USER = "senderUser";
    
    public static final String FAILED_TO_SEND_INTERNAL_ALRT = "Internal Alert Failed while Sending";
    public static final String MISSING_RECEIVER_USR_ID = "user Id is missing in subscriber";

    
}
