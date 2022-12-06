package com.richards.blog.service;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.LoginDto;
import com.richards.blog.dto.AdminDto;
import com.richards.blog.dto.ProductDto;
import com.richards.blog.entity.Admin;
import com.richards.blog.entity.Product;
import com.richards.blog.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    ApiResponse<Admin> login(LoginDto loginDto);

    ApiResponse<Admin> createNewAdmin(AdminDto adminDto);

    ApiResponse<Product> addNewProduct(ProductDto productDto);

    ApiResponse<Product> updateProduct(Long productId, ProductDto productDto);

    ApiResponse<String> deleteProduct(Long productId);

    ApiResponse<String> deleteAllProducts();

    ApiResponse<List<Product>> getAllProducts();

    ApiResponse<List<User>> getAllUsers();

    ApiResponse<Page<Product>> getAllProducts(Optional<Integer> page, Optional<String> sortBy);
}
