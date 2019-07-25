package com.discount.calculator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.discount.calculator.Entity.Product;

/**
 * @author deepak.s29
 */
public interface ProductRepository extends JpaRepository<Product, String> {

}
