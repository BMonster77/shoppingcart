package com.shoppingcart.springboot.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ShoppingCartProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shoppingCartProductId;

	@ManyToOne
	@JoinColumn(name = "shopping_cart_id")
	private ShoppingCart shoppingCart;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;

	// Getters and Setters
}
