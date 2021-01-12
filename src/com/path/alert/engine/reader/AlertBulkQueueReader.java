package com.path.alert.engine.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.path.alert.bo.fixed.FixedEventBO;
import com.path.alert.vo.notification.AlertNotificationCO;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.exception.BaseException;

/**
 * This class will hold the Alert Bulk Queue reader behavior
 * 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AlertBulkQueueReader extends AbstractQueueReader
{

    /**
     * Reference to fixed event BO
     */
    private FixedEventBO fixedEventBO;

    /**
     * Check about capacity before running, this should be done at the level of
     * the executor
     */
    public AlertBulkQueueReader()
    {
	setName("Bulk Queue Reader");
    }

    @Override
    protected List<AlertNotificationCO> readEvents()
    {
	// read the query and send message to the queue
	ArrayList<AlertNotificationCO> alertList = new ArrayList<>();

	//
	try
	{
	    logger.debug("AlertBulkQueueReader : Start Execute query");
	    logger.debug("Before calling select from Queue with bulk => Y ");
	    alertList = fixedEventBO.selectFromQueueOnline(ConstantsCommon.YES);

	    // @todo toArray Should be removed later
	    logger.debug("after calling select from Queue with bulk => Y results => "
		    + Arrays.toString(alertList.toArray()));
	    logger.debug("AlertBulkQueueReader : End Execute query");

	}
	catch(BaseException e)
	{
	    logger.error(e, "Error while Reading from Alert Queue [ Bulk ]");
	}

	return alertList;
    }

    /**
     * Return Fixed event BO
     * 
     * @return
     */
    public FixedEventBO getFixedEventBO()
    {
	return fixedEventBO;
    }

    /**
     * Set Fixed event BO
     * 
     * @param fixedEventBO
     */
    public void setFixedEventBO(FixedEventBO fixedEventBO)
    {
	this.fixedEventBO = fixedEventBO;
    }
}
