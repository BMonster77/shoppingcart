package com.shoppingcart.springboot.model;

import jakarta.persistence.*;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private String comment;
	private int rating;

	// Getters and Setters
}