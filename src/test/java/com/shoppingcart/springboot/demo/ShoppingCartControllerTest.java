package com.shoppingcart.springboot.demo;

import com.shoppingcart.springboot.controller.ShoppingCartController;
import com.shoppingcart.springboot.model.Customer;
import com.shoppingcart.springboot.model.Product;
import com.shoppingcart.springboot.model.ShoppingCart;
import com.shoppingcart.springboot.model.User;
import com.shoppingcart.springboot.repository.ProductRepository;
import com.shoppingcart.springboot.service.ShoppingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingService shoppingService;

    private Customer mockCustomer;
    private Product mockProduct;

    @BeforeEach
    void setup() {
        // 初始化模拟数据
        mockCustomer = new Customer();
        mockCustomer.setCustomerId(1L);

        // 创建 User 对象并关联到 Customer
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("johndoe@example.com");

        mockCustomer.setUser(mockUser); // 将用户关联到 Customer

        mockProduct = new Product();
        mockProduct.setProductId(1L);
        mockProduct.setName("Sample Product");
        mockProduct.setStoreQuantity(10);
    }

    @Test
    public void testViewShoppingCart() throws Exception {
        // 设置 Mock 行为
        Mockito.when(shoppingService.getShoppingCartByCustomer(mockCustomer)).thenReturn(new ShoppingCart());

        // 模拟 GET 请求
        mockMvc.perform(get("/api/view-cart")
                        .param("customerId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("")); // 可以根据实际数据结构调整
    }

    @Test
    public void testAddProductToCart() throws Exception {
        // 模拟添加商品的 POST 请求
        mockMvc.perform(post("/api/add-product")
                        .param("customerId", "1")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveProductFromCart() throws Exception {
        // 模拟移除商品的 POST 请求
        mockMvc.perform(post("/api/remove-product")
                        .param("customerId", "1")
                        .param("productId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateOrder() throws Exception {
        // 模拟创建订单的 POST 请求
        mockMvc.perform(post("/api/create-order")
                        .param("customerId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCompletePayment() throws Exception {
        // 模拟完成订单支付的 POST 请求
        mockMvc.perform(post("/api/complete-payment")
                        .param("orderId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}