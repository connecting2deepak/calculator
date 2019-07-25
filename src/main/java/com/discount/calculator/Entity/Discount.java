/**
 * 
 */
package com.discount.calculator.Entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author deepak.s29
 */
@Entity
@Table(name = "discount")
public class Discount implements Serializable {

	private static final long serialVersionUID = 8632240557197253425L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long discountId;

	@Column(name = "discount_percentage")
	private String discountPercentage;

	@Column(name = "valid_from")
	private Timestamp validFrom;

	@Column(name = "valid_to")
	private Timestamp validTo;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false, referencedColumnName = "productId")
	private Product product;

	/**
	 * Gets the discount id.
	 *
	 * @return the discount id
	 */
	public Long getDiscountId() {
		return discountId;
	}

	/**
	 * Sets the discount id.
	 *
	 * @param discountId the new discount id
	 */
	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	/**
	 * Gets the discount percentage.
	 *
	 * @return the discount percentage
	 */
	public String getDiscountPercentage() {
		return discountPercentage;
	}

	/**
	 * Sets the discount percentage.
	 *
	 * @param discountPercentage the new discount percentage
	 */
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	/**
	 * Gets the valid from.
	 *
	 * @return the valid from
	 */
	public Timestamp getValidFrom() {
		return validFrom;
	}

	/**
	 * Sets the valid from.
	 *
	 * @param validFrom the new valid from
	 */
	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * Gets the valid to.
	 *
	 * @return the valid to
	 */
	public Timestamp getValidTo() {
		return validTo;
	}

	/**
	 * Sets the valid to.
	 *
	 * @param validTo the new valid to
	 */
	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}

	/**
	 * Gets the product.
	 *
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Sets the product.
	 *
	 * @param product the new product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
