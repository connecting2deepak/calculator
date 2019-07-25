/**
 * 
 */
package com.discount.calculator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.discount.calculator.Entity.Discount;

/**
 * @author deepak.s29
 */
public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
