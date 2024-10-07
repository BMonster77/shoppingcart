package com.shoppingcart.springboot.controller;


import org.springframework.stereotype.Controller;

@Controller
public class AdminController {

//    // Add Product - 添加产品
//    @PostMapping("/admin/product/add")
//    public String addProduct(@RequestBody Product product) {
//        adminService.addProduct(product);
//        return "Product added successfully!";
//    }
//
//    // Edit Product - 编辑产品
//    @PutMapping("/admin/product/edit/{productId}")
//    public String editProduct(@PathVariable Long productId, @RequestBody Product productDetails) {
//        adminService.updateProduct(productId, productDetails);
//        return "Product updated successfully!";
//    }
//
//    // Delete Product - 删除产品
//    @DeleteMapping("/admin/product/delete/{productId}")
//    public String deleteProduct(@PathVariable Long productId) {
//        adminService.deleteProduct(productId);
//        return "Product deleted successfully!";
//    }
//
//    // Manage Stock - 库存管理 (隐藏缺货产品)
//    @PutMapping("/admin/product/manage-stock/{productId}")
//    public String manageStock(@PathVariable Long productId, @RequestParam int stock) {
//        adminService.updateStock(productId, stock);
//        if (stock == 0) {
//            adminService.hideProduct(productId); // 隐藏缺货产品
//        }
//        return "Stock updated successfully!";
//    }
}
