package com.richards.blog.dto;

import com.richards.blog.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private String name;
    private String description;
    private Category category;
    private String imageUrl;
}
