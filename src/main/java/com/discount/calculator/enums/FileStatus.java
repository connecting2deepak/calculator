/**
 * 
 */
package com.discount.calculator.enums;

/**
 * @author deepak.s29
 *
 */
public enum FileStatus {

	PROCESSED("PROCESSED"),
	
	RE_PROCESSED("RE_PROCESSED");
	
	private FileStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
