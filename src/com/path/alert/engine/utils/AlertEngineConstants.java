package com.path.alert.engine.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Mohammad Ali Mezzawi
 *
 */
public final class AlertEngineConstants {
	
    /**
     * prohibit creation
     */
    private AlertEngineConstants()
    {
    	System.out.println("This Class Should not be Instantiated");
    }
    
    public static final String INSTANT_MSG_CONSUMER_KEY = "INSTANT_MSG_CONSUMER_KEY";
    public static final String BULK_MSG_CONSUMER_KEY = "BULK_MSG_CONSUMER_KEY";
    public static final String ALERT_QUEUE_SCHEDULER_KEY = "ALERT_QUEUE_SCHEDULER_KEY";
    
    public static final String INSTANT_MSG_DESTINATION = "INSTANT_MSG_DESTINATION";
    public static final String BULK_MSG_DESTINATION = "BULK_MSG_DESTINATION";
    
    // mail and mobile number glue/separator
    public static final String RECEIVER_SEPARATOR = "<<->>";
    
    // Message tag regex
    public static final String MSG_TAG_REGEX = "<([0-9a-zA-Z._]+)>";
    public static final String TAG_LDELIMITER = "<";
    public static final String TAG_RDELIMITER = ">";
    
    // Data type
    public static final String DATA_TYPE_DATE = "D";
    public static final String DATA_TYPE_VARCHAR = "V";
    public static final String DATA_TYPE_DATETIME = "DT";
    public static final String DATA_TYPE_NUMERIC = "N";
    
    
    // configuration constants
    public static final String DEFAULT_SENDER_CONFIG = "EMAIL_CONFIG_TYPE";
    public static final String SMS_DEFAULT_SENDER_CONFIG = "SMS_CONFIG_TYPE";
    
    // Pws Mapping Id
    public static final BigDecimal EMAIL_PWS_MAPPING_ID = new BigDecimal(BigInteger.ONE);
    public static final BigDecimal DEAFULT_SMS_PWS_MAPPING_ID = new BigDecimal(2);
    public static final BigDecimal CUSTOM_SMS_PWS_MAPPING_ID = new BigDecimal(5);
    public static final BigDecimal OMNI_PWS_MAPPING_ID = new BigDecimal(3);
    
    // AMQ Transport protocols
    public static final String DEFAULT_TRANSPORT_PROTOCOL = "tcp";
    public static final String VM_TRANSPORT_PROTOCOL = "vm";
    public static final String NIO_TRANSPORT_PROTOCOL = "nio";
    public static final String TCP_TRANSPORT_PROTOCOL = "tcp";
    
    // failover prefix
	public static final Object FAILOVER_WRAPPER_PREFIX = "failover:(";
	public static final Object FAILOVER_WRAPPER_SUFFIX = ")";
	
	// communication type ( email => E , SMS => S )
	public static final String COMM_TYPE_EMAIL = "E";
	public static final String COMM_TYPE_SMS =  "S";
	public static final String COMM_TYPE_OMNI =  "OI";
	
	
	// Request/Message status
	public static final String STATUS_NEW_REQUEST = "N";
	public static final String STATUS_PROCESSED_REQUEST = "P";
	public static final String STATUS_FAILED_REQUEST = "F";
	
	public static final String STATUS_ACTIVE_MSG = "A";
	public static final String STATUS_SKIPPED_MSG = "X";
	public static final String STATUS_FAILED_MSG = "F";
	public static final String STATUS_SENT_MSG = "S";
	
	public static final BigDecimal FAILED_CODE = new BigDecimal(-1);
	public static final BigDecimal SUCCESS_CODE = new BigDecimal(BigInteger.ONE);
	public static final BigDecimal INFO_CODE = new BigDecimal(BigInteger.ZERO);
	
	public static final String ENGINE_REMOTING_FILE = "PathAlertEngineRemoting.properties";
	
	public static final Integer FAILED_MSG_CODE = 39212;
	
	public static final String SYSTEM_EVENT_TYPE = "S";
	public static final String FIXED_EVENT_TYPE = "F";
	
	public static String MSG_QUEUE = "Message has been Queued";
	
	public static final String OMNI_ADMIN_APP_NAME = "OADM";
	
	/**
	 * is Omni installed flag
	 */
	public static Boolean IS_OMNI_INSTALLED;

}
