package com.richards.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.AdminDto;
import com.richards.blog.dto.ProductDto;
import com.richards.blog.entity.Product;
import com.richards.blog.entity.User;
import com.richards.blog.enums.Category;
import com.richards.blog.enums.Role;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AdminController adminController;

    @MockBean
    CommentController commentController;

    @MockBean
    LikeController likeController;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    HttpSession session;

    private List<Product> products;

    private User admin;


    @BeforeEach
    void setUp() {
        admin  = User.builder()
                .email("richards@gmail.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        products = new ArrayList<>(List.of(
                Product.builder()
                    .name("POLO T-SHIRT")
                    .description("Fantastic to wear")
                    .category(Category.CASUAL_WEAR)
                    .imageUrl("imageUrl")
                    .build(),
                Product.builder()
                    .name("BLACK NIGHT GOWN")
                    .description("Fantastic to wear")
                    .category(Category.CASUAL_WEAR)
                    .imageUrl("imageUrl")
                    .build()
        ));

    }

//    @Test
//    @DisplayName("SUCCESS")
//    void shouldGetAllProducts() throws Exception {
//        Optional<Integer> page = Optional.of(2);
//        Page<Product> pageItem = productRepository.findAll(PageRequest.of(page.orElse(0),
//                5, Sort.Direction.ASC, "id"));
//
//        ApiResponse<Page<Product>> apiResponse =
//                new ApiResponse<>(("Page " + page.get()), HttpStatus.OK, pageItem);
//
//        when(adminController.getAllProducts(page,  null)).thenReturn(apiResponse);
//
//        mockMvc.perform(get("/api/v1/admin/products/all"))
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.data", is(pageItem)))
//                .andReturn();
//
//        verify(adminController).getAllProducts(Optional.of(2), null);
//    }



    @Test
    void adminLogin() {
    }

    @Test
    void createNewAdmin() throws Exception {
        AdminDto adminDto = AdminDto.builder()
                .email("richards@gmail.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        when(userRepository.save(admin)).thenReturn(admin);

        ApiResponse<User> apiResponse =
                new ApiResponse<>("Created Successfully",
                HttpStatus.CREATED,
                userRepository.save(admin));

        when(adminController.createNewAdmin(adminDto)).thenReturn(apiResponse);

        mockMvc.perform(post("/api/v1/admin/auth/create-new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.message",
                        is("Created Successfully")));

        verify(adminController).createNewAdmin(adminDto);
    }

    @Test
    void addNewProduct() throws Exception {
        Product product = products.get(0);
        when(productRepository.save(product)).thenReturn(product);

        ApiResponse<Product> apiResponse =
                new ApiResponse<>("Product Uploaded Successfully",
                        HttpStatus.CREATED,
                        productRepository.save(product));

        ProductDto productDto = ProductDto.builder()
                .name("POLO T-SHIRT")
                .description("Fantastic to wear")
                .category(Category.CASUAL_WEAR)
                .imageUrl("imageUrl")
                .build();

        when(adminController.addNewProduct(productDto)).thenReturn(apiResponse);
        mockMvc.perform(post("/api/v1/admin/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.message",
                        is("Product Uploaded Successfully")));

        verify(adminController).addNewProduct(productDto);
    }

    @Test
    void updateProduct() throws Exception{
        Product product = products.get(0);
        when(productRepository.save(product)).thenReturn(product);

        ApiResponse<Product> apiResponse =
                new ApiResponse<>("Product Uploaded Successfully",
                        HttpStatus.CREATED,
                        productRepository.save(product));

        ProductDto productDto = ProductDto.builder()
                .name("POLO T-SHIRT")
                .description("Fantastic to wear, Yes it is!!!")
                .category(Category.CASUAL_WEAR)
                .imageUrl("imageUrl")
                .build();

        when(adminController.updateProduct(2L, productDto)).thenReturn(apiResponse);
        mockMvc.perform(put("/api/v1/admin/products/{productId}/update", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.message",
                        is("Product Uploaded Successfully")))
                .andReturn();

        verify(adminController).updateProduct(2L, productDto);
    }

    @Test
    void deleteProduct() throws Exception {
        ApiResponse<String> apiResponse =
            new ApiResponse<>("Product Deleted Successfully",
                    HttpStatus.OK, "DELETED PRODUCT WITH ID: " + 2L);

        when(adminController.deleteProduct(2L)).thenReturn(apiResponse);
        mockMvc.perform(delete("/api/v1/admin/products/{productId}/delete", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        is("Product Deleted Successfully")))
                .andReturn();

        verify(adminController).deleteProduct(2L);

    }

    @Test
    void deleteAllProducts() throws Exception {
        ApiResponse<String> apiResponse =
                new ApiResponse<>("Products Deleted Successfully",
                        HttpStatus.OK, "DELETED ALL PRODUCTS");

        when(adminController.deleteAllProducts()).thenReturn(apiResponse);

        mockMvc.perform(delete("/api/v1/admin/products/delete-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        is("Products Deleted Successfully")))
                .andReturn();

        verify(adminController).deleteAllProducts();

    }
}