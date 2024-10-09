package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.model.*;
import com.shoppingcart.springboot.repository.ProductRepository;
import com.shoppingcart.springboot.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shoppingcart.springboot.repository.CustomerRepository;

import java.util.Optional;


@RestController // 标注这个类为一个 RESTful 控制器，用于接收并处理 HTTP 请求
@RequestMapping("/api") // 基础请求路径，定义类中的所有请求路径前缀为 /api，例如完整路径为 http://localhost:8080/api
public class ShoppingCartController {

    private final ShoppingService shoppingService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired // 构造器注入依赖服务和仓库，确保控制器的依赖清晰和易于测试
    public ShoppingCartController(ShoppingService shoppingService, ProductRepository productRepository,
                                  CustomerRepository customerRepository) {
        this.shoppingService = shoppingService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    // ========================= 购物车相关接口 =========================

    /**
     * 查看购物车接口
     * 完整路径：GET http://localhost:8080/api/view-cart?customerId=1
     *
     * @param customerId 客户ID
     * @return 客户的购物车信息
     */
    @GetMapping("/view-cart")
    public ResponseEntity<ShoppingCart> viewShoppingCart(@RequestParam Long customerId) {
        // 查找客户并返回其购物车信息
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        ShoppingCart shoppingCart = customer.getShoppingCart();
        return ResponseEntity.ok(shoppingCart);
    }

    /**
     * 添加商品到购物车接口
     * 完整路径：POST http://localhost:8080/api/add-product?customerId=1&productId=2&quantity=2
     *
     * @param customerId 客户ID
     * @param productId  商品ID
     * @param quantity   添加的商品数量
     * @return 更新后的购物车信息
     */
    @RequestMapping(value = "/add-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ShoppingCart> addProductToCart(@RequestParam Long customerId,
                                                         @RequestParam Long productId,
                                                         @RequestParam int quantity) {
        try {
            // 查找客户和产品信息
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 检查库存是否充足
            if (product.getStoreQuantity() < quantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // 添加商品到购物车并返回更新后的购物车信息
            ShoppingCart shoppingCart = shoppingService.addProductToCart(customer, product, quantity);
            return ResponseEntity.ok(shoppingCart);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 从购物车中移除指定数量商品的接口
     * 完整路径：POST http://localhost:8080/api/remove-product?customerId=1&productId=2&quantity=1
     *
     * @param customerId 客户ID
     * @param productId  商品ID
     * @param quantity   要移除的商品数量
     * @return 更新后的购物车信息
     */
    @RequestMapping(value = "/remove-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ShoppingCart> removeProductFromCart(@RequestParam Long customerId,
                                                              @RequestParam Long productId,
                                                              @RequestParam int quantity) {
        try {
            // 查找客户和产品信息
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 从购物车中移除指定数量的商品
            ShoppingCart shoppingCart = shoppingService.getShoppingCartByCustomer(customer);
            Optional<ShoppingCartProduct> cartProductOpt = shoppingCart.getShoppingCartProducts().stream()
                    .filter(cartProduct -> cartProduct.getProduct().equals(product))
                    .findFirst();

            if (cartProductOpt.isPresent()) {
                ShoppingCartProduct cartProduct = cartProductOpt.get();
                int updatedQuantity = cartProduct.getQuantity() - quantity;

                if (updatedQuantity > 0) {
                    cartProduct.setQuantity(updatedQuantity); // 更新商品数量
                } else {
                    shoppingCart.getShoppingCartProducts().remove(cartProduct); // 移除商品
                }

                shoppingService.saveShoppingCart(shoppingCart); // 保存购物车状态
                return ResponseEntity.ok(shoppingCart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 购物车中未找到商品
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ========================= 订单相关接口 =========================

    /**
     * 创建订单接口
     * 完整路径：POST http://localhost:8080/api/create-order?customerId=1
     *
     * @param customerId 客户ID
     * @return 创建的订单详情
     */
    @RequestMapping(value = "/create-order", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId) {
        try {
            // 查找客户并根据购物车创建订单
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Order order = shoppingService.createOrderFromCart(customer);
            return ResponseEntity.ok(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 完成订单支付的接口，支持使用代金券
     * 完整路径：POST http://localhost:8080/api/complete-payment?orderId=1&voucherCode=DISCOUNT2024
     *
     * @param orderId    订单ID
     * @param voucherCode 代金券代码（可选）
     * @return 支付完成状态或错误信息
     */
    @RequestMapping(value = "/complete-payment", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> completePayment(@RequestParam Long orderId,
                                                  @RequestParam(required = false) String voucherCode) {
        try {
            // 查找订单
            Order order = shoppingService.findOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // 检查订单是否已经支付
            if ("PAID".equalsIgnoreCase(order.getStatus())) {
                // 如果订单已经支付，则返回一个错误响应
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order has already been paid.");
            }

            // 完成订单支付
            shoppingService.completePayment(order, voucherCode);
            return ResponseEntity.ok("Order payment completed successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment processing.");
        }
    }
}













//    // 获取用户购物车的内容
//    @GetMapping("/cart/{userId}")
//    public ResponseEntity<Cart> getCartContents(@PathVariable Long userId) {
//        Cart cart = cartService.getCartByUserId(userId);
//        if (cart == null) {
//            return ResponseEntity.notFound().build(); // 如果购物车不存在，返回404
//        }
//        return ResponseEntity.ok(cart); // 返回购物车的内容
//    }


//    @PostMapping("/cart/add")
//    public String addProductToCart(@RequestParam Long productId, @RequestParam Long userId) {
//        Product product = productService.getProductById(productId);
//        Cart cart = cartService.getCartByUserId(userId);
//        if (product != null && cart != null) {
//            shoppingCartService.addProductToCart(cart, product);
//            return "Success";
//        }
//        return "Fail";
//    }

//    @PostMapping("/cart/checkout")
//    public String checkout(@RequestParam Long userId) {
//        Cart cart = cartService.getCartByUserId(userId);
//        if (cart != null) {
//            boolean paymentSuccess = simulatePayment(userId); // 模拟支付逻辑
//            if (paymentSuccess) {
//                purchaseHistoryService.createPurchaseHistory(cart);
//                cartService.clearCart(cart);
//                return "Checkout and payment successful!";
//            } else {
//                return "Payment failed!";
//            }
//        }
//        return "Checkout failed.";
//    }

//    private boolean simulatePayment(Long userId) {
//        // 模拟支付网关或数字钱包支付
//        return true; // 支付成功返回 true
//    }

//    @PostMapping("/cart/add")
//    public String deleteProductToCart(@RequestParam Long productId, @RequestParam Long userId) {
//        Product product = productService.getProductById(productId);
//        Cart cart = cartService.getCartByUserId(userId);
//        if (product != null && cart != null) {
//            shoppingCartService.addProductToCart(cart, product);
//            return "Success";
//        }
//        return "Fail";
//    }


