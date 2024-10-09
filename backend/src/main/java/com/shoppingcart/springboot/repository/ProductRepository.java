package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

  List<Product> findByNameContainingIgnoreCase(String name);

  List<Product> findByCategoryContainingIgnoreCase(String category);

}
