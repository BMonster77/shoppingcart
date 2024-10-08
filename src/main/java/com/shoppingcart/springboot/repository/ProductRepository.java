package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{

    Optional<Object> findByProductId(Long productId);
}

