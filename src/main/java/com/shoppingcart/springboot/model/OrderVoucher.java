package com.shoppingcart.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class
OrderVoucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderVoucherId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "voucher_id")
	private Voucher voucher;

	// Getters and Setters
}
