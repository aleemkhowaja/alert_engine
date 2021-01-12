package com.path.alert.engine.core.container;

import java.util.List;
import com.path.alert.engine.core.task.AlertRequestTasklet;

public class AlertInstantMsgContainer extends AbstractMsgContainer{
	
	/**
	 * @param containerName
	 */
	public AlertInstantMsgContainer(String containerName) {
		super(containerName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean submit(AlertRequestTasklet requestTasklet){
		requestTasklet.setMessageContainer(this);
		return requestTasklet.execute();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean submit(List<AlertRequestTasklet> requestTasklets) {
		boolean result = true;
		for(AlertRequestTasklet tasklet : requestTasklets){
			result &= submit(tasklet);
		}
		
		return result;
	}
}
