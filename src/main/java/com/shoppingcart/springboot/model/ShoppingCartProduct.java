package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ShoppingCartProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shoppingCartProductId;

	@ManyToOne
	@JoinColumn(name = "shopping_cart_id")
	private ShoppingCart shoppingCart; // 表示与购物车的多对一关系

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product; // 表示与产品的多对一关系

	private int quantity; // 商品数量

	// Getters and Setters
}

