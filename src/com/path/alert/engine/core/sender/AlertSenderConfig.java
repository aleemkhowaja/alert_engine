package com.path.alert.engine.core.sender;

/**
 * This class house all available sender configuration
 * @author MohammadAliMezzawi
 *
 */
public class AlertSenderConfig {

	/**
	 * default smtp flag.
	 * if true that mean the engine will use the defaultmailer
	 */
	private boolean defaultSmtp;
	
	/**
	 * default sms flag.
	 * if true that mean the engine will use default sms configuration
	 * other wise its custom configuration and will send sms through integration
	 */
	private boolean defaultSms;
	

	/**
	 * @return
	 */
	public boolean isDefaultSmtp() {
		return defaultSmtp;
	}

	/**
	 * @param defaultMailer
	 */
	public void setDefaultSmtp(boolean defaultSmtp) {
		this.defaultSmtp = defaultSmtp;
	}

	/**
	 * @return
	 */
	public boolean isDefaultSms() {
		return defaultSms;
	}

	/**
	 * @param defaultSms
	 */
	public void setDefaultSms(boolean defaultSms) {
		this.defaultSms = defaultSms;
	}
	
	
	
}
