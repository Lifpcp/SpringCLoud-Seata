package com.java.li.product.dao;

import com.java.li.common.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Integer> {
}
