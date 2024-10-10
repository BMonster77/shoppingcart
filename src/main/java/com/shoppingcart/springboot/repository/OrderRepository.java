package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
