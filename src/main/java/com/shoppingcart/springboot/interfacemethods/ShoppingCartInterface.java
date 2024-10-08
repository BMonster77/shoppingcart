package com.shoppingcart.springboot.interfacemethods;

import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartInterface {

    // 根据客户获取购物车
    ShoppingCart getShoppingCartByCustomer(Customer customer);

    // 将产品添加到购物车
    ShoppingCart addProductToCart(Customer customer, Product product, int quantity);

    // 从购物车中移除产品
    ShoppingCart removeProductFromCart(Customer customer, Product product);

    // 创建一个新的购物车
    ShoppingCart createShoppingCart(Customer customer);
}