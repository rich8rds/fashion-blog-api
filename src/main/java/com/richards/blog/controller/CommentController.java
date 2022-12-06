package com.richards.blog.controller;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.CommentDto;
import com.richards.blog.entity.Comment;
import com.richards.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/{productId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/new")
    public ApiResponse<Comment> addNewComment(@PathVariable Long productId, @RequestBody CommentDto commentDto) {
        return commentService.addNewComment(productId, commentDto);
    }

    @PutMapping("/{commentId}/update")
    public ApiResponse<Comment> updateComment(@PathVariable Long productId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(productId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}/delete")
    public ApiResponse<String> deleteComment(@PathVariable Long productId, @PathVariable Long commentId) {
        return commentService.deleteComment(productId, commentId);
    }
}
