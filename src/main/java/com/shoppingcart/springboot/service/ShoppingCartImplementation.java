package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.interfacemethods.ShoppingCartInterface;
import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;
import com.shoppingcart.springboot.model.ShoppingCartProduct;
import com.shoppingcart.springboot.repository.ShoppingCartProductRepository;
import com.shoppingcart.springboot.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShoppingCartImplementation implements ShoppingCartInterface {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;

    @Autowired
    public ShoppingCartImplementation(ShoppingCartRepository shoppingCartRepository,
                                      ShoppingCartProductRepository shoppingCartProductRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
    }

    @Override
    public ShoppingCart getShoppingCartByCustomer(Customer customer) {
        return shoppingCartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found for customer ID: " + customer.getCustomerId()));
    }

    @Override
    public ShoppingCart addProductToCart(Customer customer, Product product, int quantity) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);

        Optional<ShoppingCartProduct> existingProduct = shoppingCart.getShoppingCartProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(product))
                .findFirst();

        if (existingProduct.isPresent()) {
            ShoppingCartProduct shoppingCartProduct = existingProduct.get();
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + quantity);
        } else {
            ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setQuantity(quantity);
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCartProductRepository.save(shoppingCartProduct);
            shoppingCart.getShoppingCartProducts().add(shoppingCartProduct);
        }

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromCart(Customer customer, Product product) {
        ShoppingCart shoppingCart = getShoppingCartByCustomer(customer);
        shoppingCart.getShoppingCartProducts().removeIf(
                cartProduct -> cartProduct.getProduct().equals(product)
        );
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart createShoppingCart(Customer customer) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setCustomer(customer);
        return shoppingCartRepository.save(newShoppingCart);
    }
}




