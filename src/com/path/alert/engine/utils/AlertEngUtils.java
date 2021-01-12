package com.path.alert.engine.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.path.alert.vo.engine.AlertMessageCO;
import com.path.alert.vo.engine.AlertNtfCO;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

public class AlertEngUtils {

	/**
	 * This method will chunk a given list to a smaller chunk
	 * @param list
	 * @param chunkSize
	 * @return
	 */
	public static <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
		
		if (chunkSize <= 0) {
			throw new IllegalArgumentException("Invalid chunk size: " + chunkSize);
		}
		
		List<List<T>> chunkList = new ArrayList<>();
		for (int i = 0; i < list.size(); i += chunkSize) {
		    	int end = Math.min(i + chunkSize,  list.size());
			chunkList.add(list.subList(i,end));
		}
		return chunkList;
	}
	
	/**
	 * This method will create a connection
	 * @param autoStart
	 * @return
	 * @throws JMSException
	 */
	public static Connection createNewConnection(String brokerUrl, boolean autoStart) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		Connection connection = connectionFactory.createConnection();

		if (autoStart) {
			connection.start();
		}
		return connection;
	}
	
	
	/**
	 * This method will create Jms session using a Jms connection
	 * @param con
	 * @return
	 * @throws JMSException
	 */
	protected static Session createSession(Connection con, int ackMode ) throws JMSException {
		return con.createSession(false, ackMode);
	}


	/**
	 * This method will create a session with Auto acknowledgement
	 * @param con
	 * @return
	 * @throws JMSException
	 */
	public static Session createSession(Connection con ) throws JMSException {
		return createSession(con,Session.AUTO_ACKNOWLEDGE);
	}
	

	/**
	 * This method will create a Message producer
	 * @param session
	 * @return
	 * @throws JMSException 
	 */
	public static MessageProducer createMessageProducer(Session session, String destinationName, 
			int deliveryMode ) throws JMSException {
		
		Destination destination = session.createQueue(destinationName);
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(deliveryMode);
		return producer;
	}
	
	
    /**
     * Return computer name
     * @return
     */
	public static String getComputerName() {
		
		Map<String, String> env = System.getenv();
		
		if (env.containsKey("COMPUTERNAME")) { 
			return env.get("COMPUTERNAME");
		}
		if (env.containsKey("HOSTNAME")) {
			return env.get("HOSTNAME");
		}
		try {
			return InetAddress.getLocalHost().getHostAddress() != null ?
					InetAddress.getLocalHost().getHostAddress():
						InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			Log.getInstance().error(e,"Unknown host Exception");
		}
		
		return "UNKNOWN_MACHINE_NAME";
	}
	
	
	public static HashMap<String, Object> fillSmsVariablesMap(AlertMessageCO co)
	{
		HashMap<String, Object> smsMap = new HashMap<>();
		smsMap.put("as_message_body", StringUtil.nullToEmpty(co.getMessageBody()).replaceAll("<br/>", "\n"));
		if(co.getNtfCO() != null)
		{	
			AlertNtfCO ntfCo = co.getNtfCO();
			if(ntfCo.getSubscriberCO() != null)
			{
				smsMap.put("subId", ntfCo.getSubscriberCO().getSubScriberId());
				if(ntfCo.getSubscriberCO().getSubscriptionCO() != null ) {
				    smsMap.put("subscriptionId", ntfCo.getSubscriberCO().getSubscriptionCO().getSubscriptionID());
				}
				/**
				 * put the language code while sending notification
				 */
				smsMap.put("as_lang_code", ntfCo.getSubscriberCO().getLang());
			}
			smsMap.put("eventId", ntfCo.getEventID());
			if(ntfCo.getMsgVO() != null)
			{
				if(ntfCo.getMsgVO().getMSG_ID() != null && ntfCo.getMsgVO().getREQUEST_ID() != null) 
				{
    					smsMap.put("msgId", ntfCo.getMsgVO().getMSG_ID().toString().concat(ntfCo.getMsgVO().getREQUEST_ID().toString()));
    					smsMap.put("an_request_id", ntfCo.getMsgVO().getREQUEST_ID());
    					smsMap.put("an_msg_id", ntfCo.getMsgVO().getMSG_ID());
				}
			}
		}
		return smsMap;
	}
	
}
