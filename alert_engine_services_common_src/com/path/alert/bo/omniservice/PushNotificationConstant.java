package com.path.alert.bo.omniservice;

public class PushNotificationConstant
{
	public static final String APPLICATION_JSON = "application/json";
    public static final String PUSH_NOTIFICATION_URL= "https://fcm.googleapis.com/fcm/send";
    public static final String METHOD_POST= "POST";
    public static final String PUSH_NOTIFICATION_AUTH_KEY = "pushNotifAuthorizationKey";
    public static final String OSRV_PROPERTIES = "PathOSRVRemoting.properties";
    public static final String PUSH_NOTIFICATION_SOUND = "default";
    public static final String PUSH_NOTIFICATION_CLICK_ACTION = "FCM_PLUGIN_ACTIVITY";
    public static final String PUSH_NOTIFICATION_ICON = "fcm_push_icon";
    public static final String PUSH_NOTIFICATION_PRIORITY =  "high";
    public static final String STATUS_READ =  "R";
    public static final String STATUS_UNREAD =  "U";
    public static final String STATUS_FAILED =  "F"; 
    public static final Integer WARNING_ERROR_ID = 1;
    public static final String SUCCESS = "Success";
    public static final String FAILURE_OUTPUT_TYPE = "F";
    public static final String INFORMATION_OUTPUT_TYPE = "I";
    public static final String SUCCESS_OUTPUT_TYPE = "S";
    public static final String FATAL_OUTPUT_TYPE = "E";
    public static final Integer SUCCESS_OUTPUT_CODE = 0;
    public static final String SUCCESS_OUTPUT_DESC = "Success";
    public static final Integer EXCEPTION_OUTPUT_CODE = -1;
    public static final String EXCEPTION_OUTPUT_DESC = "Exception";
    public static final Integer MISSING_VALUES_OUTPUT_CODE = -2;
    public static final String PATH_SEPARATOR = "\\";
    public static final Integer DISABLED_FLAG = 0;
    public static final Integer ENABLED_FLAG = 1;
    public static final String ONE = "1";

    public static String APP_ID= "appId";
    public static String CHANNEL_ID= "channelId";
    //Errors Description
    public static final String FAILED_TO_SEND_PUSH_NOTIFICATION = "Push Notification Failed while Sending";
    public static final String APP_CHANNEL_ID_REQUIRED = "appId And channelId custom tags are Required for Push Notification";
    public static final String APP_CHANNEL_ID_SHOULD_NUMERIC = "appId And channelId custom tags Should be Numeric for Push Notification";
    public static final String STATUS_ACTIVE	 = "ACT";
}
