package com.shoppingcart.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderDetailId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

	private int quantity;

	// Getters and Setters
}
