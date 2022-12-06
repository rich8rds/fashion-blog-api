package com.richards.blog.service;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.CommentDto;
import com.richards.blog.entity.Comment;

public interface CommentService {
    ApiResponse<Comment> addNewComment(Long productId, CommentDto commentDto);
    ApiResponse<Comment> updateComment(Long productId, Long commentId, CommentDto commentDto);
    ApiResponse<String> deleteComment(Long productId, Long commentId);
}
