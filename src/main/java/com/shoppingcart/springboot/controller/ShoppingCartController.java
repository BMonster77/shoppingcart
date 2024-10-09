package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;
import com.shoppingcart.springboot.repository.ProductRepository;
import com.shoppingcart.springboot.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shoppingcart.springboot.repository.CustomerRepository;


@RestController
@RequestMapping("/api")
public class ShoppingCartController {

    private final ShoppingService shoppingService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ShoppingCartController(ShoppingService shoppingService, ProductRepository productRepository,
                                  CustomerRepository customerRepository) {
        this.shoppingService = shoppingService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/view-cart")
    public ResponseEntity<ShoppingCart> viewShoppingCart(@RequestParam Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        ShoppingCart shoppingCart = customer.getShoppingCart();
        return ResponseEntity.ok(shoppingCart);
    }

    @RequestMapping(value = "/add-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ShoppingCart> addProductToCart(@RequestParam Long customerId, @RequestParam Long productId,
                                                         @RequestParam int quantity) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStoreQuantity() < quantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            ShoppingCart shoppingCart = shoppingService.addProductToCart(customer, product, quantity);
            return ResponseEntity.ok(shoppingCart);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/remove-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<ShoppingCart> removeProductFromCart(@RequestParam Long customerId, @RequestParam Long productId) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ShoppingCart shoppingCart = shoppingService.removeProductFromCart(customer, product);
            return ResponseEntity.ok(shoppingCart);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/create-order", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Order order = shoppingService.createOrderFromCart(customer);
            return ResponseEntity.ok(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/complete-payment", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> completePayment(@RequestParam Long orderId) {
        try {
            Order order = shoppingService.findOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            shoppingService.completePayment(order);
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


