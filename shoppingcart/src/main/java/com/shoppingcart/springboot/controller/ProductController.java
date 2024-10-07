package com.shoppingcart.springboot.controller;

import com.shoppingcart.springboot.interfacemethods.ProductInterface;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.service.ProductImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductInterface productService;

//    @RequestMapping("/product/getProductsByPage")
//    public List<Product> browseAllProducts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        return productService.getProductsByPage(page, size);  // 分页实现
//    }

//    // 获取某一商品的具体信息
//    @GetMapping("/products/{productId}")
//    public ResponseEntity<Product> getProductDetails(@PathVariable Long productId) {
//        Product product = productService.getProductById(productId);
//        if (product == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(null); // 商品未找到，返回404
//        }
//        return ResponseEntity.ok(product); // 返回商品的具体信息
//    }







    //method to display a product for deletion

}
