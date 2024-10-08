package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.exception.ShoppingCartNotFoundException;
import com.shoppingcart.springboot.interfacemethods.ShoppingCartInterface;
import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;
import com.shoppingcart.springboot.repository.CustomerRepository;
import com.shoppingcart.springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartInterface shoppingCartService;
    private final ProductRepository productRepository;

    @Autowired
    public ShoppingCartController(ShoppingCartInterface shoppingCartService, ProductRepository productRepository) {
        this.shoppingCartService = shoppingCartService;
        this.productRepository = productRepository;
    }

    // 查看购物车页面
    @GetMapping
    public String viewShoppingCart(@RequestParam Customer customer, Model model) {
        try {
            // 根据 customer 获取购物车对象
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomer(customer);

            model.addAttribute("shoppingCart", shoppingCart);
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", "购物车不存在，请先添加商品到购物车。");
        }
        return "shoppingCart"; // 返回到 Thymeleaf 模板名为 shoppingCart.html 的页面
    }

    // 将产品添加到购物车
    @PostMapping("/add-product")
    public String addProductToCart(@RequestParam Customer customer, @RequestParam Long productId,
                                   @RequestParam int quantity, Model model) {
        try {
            // 通过 productId 获取产品对象
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 检查库存是否足够
            if (product.getStoreQuantity() < quantity) {
                model.addAttribute("errorMessage", "库存不足，无法添加商品到购物车。");
                return "shoppingCart"; // 返回到购物车页面，并显示库存不足的错误信息
            }
            // 根据 customer 获取购物车对象
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomer(customer);

            // 将产品添加到购物车
            shoppingCart = shoppingCartService.addProductToCart(customer, product, quantity);
            model.addAttribute("shoppingCart", shoppingCart);

        } catch (Exception ex) {
            model.addAttribute("errorMessage", "无法添加商品，请稍后重试。");
        }
        return "shoppingCart"; // 返回到 Thymeleaf 模板名为 shoppingCart.html 的页面
    }

    // 从购物车中移除产品
    @PostMapping("/remove-product")
    public String removeProductFromCart(@RequestParam Customer customer, @RequestParam Long productId, Model model) {
        try {
            // 根据产品 ID 获取产品对象
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 根据 customer 获取购物车对象
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomer(customer);

            // 从购物车中移除产品
            shoppingCart = shoppingCartService.removeProductFromCart(customer, product);
            model.addAttribute("shoppingCart", shoppingCart);
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", "购物车或产品不存在，无法移除商品。");
        }
        return "shoppingCart"; // 返回到 Thymeleaf 模板名为 shoppingCart.html 的页面
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


