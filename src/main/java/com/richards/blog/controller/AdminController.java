package com.richards.blog.controller;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.LoginDto;
import com.richards.blog.dto.AdminDto;
import com.richards.blog.dto.ProductDto;
import com.richards.blog.entity.Product;
import com.richards.blog.entity.User;
import com.richards.blog.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/products")
    public ApiResponse<Page<Product>> getAllProducts(@RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
        return adminService.getAllProducts(page, sortBy);
    }

    @PostMapping("/auth/login")
    public ApiResponse<User> adminLogin(@RequestBody LoginDto loginDto) {
        return adminService.login(loginDto);
    }

    @PostMapping("/auth/create-new")
    public ApiResponse<User> createNewAdmin(@RequestBody AdminDto adminDto) {
        return adminService.createNewAdmin(adminDto);
    }

    // Add a new product
    @PostMapping("/products")
    public ApiResponse<Product> addNewProduct(@RequestBody ProductDto productDto) {
        return adminService.addNewProduct(productDto);
    }

    // Update Products
    @PutMapping("/products/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        return adminService.updateProduct(productId, productDto);
    }

    // Delete product
    @DeleteMapping("/products/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable Long productId) {
        return adminService.deleteProduct(productId);
    }

    // Delete all products
    @DeleteMapping("/products")
    public ApiResponse<String> deleteAllProducts() {
        return adminService.deleteAllProducts();
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        return adminService.getAllUsers();
    }
}
