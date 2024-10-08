package com.shoppingcart.springboot.interfacemethods;

import com.shoppingcart.springboot.model.ShoppingCartProduct;

public interface ShoppingCartProductInterface {

    // 根据购物车和产品 ID 获取购物车中的产品
    ShoppingCartProduct getShoppingCartProduct(Long cartId, Long productId);

    // 更新购物车中产品的数量
    void updateProductQuantity(Long cartId, Long productId, int quantity);

    // 从购物车中移除特定产品
    void removeProductFromCart(Long cartId, Long productId);
}
