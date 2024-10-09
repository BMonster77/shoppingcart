package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Product findProductById(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
  }

  @Override
  public List<Product> findProductByName(String name) {
    return productRepository.findByNameContainingIgnoreCase(name);
  }

  @Override
  public List<Product> findProductByCategory(String category) {
    return productRepository.findByCategoryContainingIgnoreCase(category);
  }

  @Override
  public Product updateProduct(Long productId, Product productDetails) {
    Optional<Product> productOptional = productRepository.findById(productId);

    if (productOptional.isPresent()) {
      Product existingProduct = productOptional.get();

      // Update the fields of the existing product with the new details
      existingProduct.setName(productDetails.getName());
      existingProduct.setCategory(productDetails.getCategory());
      existingProduct.setDescription(productDetails.getDescription());
      existingProduct.setPrice(productDetails.getPrice());
      existingProduct.setDiscount(productDetails.getDiscount());
      existingProduct.setImageUrl(productDetails.getImageUrl());
      existingProduct.setStoreQuantity(productDetails.getStoreQuantity());
      existingProduct.setVisible(productDetails.isVisible());

      // Save the updated product
      return productRepository.save(existingProduct);
    } else {
      throw new RuntimeException("Product not found with id: " + productId);
    }
  }

  @Override
  public void deleteProductById(Long productId) {
    if (productRepository.existsById(productId)) {
      productRepository.deleteById(productId);
    } else {
      throw new RuntimeException("Product not found with id: " + productId);
    }
  }

  @Override
  public List<Product> searchProducts(Long productId, String name, String category) {
    if (productId != null) {
      // Search by productId
      return List.of(productRepository.findById(productId)
          .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId)));
    } else if (name != null) {
      // Search by name
      return productRepository.findByNameContainingIgnoreCase(name);
    } else if (category != null) {
      // Search by category
      return productRepository.findByCategoryContainingIgnoreCase(category);
    } else {
      throw new RuntimeException("At least one search criterion (productId, name, category) must be provided.");
    }
  }
}
