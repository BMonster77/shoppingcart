package com.shoppingcart.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ShoppingCart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shoppingCartId;

	@OneToOne(mappedBy = "shoppingCart")
	@JsonBackReference
	private Customer customer;

	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShoppingCartProduct> shoppingCartProducts = new ArrayList<>();

	// Getters and Setters

}
