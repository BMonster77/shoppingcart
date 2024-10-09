package com.shoppingcart.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "orders") // 修改表名避免使用 SQL 保留字
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	@JsonIgnore
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetail> orderDetails = new ArrayList<>();

	private double finalPrice;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address deliveryAddress;

	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;

	// 使用 String 字段来管理订单状态
	private String status;

	// Getters and Setters
}
