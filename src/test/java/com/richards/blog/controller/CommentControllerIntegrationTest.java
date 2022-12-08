package com.richards.blog.controller;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.AddUserDto;
import com.richards.blog.dto.CommentDto;
import com.richards.blog.entity.Comment;
import com.richards.blog.entity.Product;
import com.richards.blog.enums.Category;
import com.richards.blog.exceptions.FieldBlankException;
import com.richards.blog.exceptions.ProductNotFoundException;
import com.richards.blog.exceptions.SessionIdNotFoundException;
import com.richards.blog.repository.CommentRepository;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.repository.UserRepository;
import com.richards.blog.service.AdminService;
import com.richards.blog.service.CommentService;
import com.richards.blog.service.UserService;
import com.richards.blog.session.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CommentControllerIntegrationTest {
    @Autowired
    private CommentService commentService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @Mock
    private HttpSession session;

    @MockBean
    AdminService adminService;
    User user, guestUser;
    Product product;
    Comment comment;
    CommentDto commentDto;
    Long productId = 2L;
    @Autowired
    private HttpSession realSession;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("richards")
                .email("richie@gmail.com")
                .build();

        user.setId(2L);

        guestUser = User.builder().username("GUEST").email(null).build();
        guestUser.setId(4L);

        product = Product.builder()
                .name("BLACK NIGHT GOWN")
                .description("Fantastic to wear")
                .category(Category.CASUAL_WEAR)
                .imageUrl("imageUrl")
                .build();

        commentDto = CommentDto.builder()
                .email("richie@gmail.com")
                .username("rickman")
                .comment("This is a great piece. Can't wait to see more.")
                .build();

        comment = Comment.builder()
                .comment(commentDto.getComment())
                .product(product)
                .user(User.builder()
                        .email(commentDto.getEmail())
                        .username(commentDto.getUsername()).build())
                .build();
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException")
    public void addNewCommentShouldThrowProductNotFoundException() {
        assertThrows(ProductNotFoundException.class,
                () -> commentService.addNewComment(productId, commentDto));
    }

    @Test
    @DisplayName("Should return success for a user who has commented before.")
    public void addNewCommentShouldReturnAValidApiResponse() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        String email = commentDto.getEmail();
        String username = commentDto.getUsername();
        AddUserDto addUserDto = AddUserDto.builder().email(email).username(username).build();

        when(userService.registerNewUser(addUserDto))
                .thenReturn(new ApiResponse<>(
                        "Registration Successful",
                        HttpStatus.CREATED,
                        user));

        ApiResponse<Comment> apiResponse = new ApiResponse<>(
                "Comment Added",
                HttpStatus.CREATED,
                commentRepository.save(comment));
        assertEquals(commentService.addNewComment(productId, commentDto), apiResponse);
    }


    @Test
    @DisplayName("Should return success for an unregistered user (GUEST).")
    public void addNewCommentShouldReturnAValidApiResponseForGuest() {
        realSession.setAttribute("userDetails", guestUser);
        assertEquals(4L, UserInfo.getUserSessionId(realSession));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        String email = commentDto.getEmail();
        when(session.getAttribute("userDetails")).thenReturn(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ApiResponse<Comment> apiResponse = new ApiResponse<>(
                "Comment Added",
                HttpStatus.CREATED,
                commentRepository.save(comment));
        assertEquals(commentService.addNewComment(productId, commentDto), apiResponse);

    }

    @Test
    public void addNewCommentShouldThrowFieldBlankException() {
        when(session.getAttribute("userDetails")).thenReturn(user);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        commentDto.setComment(null);
        commentDto.setUsername(null);
//        commentDto.setEmail(null);

        assertThrows(FieldBlankException.class,
                () -> commentService.addNewComment(productId, commentDto));
    }

    @Test
    public void updateCommentShouldThrowSessionIdNotFoundException() {
        Long commentId = 5L;
       realSession.setAttribute("userDetails", null);

        assertThrows(SessionIdNotFoundException.class,
                () -> commentService.updateComment(productId, commentId, commentDto));
    }

    @Test
    public void updateComment() {
        Long commentId = 5L;
        realSession.setAttribute("userDetails", user);

        when(commentRepository.findById(commentId)).thenReturn(null);
    }


}
