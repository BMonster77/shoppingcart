package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.interfacemethods.ShoppingCartInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ShoppingCartImplementation implements ShoppingCartInterface {
}
