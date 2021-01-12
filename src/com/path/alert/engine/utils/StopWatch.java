package com.path.alert.engine.utils;


import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import com.path.lib.common.util.StringUtil;


/**
 * An object that measures elapsed time in nanoseconds. It is useful to measure elapsed time using
 * this class instead of direct calls to {@link System#nanoTime} for a few reasons:
 */
public final class StopWatch {
	
	//@todo to fix this approach later Use ticker approach ( object handle all those infos )
	private HashMap<String,HashMap<String,Long>> timers = new HashMap<String,HashMap<String,Long>>();
	private HashMap<String,String> durations = new HashMap<String,String>();
	private HashMap<String,HashMap<String,String>> messages = new HashMap<String,HashMap<String,String>>();
	
	private static final String START_TIME = "start";
	private static final String END_TIME = "end";
	private static final String START_MSG = "startMsg";
	private static final String STOP_MSG = "stopMsg";
	private boolean debugMode;
	
	
	/**
	 * Start a timer
	 * @param timerName
	 * @return
	 * @throws Exception
	 */
	public Long start( String timerName){
		return start(timerName,"");
	}
	
	
	/**
	 * Start a timer
	 * @param timerName
	 * @return
	 * @throws Exception
	 */
	public Long start( String timerName, String startMsg ){
		
		Long startDateLong = null;
		
		try{
			if(timers.get(timerName) != null ) {
				throw new Exception(String.format("Timer already started: %s",timerName));
			}
			HashMap<String,Long> timerHm = new HashMap<>();
			
			// set start date
			startDateLong = System.currentTimeMillis();
			timerHm.put(START_TIME,startDateLong );
			
			// initialize start msg
			startMsg = StringUtil.nullToEmpty(startMsg);
			
			if(!startMsg.isEmpty()){
				HashMap<String,String> timerMsg = new HashMap<>();
				timerMsg.put(START_MSG, startMsg);
				messages.put(timerName, timerMsg);
			}
			
			timers.put(timerName, timerHm);
			
			if(isDebugMode()){
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(startDateLong);
				System.out.println(String.format("%s -> Timer started at %s -> { %s }",
						timerName, c.getTime(),startMsg));
			}
			
		}catch (Exception e) {
			
			if(isDebugMode()) {
				System.out.println(e.getMessage());
			}
		}
		
		return startDateLong;
	}
	
	
	/**
	 * Stop a timer
	 * @param timerName
	 * @return
	 * @throws Exception
	 */
	public Long stop( String timerName, String stopMsg ){
		
		Long duration = null;
		
		try{
			if(timers.get(timerName) == null ) {
				throw new Exception(String.format("Timer not started: %s",timerName));
			}
			HashMap<String,Long> timerHm =timers.get(timerName);
			Long endDate = System.currentTimeMillis();
			
			timerHm.put(END_TIME,endDate);
			duration = calculateDuration(timerName,timerHm);
			
			// save duration
			String prettyDuration = prettyFormatDuration(duration);
			durations.put(timerName,prettyDuration);
			
			// initialize start msg ( null to empty ?? )
			stopMsg = StringUtil.nullToEmpty(stopMsg);
			
			if(!stopMsg.isEmpty()){
				HashMap<String,String> timerMsg = messages.get(timerName);
				timerMsg.put(STOP_MSG, stopMsg);
			}
			
			
			if(isDebugMode()) {
				System.out.println(String.format("%s -> Timer Stoped , it took %s -> { %s }", 
						timerName, prettyDuration, stopMsg ));
			}
		}catch (Exception e) {
			if(isDebugMode()) {
				System.out.println(e.getMessage());
			}
		}finally {
			timers.remove(timerName);
		}
		
		return duration;
	}
	
	
	
	/**
	 * Return timer duration
	 * @param timerName
	 * @return
	 * @throws Exception
	 */
	public String returnDuration( String timerName ){
		
		if(durations.get(timerName) == null) {
			stop(timerName, null);
		}
		return durations.get(timerName);
	}
	
	
	/**
	 * Calculate the duration as Long
	 * @param timerName
	 * @param timerHm
	 * @return
	 * @throws Exception
	 */
	private Long calculateDuration(String timerName, HashMap<String, Long> timerHm) throws Exception {
		
		if(timerHm.get(START_TIME) == null || timerHm.get(END_TIME) == null ) {
			throw new Exception(String.format("Duration can't be calculated for timer %s",timerName));
		}
		// return Duration
		return timerHm.get(END_TIME) - timerHm.get(START_TIME);
		
	}

	
	/**
	 * Return Duration in a pretty format
	 * @param millis
	 * @return
	 */
	private String prettyFormatDuration(Long millis){
		
        Duration duration = Duration.ofMillis(millis);
        Long minutes = duration.toMinutes();
        Long seconds = duration.minusMinutes(minutes).getSeconds();
        Long millisecond = duration.minusMinutes(minutes).minusSeconds(seconds).toMillis();
		return String.format("%02d min, %02d sec, %02d ms", minutes,seconds,millisecond);
	}
	
	/**
	 * @return the debugMode
	 */
	public boolean isDebugMode() {
		return debugMode;
	}


	/**
	 * @param debugMode the debugMode to set
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
}