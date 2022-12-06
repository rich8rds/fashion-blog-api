package com.richards.blog.service;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.entity.Product;

public interface LikeService {
    ApiResponse<String> likeOrUnlikeProduct(Long productId);

    ApiResponse<Integer> getLikes(Long productId);
}
