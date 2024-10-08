package com.shoppingcart.springboot.service;

import java.util.List;

import com.shoppingcart.springboot.model.Product;

public interface ProductService {
    List<Product> getAllProducts(int page, int size);  // 获取分页的产品列表
    List<Product> searchProducts(String keyword, int page, int size);  // 根据关键词搜索产品

    Product getProductById(Long productId);
}
