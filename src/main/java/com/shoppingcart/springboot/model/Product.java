package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	private String name;
	private String category;
	private String description;
	private double price;
	private double discount;
	private String imageUrl;
	private String tags;
	private int storeQuantity;
	private int viewCount;
	private boolean isVisible;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "product")
	private List<ShoppingCartProduct> shoppingCartProducts;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;

	public Product() {
	}

	// Getters and Setters
}
