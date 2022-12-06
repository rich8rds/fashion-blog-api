package com.richards.blog.controller;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.entity.Product;
import com.richards.blog.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/products/{productId}/like")
    public ApiResponse<String> likeOrUnlikeProduct(@PathVariable Long productId) {
        return likeService.likeOrUnlikeProduct(productId);
    }

    @GetMapping("/products/{productId}/count-likes")
    public ApiResponse<Integer> getLikeCount(@PathVariable Long productId) {
        return likeService.getLikes(productId);
    }
}
