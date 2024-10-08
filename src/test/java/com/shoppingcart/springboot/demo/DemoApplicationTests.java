package com.shoppingcart.springboot.demo;

import com.shoppingcart.springboot.controller.ShoppingCartController;
import com.shoppingcart.springboot.interfacemethods.ShoppingCartInterface;
import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;
import com.shoppingcart.springboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartInterface shoppingCartService;

    @MockBean
    private ProductRepository productRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    public void setup() {
        // 初始化测试数据
        customer = new Customer();
        customer.setCustomerId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setStoreQuantity(10);
    }

    @Test
    public void testViewShoppingCart() throws Exception {
        Mockito.when(shoppingCartService.getShoppingCartByCustomer(customer))
                .thenReturn(new ShoppingCart());

        mockMvc.perform(get("/shopping-cart")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("shoppingCart"));
    }

    @Test
    public void testAddProductToCart_InsufficientStock() throws Exception {
        Mockito.when(productRepository.findByProductId(1L)).thenReturn(java.util.Optional.of(product));
        product.setStoreQuantity(1); // 库存不足的情况

        mockMvc.perform(post("/shopping-cart/add-product")
                        .param("customerId", "1")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("shoppingCart"));
    }

}
