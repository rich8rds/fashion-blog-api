package com.richards.blog.controller;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.repository.LikeRepository;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.service.UserService;
import com.richards.blog.service.LikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@ExtendWith(MockitoExtension.class)
class LikeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LikeController likeController;

    @MockBean
    AdminController adminController;
    @MockBean
    CommentController commentController;

    @MockBean
    LikeService likeService;
    @MockBean
    ProductRepository productRepository;

    @MockBean
    LikeRepository likeRepository;
    @MockBean
    UserService userService;
    @MockBean
    HttpSession session;

    @Test
    void likeOrUnlikeProduct() throws Exception {
        Long id = 2L;
        ApiResponse<String> apiResponse =
                new ApiResponse<>("Post Liked!",
                        HttpStatus.OK,
                "PICTURE LIKED!");

        when(likeController.likeOrUnlikeProduct(id)).thenReturn(apiResponse);

        mockMvc.perform(post("/api/v1/products/{productId}/like", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is("PICTURE LIKED!")))
                .andReturn();

        verify(likeController).likeOrUnlikeProduct(id);
    }

    @Test
    void getLikeCount() throws Exception {
        Long id = 2L;
        ApiResponse<Integer> apiResponse =
                new ApiResponse<>("Post Liked!", HttpStatus.OK, 10);

        when(likeController.getLikeCount(id)).thenReturn(apiResponse);

        mockMvc.perform(get("/api/v1/products/{productId}/count-likes", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(10)))
                .andReturn();

        verify(likeController).getLikeCount(id);

    }
}