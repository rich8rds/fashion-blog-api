package com.richards.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.CommentDto;
import com.richards.blog.entity.Comment;
import com.richards.blog.entity.Product;
import com.richards.blog.entity.User;
import com.richards.blog.enums.Category;
import com.richards.blog.repository.AdminRepository;
import com.richards.blog.repository.CommentRepository;
import com.richards.blog.repository.ProductRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
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
    CommentRepository commentRepository;
    @MockBean
    AdminRepository adminRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    HttpSession session;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addNewComment() throws Exception {
        Long productId = 2L;

        Product product = Product.builder()
                .name("BLACK NIGHT GOWN")
                .description("Fantastic to wear")
                .category(Category.CASUAL_WEAR)
                .imageUrl("imageUrl")
                .build();


        CommentDto commentDto = CommentDto.builder()
                .email("richie@gmail.com")
                .username("rickman")
                .comment("This is a great piece. Can't wait to see more.")
                .build();

        Comment comment = Comment.builder()
                .comment(commentDto.getComment())
                .product(product)
                .user(User.builder()
                        .email(commentDto.getEmail())
                        .username(commentDto.getUsername()).build())
                .build();

        when(commentRepository.save(comment)).thenReturn(comment);

        ApiResponse<Comment> apiResponse =
        new ApiResponse<>("Comment Added", HttpStatus.CREATED, commentRepository.save(comment));

        when(commentController.addNewComment(productId, commentDto)).thenReturn(apiResponse);
        mockMvc.perform(
                post("/api/v1/products/{productId}/comments/new",
                        productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment Added")))
                .andReturn();

        verify(commentController).addNewComment(productId, commentDto);
    }

    @Test
    void updateComment() throws Exception {
        Long productId = 2L;

        Product product = Product.builder()
                .name("BLACK NIGHT GOWN")
                .description("Fantastic to wear")
                .category(Category.CASUAL_WEAR)
                .imageUrl("imageUrl")
                .build();


        CommentDto commentDto = CommentDto.builder()
                .email("richie@gmail.com")
                .username("rickman")
                .comment("This is a great piece. Can't wait to see more.")
                .build();

        Comment comment = Comment.builder()
                .comment(commentDto.getComment())
                .product(product)
                .user(User.builder()
                        .email(commentDto.getEmail())
                        .username(commentDto.getUsername()).build())
                .build();

        when(commentRepository.save(comment)).thenReturn(comment);

        ApiResponse<Comment> apiResponse =
                new ApiResponse<>("Comment updated successfully", HttpStatus.CREATED, commentRepository.save(comment));

        when(commentController.updateComment(productId, 4L, commentDto)).thenReturn(apiResponse);
        mockMvc.perform(
                        put("/api/v1/products/{productId}/comments/{commentID}/update",
                                productId, 4L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment updated successfully")))
                .andReturn();

        verify(commentController).updateComment(productId, 4L, commentDto);
    }

    @Test
    void deleteComment() throws Exception {
        Long productId = 4L;
        Long commentId = 20L;

        ApiResponse<String> apiResponse =
                new ApiResponse<>("Comment deleted successfully",
                        HttpStatus.OK, "COMMENT DELETED");

        when(commentController.deleteComment(productId, commentId)).thenReturn(apiResponse);
        mockMvc.perform(
                        delete("/api/v1/products/{productId}/comments/{commentId}/delete",
                                productId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment deleted successfully")))
                .andReturn();

    }
}