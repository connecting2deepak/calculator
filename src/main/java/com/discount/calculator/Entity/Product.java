/**
 * 
 */
package com.discount.calculator.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author deepak.s29
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 2387022825931329438L;

	@Id
	private String productId;

	@Column(name = "list_price")
	private BigDecimal listPrice;
	
	@Column(name = "discount_price")
	private BigDecimal discountPrice;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
	private List<Discount> discounts;

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
	 * Gets the list price.
	 *
	 * @return the list price
	 */
	public BigDecimal getListPrice() {
		return listPrice;
	}

	/**
	 * Sets the list price.
	 *
	 * @param listPrice the new list price
	 */
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	/**
	 * Gets the discounts.
	 *
	 * @return the discounts
	 */
	public List<Discount> getDiscounts() {
		return discounts;
	}

	/**
	 * Sets the discounts.
	 *
	 * @param discounts the new discounts
	 */
	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

}
