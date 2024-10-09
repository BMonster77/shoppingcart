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
    private final VoucherRepository voucherRepository; // 新增：用于处理代金券的数据库操作

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartProductRepository shoppingCartProductRepository,
                                   OrderRepository orderRepository,
                                   OrderDetailRepository orderDetailRepository,
                                   VoucherRepository voucherRepository) { // 新增：注入代金券仓库
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.voucherRepository = voucherRepository; // 代金券仓库
    }

    @Override
    public ShoppingCart getShoppingCartByCustomer(Customer customer) {
        // 根据客户信息查找其购物车，如果找不到则抛出异常
        return shoppingCartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for customer ID: " + customer.getCustomerId()));
    }

    @Override
    public ShoppingCart addProductToCart(Customer customer, Product product, int quantity) {
        // 获取客户的购物车
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);

        // 检查购物车中是否已经存在该商品
        Optional<ShoppingCartProduct> existingProduct = shoppingCart.getShoppingCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(product))
                .findFirst();

        if (existingProduct.isPresent()) {
            // 如果商品已存在，更新商品数量
            ShoppingCartProduct shoppingCartProduct = existingProduct.get();
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + quantity);
        } else {
            // 如果商品不存在，创建一个新的购物车商品条目
            ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setQuantity(quantity);
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCartProductRepository.save(shoppingCartProduct);
            shoppingCart.getShoppingCartProducts().add(shoppingCartProduct);
        }

        // 保存并返回更新后的购物车
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromCart(Customer customer, Product product) {
        // 获取客户的购物车并移除指定商品
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
        shoppingCart.getShoppingCartProducts().removeIf(cartProduct -> cartProduct.getProduct().equals(product));
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void saveShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartRepository.save(shoppingCart); // 使用 shoppingCartRepository 保存购物车状态
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderFromCart(Customer customer) {
        try {
            // 获取客户的购物车并创建一个新的订单
            ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
            Order newOrder = new Order();
            newOrder.setCustomer(customer);
            newOrder.setOrderDate(new Date());
            newOrder.setStatus("UNPAID");

            // 将购物车中的商品转换为订单明细
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (ShoppingCartProduct cartProduct : shoppingCart.getShoppingCartProducts()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(cartProduct.getProduct());
                orderDetail.setQuantity(cartProduct.getQuantity());
                orderDetail.setOrder(newOrder);
                orderDetails.add(orderDetail);
            }

            // 设置订单的明细并保存到数据库
            newOrder.setOrderDetails(orderDetails);
            orderRepository.save(newOrder);
            orderDetailRepository.saveAll(orderDetails);
            clearShoppingCart(shoppingCart); // 清空购物车
            logger.info("Order created successfully for customer ID: {}", customer.getCustomerId());
            return newOrder;
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage(), e);
        }
    }

    @Override
    public void completePayment(Order order, String voucherCode) {
        try {
            if (voucherCode != null && !voucherCode.isEmpty()) {
                // 查找并应用代金券折扣
                Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(voucherCode);
                if (voucherOpt.isPresent()) {
                    Voucher voucher = voucherOpt.get();
                    if (voucher.getRemainingQuantity() > 0) {
                        double discount = voucher.getVoucherDiscount();
                        double discountedPrice = order.getFinalPrice() - discount;
                        order.setFinalPrice(Math.max(discountedPrice, 0)); // 确保价格不为负值

                        // 减少代金券的剩余数量
                        voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);
                        voucherRepository.save(voucher); // 保存更新后的代金券信息
                    }
                }
            }

            // 更新订单状态为已支付并保存
            order.setStatus("PAID");
            orderRepository.save(order);
            logger.info("Order payment completed successfully for order ID: {}", order.getOrderId());
        } catch (Exception e) {
            logger.error("Error applying voucher or completing payment: {}", e.getMessage());
            throw new RuntimeException("Error during payment process: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        // 根据订单ID查找订单
        return orderRepository.findById(orderId);
    }

    @Override
    public double applyVoucherToOrder(Order order, String voucherCode) {
        // 检查代金券代码是否为空
        if (voucherCode == null || voucherCode.isEmpty()) {
            return order.getFinalPrice(); // 如果没有代金券代码，返回原始价格
        }

        // 查找代金券
        Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(voucherCode);
        if (voucherOpt.isPresent()) {
            Voucher voucher = voucherOpt.get();
            // 检查代金券是否有剩余数量
            if (voucher.getRemainingQuantity() > 0) {
                double discount = voucher.getVoucherDiscount();
                double discountedPrice = order.getFinalPrice() - discount;
                order.setFinalPrice(Math.max(discountedPrice, 0)); // 确保最终价格不为负值

                // 更新代金券的剩余数量
                voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);
                voucherRepository.save(voucher); // 保存代金券的更新
                orderRepository.save(order); // 保存订单的更新
            }
        }
        return order.getFinalPrice(); // 返回折扣后的订单价格
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        // 清空购物车中的所有商品并保存
        shoppingCart.getShoppingCartProducts().clear();
        shoppingCartRepository.save(shoppingCart);
    }
}
