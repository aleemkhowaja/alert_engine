package com.path.alert.engine.reader;

import java.util.List;

import com.path.alert.engine.core.AlertEngine;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.lib.log.Log;

/**
 * This Abstract class House the basic Queue reader Behavior
 * 
 * @author Mohammad Ali Mezzawi
 */
public abstract class AbstractQueueReader implements Runnable
{

    /**
     * Queue Reader Name
     */
    private String name;
    /**
     * 
     */
    protected Log logger = Log.getInstance();
    /**
     * 
     */
    private AlertEngine engine;

    /**
     * @return the alert engine
     */
    public AlertEngine getEngine()
    {
	return engine;
    }

    /**
     * @param alertEngine set the engine
     */
    public void setEngine(AlertEngine alertEngine)
    {
	// TODO Auto-generated method stub
	engine = alertEngine;
    }

    /**
     * Run the below task
     */
    public void run()
    {

	try
	{
	    // get list of Notification
	    List<AlertNotificationCO> listOfNotifications = readEvents();
	    
	    // loop over notification and send them ;)
	    for(AlertNotificationCO notificationCO : listOfNotifications)
	    {
		AlertEngine.getInstance().sendMessage(notificationCO);
	    }

	}
	catch(Exception e)
	{
	    logger.error(e, String.format("Error while Posting from Alert Queue [ %s ]", name));
	}
    }

    /**
     * Read Events from the queue
     * 
     * @return
     */
    abstract protected List<AlertNotificationCO> readEvents();

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
	this.name = name;
    }
}
