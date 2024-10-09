package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.*;
import com.shoppingcart.springboot.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartProductRepository shoppingCartProductRepository,
                                   OrderRepository orderRepository,
                                   OrderDetailRepository orderDetailRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public ShoppingCart getShoppingCartByCustomer(Customer customer) {
        return shoppingCartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for customer ID: " + customer.getCustomerId()));
    }

    @Override
    public ShoppingCart addProductToCart(Customer customer, Product product, int quantity) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);

        Optional<ShoppingCartProduct> existingProduct = shoppingCart.getShoppingCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(product))
                .findFirst();

        if (existingProduct.isPresent()) {
            ShoppingCartProduct shoppingCartProduct = existingProduct.get();
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + quantity);
        } else {
            ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setQuantity(quantity);
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCartProductRepository.save(shoppingCartProduct);
            shoppingCart.getShoppingCartProducts().add(shoppingCartProduct);
        }

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromCart(Customer customer, Product product) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
        shoppingCart.getShoppingCartProducts().removeIf(cartProduct -> cartProduct.getProduct().equals(product));
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderFromCart(Customer customer) {
        try {
            ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
            Order newOrder = new Order();
            newOrder.setCustomer(customer);
            newOrder.setOrderDate(new Date());
            newOrder.setStatus("UNPAID");

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (ShoppingCartProduct cartProduct : shoppingCart.getShoppingCartProducts()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(cartProduct.getProduct());
                orderDetail.setQuantity(cartProduct.getQuantity());
                orderDetail.setOrder(newOrder);
                orderDetails.add(orderDetail);
            }

            newOrder.setOrderDetails(orderDetails);
            orderRepository.save(newOrder);
            orderDetailRepository.saveAll(orderDetails);
            clearShoppingCart(shoppingCart);
            logger.info("Order created successfully for customer ID: {}", customer.getCustomerId());
            return newOrder;
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

    @Override
    public void completePayment(Order order) {
        order.setStatus("PAID");
        orderRepository.save(order);
        logger.info("Order payment completed successfully for order ID: {}", order.getOrderId());
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getShoppingCartProducts().clear();
        shoppingCartRepository.save(shoppingCart);
    }
}
