package com.shoppingcart.springboot.service;

import java.util.List;

import com.shoppingcart.springboot.model.Product;

public interface ProductService {
    List<Product> getProductsByPage(int page, int size);
    Product getProductById(Long productId);
}
