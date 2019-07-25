/**
 * 
 */
package com.discount.calculator.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author deepak.s29
 */
public class Values implements Serializable {

	private static final long serialVersionUID = -4690601329263987876L;

	@JacksonXmlProperty(localName = "SalesPrice")
	private BigDecimal salesPrice;

	@JacksonXmlProperty(localName = "LastUpdated")
	private String lastUpdated;

	/**
	 * Gets the sales price.
	 *
	 * @return the sales price
	 */
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	/**
	 * Sets the sales price.
	 *
	 * @param salesPrice the new sales price
	 */
	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * Gets the last updated.
	 *
	 * @return the last updated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the last updated.
	 *
	 * @param lastUpdated the new last updated
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
