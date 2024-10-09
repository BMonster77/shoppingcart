package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The admin controller handles the admin functions for managing products,
 * including adding, editing,
 * deleting products, managing stock, and toggling product visibility.
 */

// Create RestAPI
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @Autowired
  private ProductService productService;

  // Add a NEW product
  // Use POST request that accepts a Product object in the request RequestBody
  // Save to Database
  // Returns te created product with 201 Created status
  @PostMapping("/products")
  public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
    Product newProduct = productService.saveProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
  }

  // Get ALL products
  // Use Get as we are retrieving and showing to user
  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  // Search products by ID, name, or category
  @GetMapping("/products/search")
  public ResponseEntity<List<Product>> searchProducts(
      @RequestParam(required = false) Long productId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String category
  ) {
      List<Product> products = productService.searchProducts(productId, name, category);
      return ResponseEntity.ok(products);
  }

  // Get ONE product by ID
  // Use Get as we are retrieving and showing to user
  @GetMapping("/products/{productId}")
  public ResponseEntity<Product> getProductsById(@PathVariable Long productId) {
      Product product = productService.findProductById(productId);
      return ResponseEntity.ok(product);
  }

  // Get ONE product by Name
  @GetMapping("/products/name/{name}")
  public ResponseEntity<List<Product>> getProductsByName(@PathVariable String name) {
      List<Product> products = productService.findProductByName(name);
      return ResponseEntity.ok(products);
  }

  // Get ONE prouct by Category
  @GetMapping("/products/category/{category}")
  public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
    List<Product> products = productService.findProductByCategory(category);
    return ResponseEntity.ok(products);
  }


  // Update the entire product (all details)
  @PutMapping("/products/{productId}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
      Product updatedProduct = productService.updateProduct(productId, product);
      return ResponseEntity.ok(updatedProduct);
  }

  // Delete a product by ID
  // Return 204 No Content
  @DeleteMapping("/products/{productId}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
      productService.deleteProductById(productId);
      return ResponseEntity.noContent().build();
  }





}

// // Add Product - 添加产品
// @PostMapping("/admin/product/add")
// public String addProduct(@RequestBody Product product) {
//
// }
//
// // Edit Product - 编辑产品
// @PutMapping("/admin/product/edit/{productId}")
// public String editProduct(@PathVariable Long productId, @RequestBody Product
// productDetails) {
//
// }
//
// // Delete Product - 删除产品
// @DeleteMapping("/admin/product/delete/{productId}")
// public String deleteProduct(@PathVariable Long productId) {
//
// }
//
// // Manage Stock - 库存管理 (隐藏缺货产品) hide
// @PutMapping("/admin/product/manage-stock/{productId}")
// public String manageStock(@PathVariable Long productId, @RequestParam int
// stock) {
//
// }
