package com.path.alert.engine.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.exception.BaseException;

/**
 * This class will hold the Alert Instant Queue reader behavior
 * 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AlertInstantQueueReader extends AbstractQueueReader
{

    /**
     * Reference to fixed event BO
     */
    private FixedEventBO fixedEventBO;

    /**
     * Check about capacity before running, this should be done at the level of
     * the executor
     */
    public AlertInstantQueueReader()
    {
	setName("Instant Queue Reader");
    }

    @Override
    protected List<AlertNotificationCO> readEvents()
    {
	// read the query and send message to the queue
	ArrayList<AlertNotificationCO> alertList = new ArrayList<>();

	//
	try
	{

	    logger.debug("AlertInstantQueueReader : Start Execute query");
	    logger.debug("Before calling select from Queue with bulk => N ");

	    alertList = fixedEventBO.selectFromQueueOnline(ConstantsCommon.NO);

	    // @todo toArray Should be removed later
	    logger.debug("after calling select from Queue with bulk: N results : " + Arrays.toString(alertList.toArray()));

	    logger.debug("AlertInstantQueueReader : End Execute query");
	}
	catch(BaseException e)
	{
	    logger.error(e, "Error while Reading from Alert Queue [ Instant ]");
	}

	return alertList;
    }

    /**
     * @return the fixedEventBO
     */
    public FixedEventBO getFixedEventBO()
    {
	return fixedEventBO;
    }

    /**
     * @param fixedEventBO the fixedEventBO to set
     */
    public void setFixedEventBO(FixedEventBO fixedEventBO)
    {
	this.fixedEventBO = fixedEventBO;
    }
}
