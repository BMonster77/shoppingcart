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

	@ManyToMany
	@JoinTable(
			name = "shoppingcart_product", // 中间表的表名
			joinColumns = @JoinColumn(name = "shopping_cart_id"), // 当前实体在中间表的外键
			inverseJoinColumns = @JoinColumn(name = "product_id") // 对方实体在中间表的外键
	)
	private List<Product> products;

	private int quantity;

	// Getters and Setters
}
