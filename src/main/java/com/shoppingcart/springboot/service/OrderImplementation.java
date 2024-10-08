package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.interfacemethods.OrderInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderImplementation implements OrderInterface {
}
