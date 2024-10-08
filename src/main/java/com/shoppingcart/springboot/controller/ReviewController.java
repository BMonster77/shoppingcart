package com.shoppingcart.springboot.controller;

import org.springframework.stereotype.Controller;

@Controller
public class ReviewController {


    @PostMapping("/products/{productId}/review")
    public String leaveReview(@PathVariable Long productId, @RequestParam Long userId,
                              @RequestBody Review review) {
        Product product = productService.getProductById(productId);
        User user = userService.getUserById(userId);
        if (product != null && user != null) {
            reviewService.addReview(product, user, review);
            return "Review added successfully!";
        }
        return "Failed to add review.";
    }
}
