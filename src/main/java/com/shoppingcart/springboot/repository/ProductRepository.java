package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<Product, Long>{

}

