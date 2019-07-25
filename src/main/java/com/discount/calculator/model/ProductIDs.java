/**
 * 
 */
package com.discount.calculator.model;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author deepak.s29
 */

@JacksonXmlRootElement(localName = "Product")
public class ProductIDs implements Serializable {

	private static final long serialVersionUID = -1085844895006105136L;

	@JacksonXmlProperty(localName = "Product ID", isAttribute = true)
	private String productId;

	@JacksonXmlProperty(localName = "Values")
	private Values values;

	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Sets the product id.
	 *
	 * @param productId the new product id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Values getValues() {
		return values;
	}

	/**
	 * Sets the values.
	 *
	 * @param values the new values
	 */
	public void setValues(Values values) {
		this.values = values;
	}

}
