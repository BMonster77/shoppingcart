package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingService {
    // 根据客户获取购物车
    ShoppingCart getShoppingCartByCustomer(Customer customer);

    // 将产品添加到购物车
    ShoppingCart addProductToCart(Customer customer, Product product, int quantity);

    // 从购物车中移除产品
    ShoppingCart removeProductFromCart(Customer customer, Product product);

    // 保存购物车状态的方法
    void saveShoppingCart(ShoppingCart shoppingCart);

    // 创建一个新的订单
    Order createOrderFromCart(Customer customer);
    Optional<Order> findOrderById(Long orderId);

    // 完成订单支付
    void completePayment(Order order, String voucherCode);

    // 新增代金券方法
    double applyVoucherToOrder(Order order, String voucherCode);

}
