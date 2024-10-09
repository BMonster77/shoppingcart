package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product saveProduct(Product product);
    Product findProductById(Long id);
    List<Product> findProductByName(String name);
    List<Product> findProductByCategory(String category);
    Product updateProduct(Long id, Product product);
    void deleteProductById(Long id);
    List<Product> searchProducts(Long id, String name, String category);
}
