package com.path.alert.engine.utils;

public enum AlertEngineStatus {

	RUNNING("R"),
	SHUTDOWNING("B"),
	SHUTDOWN("S");
	
	/**
	 * Hold Engine status code
	 */
	private String code;
	
	/**
	 * @param code
	 */
	private AlertEngineStatus(String code)
	{
		this.setCode(code);
	}

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
}
