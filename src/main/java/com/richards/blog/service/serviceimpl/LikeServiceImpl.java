package com.richards.blog.service.serviceimpl;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.AddUserDto;
import com.richards.blog.entity.User;
import com.richards.blog.entity.Like;
import com.richards.blog.entity.Product;
import com.richards.blog.exceptions.ProductNotFoundException;
import com.richards.blog.repository.LikeRepository;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.repository.UserRepository;
import com.richards.blog.service.LikeService;
import com.richards.blog.service.UserService;
import com.richards.blog.session.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final ProductRepository productRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public ApiResponse<String> likeOrUnlikeProduct(Long productId) {
        Long userId = UserInfo.getUserSessionId(session);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found!"));

        if(userId.equals(0L)) {
            User addUserDto = User.builder()
                    .email(null)
                    .username("GUEST")
                    .build();

            User newUser = userRepository.save(addUserDto);
            session.setAttribute("userDetails", newUser);

            Like like = Like.builder()
                    .product(product)
                    .user(newUser)
                    .build();

            likeRepository.save(like);
            return new ApiResponse<>("Post Liked!", HttpStatus.OK, "PICTURE LIKED!");
        }

        Optional<Like> likeOptional = likeRepository.findLikeByUserIdAndProductId(userId, productId);
        likeOptional.ifPresent(likeRepository::delete);
        if(likeOptional.isPresent())
            return new ApiResponse<>("Post Unliked!", HttpStatus.OK, "PRODUCT UNLIKED!");

        Like like = Like.builder().product(product)
                .user(UserInfo.getUser(session)).build();
        likeRepository.save(like);

        return new ApiResponse<>("Post Liked!", HttpStatus.OK, "PICTURE LIKED!");
    }

    @Override
    public ApiResponse<Integer> getLikes(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found!"));
        return new ApiResponse<>("Post Liked!", HttpStatus.OK, product.getLikes().size());
    }
}
