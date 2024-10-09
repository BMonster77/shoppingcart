package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByCustomer(Customer customer);
}