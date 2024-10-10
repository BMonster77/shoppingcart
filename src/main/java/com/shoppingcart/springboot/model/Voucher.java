package com.shoppingcart.springboot.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Voucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voucherId;

	@Column(unique = true)
	private String voucherCode;

	private double voucherDiscount;

	private int remainingQuantity;

	@OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderVoucher> orderVouchers;

	// Getters and Setters
}
