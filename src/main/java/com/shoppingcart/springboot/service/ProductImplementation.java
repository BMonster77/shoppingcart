package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.interfacemethods.ProductInterface;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductImplementation implements ProductInterface{
}
