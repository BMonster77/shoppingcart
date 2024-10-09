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


@RestController // 标注这个类是一个 RESTful 控制器，能够接收并处理 HTTP 请求
@RequestMapping("/api") // 定义了类中的所有请求路径的基础路径为 /api，例如完整路径为 http://localhost:8080/api
public class ShoppingCartController {

    private final ShoppingService shoppingService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired // 使用构造器注入所需的服务和仓库（repository），以保证类的依赖性
    public ShoppingCartController(ShoppingService shoppingService, ProductRepository productRepository,
                                  CustomerRepository customerRepository) {
        this.shoppingService = shoppingService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    // 查看购物车的接口
    @GetMapping("/view-cart") // 完整路径为: GET http://localhost:8080/api/view-cart?customerId=1
    public ResponseEntity<ShoppingCart> viewShoppingCart(@RequestParam Long customerId) {
        // 根据客户 ID 查找客户，如果找不到则抛出异常
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        // 获取客户的购物车并返回
        ShoppingCart shoppingCart = customer.getShoppingCart();
        return ResponseEntity.ok(shoppingCart);
    }

    // 添加商品到购物车的接口
    @RequestMapping(value = "/add-product", method = {RequestMethod.POST, RequestMethod.GET}) // 完整路径为: POST http://localhost:8080/api/add-product?customerId=1&productId=2&quantity=2
    public ResponseEntity<ShoppingCart> addProductToCart(@RequestParam Long customerId, @RequestParam Long productId,
                                                         @RequestParam int quantity) {
        try {
            // 查找客户和产品，如果找不到则抛出异常
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 检查库存是否充足
            if (product.getStoreQuantity() < quantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // 添加商品到购物车并返回更新后的购物车
            ShoppingCart shoppingCart = shoppingService.addProductToCart(customer, product, quantity);
            return ResponseEntity.ok(shoppingCart);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 从购物车中移除指定数量商品的接口
    @RequestMapping(value = "/remove-product", method = {RequestMethod.POST, RequestMethod.GET}) // 完整路径为: POST http://localhost:8080/api/remove-product?customerId=1&productId=2&quantity=1
    public ResponseEntity<ShoppingCart> removeProductFromCart(@RequestParam Long customerId,
                                                              @RequestParam Long productId,
                                                              @RequestParam int quantity) {
        try {
            // 查找客户和产品，如果找不到则抛出异常
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 从购物车中减少指定数量的商品
            ShoppingCart shoppingCart = shoppingService.getShoppingCartByCustomer(customer);
            Optional<ShoppingCartProduct> cartProductOpt = shoppingCart.getShoppingCartProducts().stream()
                    .filter(cartProduct -> cartProduct.getProduct().equals(product))
                    .findFirst();

            if (cartProductOpt.isPresent()) {
                ShoppingCartProduct cartProduct = cartProductOpt.get();
                int updatedQuantity = cartProduct.getQuantity() - quantity;

                if (updatedQuantity > 0) {
                    // 如果剩余数量大于0，更新数量
                    cartProduct.setQuantity(updatedQuantity);
                } else {
                    // 如果剩余数量小于或等于0，移除该商品
                    shoppingCart.getShoppingCartProducts().remove(cartProduct);
                }

                shoppingService.saveShoppingCart(shoppingCart); // 使用服务保存购物车状态
                return ResponseEntity.ok(shoppingCart);
            } else {
                // 如果购物车中没有找到该商品
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 创建订单的接口
    @RequestMapping(value = "/create-order", method = {RequestMethod.POST, RequestMethod.GET}) // 完整路径为: POST http://localhost:8080/api/create-order?customerId=1
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId) {
        try {
            // 查找客户，如果找不到则抛出异常
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            // 从购物车创建订单并返回订单详情
            Order order = shoppingService.createOrderFromCart(customer);
            return ResponseEntity.ok(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 完成订单支付的接口，支持使用代金券
    @RequestMapping(value = "/complete-payment", method = {RequestMethod.POST, RequestMethod.GET}) // 完整路径为: POST http://localhost:8080/api/complete-payment?orderId=1&voucherCode=DISCOUNT2024
    public ResponseEntity<String> completePayment(@RequestParam Long orderId, @RequestParam(required = false) String voucherCode) {
        try {
            // 查找订单，如果找不到则抛出异常
            Order order = shoppingService.findOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // 完成订单支付并应用代金券（如果提供了代金券代码）
            shoppingService.completePayment(order, voucherCode);
            return ResponseEntity.ok("Order payment completed successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or already paid.");
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


