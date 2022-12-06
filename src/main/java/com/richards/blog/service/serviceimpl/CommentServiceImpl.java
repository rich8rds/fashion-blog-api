package com.richards.blog.service.serviceimpl;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.CommentDto;
import com.richards.blog.dto.AddUserDto;
import com.richards.blog.entity.Comment;
import com.richards.blog.entity.User;
import com.richards.blog.entity.Product;
import com.richards.blog.exceptions.FieldBlankException;
import com.richards.blog.exceptions.ProductNotFoundException;
import com.richards.blog.exceptions.UnAuthorizedException;
import com.richards.blog.repository.CommentRepository;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.repository.UserRepository;
import com.richards.blog.service.CommentService;
import com.richards.blog.service.UserService;
import com.richards.blog.session.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final HttpSession session;
    @Override
    public ApiResponse<Comment> addNewComment(Long productId, CommentDto commentDto) {
        Long userId = UserInfo.getUserSessionId(session);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product does not exist!"));

        String commentStr = commentDto.getComment();
        String email = commentDto.getEmail();
        String username = commentDto.getUsername();

        if(commentStr == null || commentStr.isBlank())
            throw new FieldBlankException("Please fill in the field to post comment.");
        if(email == null || email.isBlank()) throw new FieldBlankException("Email Cannot Be Blank");
        if(username == null || username.isBlank()) throw new FieldBlankException("Username Cannot Be Blank");

        System.out.println("INSIDE THIS BELLY 000 => userId: " + userId);
        if(!userId.equals(0L)) {
            System.out.println("INSIDE THIS BELLY 0001");
            User user = UserInfo.getUser(session);
            if(user.getUsername().equals("GUEST")){
                user.setEmail(email);
                user.setUsername(username);
            }
            User passedUser = user;
            //Find if the email exists already
            Optional<User> userOptional = userRepository.findByEmail(email);

            if(userOptional.isPresent()) {
                //Find oldUser details and delete but update the new one.
                System.out.println("INSIDE THE BELLY");
                commentRepository.deleteById(userId);
                passedUser = userOptional.get();
            }

            userRepository.save(passedUser);

            Comment comment = Comment.builder().comment(commentStr)
                    .product(product)
                    .user(passedUser)
                    .build();

            return new ApiResponse<>("Comment Added", HttpStatus.CREATED, commentRepository.save(comment));
        }

        AddUserDto addUserDto = AddUserDto.builder().email(email).username(username).build();
        ApiResponse<User> apiResponse = userService.registerNewUser(addUserDto);

        System.out.println("MESSAGE: " + apiResponse.getMessage());
        User newUser = apiResponse.getData();

        if(newUser == null) try {
            throw new ValidationException(apiResponse.getMessage());
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        System.out.println("USER: " + newUser);

        Comment comment = Comment.builder().comment(commentStr)
                .product(product)
                .user(newUser)
                .build();

        return new ApiResponse<>("Comment Added", HttpStatus.CREATED, commentRepository.save(comment));
    }

    @Override
    public ApiResponse<Comment> updateComment(Long productId, Long commentId, CommentDto commentDto) {
        Long userId = UserInfo.userInSessionOrElseThrow(session);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment does not exist!"));

        if(!productRepository.existsById(productId))
                throw new ProductNotFoundException("Product does not exist!");

        Long commentUserId = comment.getUser().getId();
        Long commentProductId = comment.getProduct().getId();

        if(!userId.equals(commentUserId) || !productId.equals(commentProductId))
            throw new UnAuthorizedException("You can only update your own comment.");

        comment.setComment(commentDto.getComment());
        return new ApiResponse<>("Comment updated successfully", HttpStatus.OK, commentRepository.save(comment));
    }

    @Override
    public ApiResponse<String> deleteComment(Long productId, Long commentId) {
        Long userId = UserInfo.userInSessionOrElseThrow(session);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment does not exist!"));

        Long commentUserId = comment.getUser().getId();
        Long commentProductId = comment.getProduct().getId();

        if(!userId.equals(commentUserId) || !productId.equals(commentProductId))
            throw new UnAuthorizedException("You can only delete your own comment.");

        commentRepository.deleteById(commentId);

        return new ApiResponse<>("Comment deleted successfully", HttpStatus.OK, "COMMENT DELETED");
    }
}
