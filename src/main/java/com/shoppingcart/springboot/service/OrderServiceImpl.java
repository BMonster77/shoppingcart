package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepo;
}
