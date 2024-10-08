package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shoppingCartId;

	@OneToOne(mappedBy = "shoppingCart")
	private Customer customer;

	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingCartProduct> shoppingCartProducts = new ArrayList<>();

	public ShoppingCart() {
	}

}
