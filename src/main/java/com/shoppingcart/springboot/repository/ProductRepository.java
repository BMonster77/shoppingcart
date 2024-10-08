package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByNameContaining(String keyword, Pageable pageable);  // 支持分页的模糊搜索
    Page<Product> findAll(Pageable pageable);  // 分页获取所有产品
}

