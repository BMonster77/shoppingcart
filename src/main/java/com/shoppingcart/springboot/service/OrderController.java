//package com.shoppingcart.springboot.controller;

//import org.springframework.stereotype.Controller;

//@Controller
//public class OrderController {

//    @GetMapping("/purchase-history")
//    public List<PurchaseHistory> browsePurchaseHistory(@RequestParam Long userId) {
//        return purchaseHistoryService.getPurchaseHistoryByUserId(userId);
//    }


//    删除
 //   @GetMapping("/purchase-history")
//    public List<PurchaseHistory> hidePurchaseHistory(@RequestParam Long userId) {
//        return purchaseHistoryService.getPurchaseHistoryByUserId(userId);
//    }

package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // 获取所有订单，支持分页和排序
    public List<Order> getAllOrders(int page, int size, String[] sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort));
        return orderRepository.findAll(pageRequest).getContent();
    }

    // 根据用户ID查询订单历史
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // 创建新订单
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // 删除订单
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    // 修改订单状态
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // 根据订单ID查询订单
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
