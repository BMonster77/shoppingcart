package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    rService orderService;

    // 获取所有订单，支持分页和排序
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {
        List<Order> orders = orderService.getAllOrders(page, size, sort);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 根据用户ID查询订单历史
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 创建订单
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // 根据订单ID删除订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 修改订单状态
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // 根据订单ID查询订单详情
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}

