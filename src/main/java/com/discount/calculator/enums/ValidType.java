/**
 * 
 */
package com.discount.calculator.enums;

/**
 * @author deepak.s29
 */
public enum ValidType {

	VNDMS(1L, "application/vnd.ms-excel"),

	MSEXCEL(2L, "application/msexcel"),

	EXCEL(3L, "application/x-excel"),

	DOSMSEXCEL(4L, "application/x-dos_ms_excel"),

	XLS(5L, "application/xls"),

	XXLS(6L, "application/x-xls"),

	VNDOPENXML(7L, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	private Long code;

	private String value;

	/**
	 * Instantiates a new valid type.
	 *
	 * @param code the code
	 * @param value the value
	 */
	private ValidType(Long code, String value) {
		this.code = code;
		this.value = value;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Long getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(Long code) {
		this.code = code;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Checks if is invalid type.
	 *
	 * @param value the value
	 * @return the boolean
	 */
	public static Boolean isInvalidType(final String value) {
		Boolean invalid = Boolean.TRUE;
		for (ValidType type : ValidType.values()) {
			if (value.trim().equalsIgnoreCase(type.getValue())) {
				invalid = Boolean.FALSE;
				break;
			}
		}
		return invalid;
	}

}
