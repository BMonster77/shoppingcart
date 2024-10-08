package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.interfacemethods.UserInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserImplementation implements UserInterface {
}
