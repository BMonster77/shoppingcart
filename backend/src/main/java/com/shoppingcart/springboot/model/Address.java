package com.shoppingcart.springboot.model;

import jakarta.persistence.*;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	private String streetAddress;
	private String city;
	private String state;
	private String postalCode;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	// Getters and Setters


}
