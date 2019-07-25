/**
 * 
 */
package com.discount.calculator.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author deepak.s29
 */
@JacksonXmlRootElement(localName = "Products")
public class Products implements Serializable {

	private static final long serialVersionUID = 9046946759606618132L;

	@JacksonXmlProperty(localName = "Product", isAttribute = true)
	private List<ProductIDs> productIDs;

	/**
	 * Gets the product I ds.
	 *
	 * @return the product I ds
	 */
	public List<ProductIDs> getProductIDs() {
		return productIDs;
	}

	/**
	 * Sets the product I ds.
	 *
	 * @param productIDs the new product I ds
	 */
	public void setProductIDs(List<ProductIDs> productIDs) {
		this.productIDs = productIDs;
	}

}
