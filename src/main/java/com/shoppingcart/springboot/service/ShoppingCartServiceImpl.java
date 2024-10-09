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

    // 仓库依赖
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final VoucherRepository voucherRepository; // 处理代金券的仓库

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartProductRepository shoppingCartProductRepository,
                                   OrderRepository orderRepository,
                                   OrderDetailRepository orderDetailRepository,
                                   VoucherRepository voucherRepository) { // 注入代金券仓库
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.voucherRepository = voucherRepository;
    }

    // ========================= 购物车操作相关方法 =========================

    /**
     * 根据客户获取购物车
     *
     * @param customer 客户信息
     * @return 购物车对象
     */
    @Override
    public ShoppingCart getShoppingCartByCustomer(Customer customer) {
        return shoppingCartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for customer ID: " + customer.getCustomerId()));
    }

    /**
     * 将商品添加到购物车
     *
     * @param customer 客户信息
     * @param product 商品信息
     * @param quantity 商品数量
     * @return 更新后的购物车对象
     */
    @Override
    public ShoppingCart addProductToCart(Customer customer, Product product, int quantity) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);

        // 检查购物车中是否已有此商品
        Optional<ShoppingCartProduct> existingProduct = shoppingCart.getShoppingCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(product))
                .findFirst();

        if (existingProduct.isPresent()) {
            // 商品存在，更新数量
            ShoppingCartProduct shoppingCartProduct = existingProduct.get();
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + quantity);
        } else {
            // 商品不存在，创建新购物车商品
            ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setQuantity(quantity);
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCartProductRepository.save(shoppingCartProduct);
            shoppingCart.getShoppingCartProducts().add(shoppingCartProduct);
        }

        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * 从购物车中移除商品
     *
     * @param customer 客户信息
     * @param product 商品信息
     * @return 更新后的购物车对象
     */
    @Override
    public ShoppingCart removeProductFromCart(Customer customer, Product product) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
        shoppingCart.getShoppingCartProducts().removeIf(cartProduct -> cartProduct.getProduct().equals(product));
        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * 保存购物车的状态
     *
     * @param shoppingCart 购物车对象
     */
    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartRepository.save(shoppingCart);
    }

    /**
     * 清空购物车
     *
     * @param shoppingCart 购物车对象
     */
    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getShoppingCartProducts().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    // ========================= 订单操作相关方法 =========================

    /**
     * 从购物车创建订单
     *
     * @param customer 客户信息
     * @return 新创建的订单对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderFromCart(Customer customer) {
        try {
            ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
            Order newOrder = new Order();
            newOrder.setCustomer(customer);
            newOrder.setOrderDate(new Date());
            newOrder.setStatus("UNPAID");

            // 转换购物车商品为订单明细
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

    /**
     * 完成订单支付
     *
     * @param order 订单对象
     * @param voucherCode 代金券代码（可选）
     */
    @Override
    public void completePayment(Order order, String voucherCode) {
        try {
            if (voucherCode != null && !voucherCode.isEmpty()) {
                // 查找并应用代金券
                Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(voucherCode);
                if (voucherOpt.isPresent()) {
                    Voucher voucher = voucherOpt.get();
                    if (voucher.getRemainingQuantity() > 0) {
                        double discount = voucher.getVoucherDiscount();
                        double discountedPrice = order.getFinalPrice() - discount;
                        order.setFinalPrice(Math.max(discountedPrice, 0));

                        // 更新代金券的剩余数量
                        voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);
                        voucherRepository.save(voucher);
                    }
                }
            }

            // 更新订单状态为已支付
            order.setStatus("PAID");
            orderRepository.save(order);
            logger.info("Order payment completed successfully for order ID: {}", order.getOrderId());
        } catch (Exception e) {
            logger.error("Error applying voucher or completing payment: {}", e.getMessage());
            throw new RuntimeException("Error during payment process: " + e.getMessage(), e);
        }
    }

    /**
     * 根据订单ID查找订单
     *
     * @param orderId 订单ID
     * @return 包含订单对象的 Optional
     */
    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * 将代金券应用于订单
     *
     * @param order 订单对象
     * @param voucherCode 代金券代码
     * @return 折扣后的订单价格
     */
    @Override
    public double applyVoucherToOrder(Order order, String voucherCode) {
        if (voucherCode == null || voucherCode.isEmpty()) {
            return order.getFinalPrice();
        }

        Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(voucherCode);
        if (voucherOpt.isPresent()) {
            Voucher voucher = voucherOpt.get();
            if (voucher.getRemainingQuantity() > 0) {
                double discount = voucher.getVoucherDiscount();
                double discountedPrice = order.getFinalPrice() - discount;
                order.setFinalPrice(Math.max(discountedPrice, 0));

                // 更新代金券的剩余数量
                voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);
                voucherRepository.save(voucher);
                orderRepository.save(order);
            }
        }
        return order.getFinalPrice();
    }
}

