package com.richards.blog.service.serviceimpl;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.LoginDto;
import com.richards.blog.dto.AdminDto;
import com.richards.blog.dto.ProductDto;
import com.richards.blog.entity.Product;
import com.richards.blog.entity.User;
import com.richards.blog.enums.Category;
import com.richards.blog.enums.Role;
import com.richards.blog.enums.ValidationResult;
import com.richards.blog.exceptions.ProductAlreadyExistsException;
import com.richards.blog.exceptions.ProductNotFoundException;
import com.richards.blog.exceptions.UnAuthorizedException;
import com.richards.blog.repository.ProductRepository;
import com.richards.blog.repository.UserRepository;
import com.richards.blog.service.AdminService;
import com.richards.blog.session.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static com.richards.blog.validator.AdminRegValidatorService.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HttpSession session;

    @Override
    public ApiResponse<User> login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Optional<User> adminOptional = userRepository.findByEmailAndPassword(email, password);

        if(adminOptional.isEmpty())
            return new ApiResponse<>("Login failed", HttpStatus.NOT_ACCEPTABLE, null);

        User admin = adminOptional.get();
        session.setAttribute("adminInfo", admin);
        return new ApiResponse<>("Login successful", HttpStatus.OK, admin);
    }

    @Override
    public ApiResponse<User> createNewAdmin(AdminDto adminDto) {
        String email = adminDto.getEmail();
        String password = adminDto.getPassword();

        User userInfo = UserInfo.getUser(session);
        if(!userInfo.getRole().equals(Role.SUPERADMIN))
            throw new UnAuthorizedException("You are not authorized to access this page!");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(!user.getRole().equals(Role.SUPERADMIN))
                throw new UnAuthorizedException("You are not authorized to access this page!");
        }

        ValidationResult result = isPasswordValid()
                .and(isOtherPasswordValid())
                .and(doPasswordsMatch())
                .and(isEmailValid())
                .and(emailExists(userRepository))
                .apply(adminDto);

        if(!result.equals(ValidationResult.SUCCESS))
            throw new IllegalArgumentException(result.name());

        Role role = !userRepository.existsByRole(Role.SUPERADMIN) ? Role.SUPERADMIN : adminDto.getRole();

        User admin = User.builder()
                .email(email)
                .password(password)
                .role(role)
                .build();
        return new ApiResponse<>("Created Successfully", HttpStatus.CREATED, userRepository.save(admin));
    }

    @Override
    public ApiResponse<Product> addNewProduct(ProductDto productDto) {
        isUserSuperAdmin();
        if(productRepository.existsByName(productDto.getName()))
            throw new ProductAlreadyExistsException("Product name already exists");

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .category(productDto.getCategory())
                .imageUrl(productDto.getImageUrl())
                .build();

        return new ApiResponse<>("Product Uploaded Successfully", HttpStatus.CREATED, productRepository.save(product));
    }

    @Override
    public ApiResponse<Product> updateProduct(Long productId, ProductDto productDto) {
        isUserSuperAdmin();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() ->
                new ProductNotFoundException("Product with id: " + productId + " does not exist!"));

        String name = productDto.getName();
        String description = productDto.getDescription();
        Category category = productDto.getCategory();
        String imageUrl = productDto.getImageUrl();

        if(name != null && !name.isBlank())
            product.setName(name);
        if(description != null && !description.isBlank())
            product.setDescription(description);
        if(category != null)
            product.setCategory(category);
        if(imageUrl != null && !imageUrl.isBlank())
            product.setImageUrl(imageUrl);

        return new ApiResponse<>("Product Updated Successfully", HttpStatus.CREATED, productRepository.save(product));
    }

    @Override
    public ApiResponse<String> deleteProduct(Long productId) {
        isUserSuperAdmin();
        if(!productRepository.existsById(productId))
            throw new ProductNotFoundException("Product with id: " + productId + " does not exist!");

        productRepository.deleteById(productId);
        return new ApiResponse<>("Product Deleted Successfully", HttpStatus.OK, "DELETED PRODUCT WITH ID: " + productId);
    }

    @Override
    public ApiResponse<String> deleteAllProducts() {
        isUserSuperAdmin();
        productRepository.deleteAll();
        return new ApiResponse<>("Products Deleted Successfully", HttpStatus.OK, "DELETED ALL PRODUCTS");
    }

    @Override
    public ApiResponse<List<Product>> getAllProducts() {
        isUserSuperAdmin();
        return new ApiResponse<>("Products Fetched Successfully", HttpStatus.OK, productRepository.findAll());
    }

    @Override
    public ApiResponse<List<User>> getAllUsers() {
        return new ApiResponse<>("List of users", HttpStatus.OK, userRepository.findAll());
    }

    @Override
    public ApiResponse<Page<Product>> getAllProducts(Optional<Integer> page, Optional<String> sortBy) {
        Page<Product> pageItem = productRepository.findAll(PageRequest.of(page.orElse(0),
                5, Sort.Direction.ASC, sortBy.orElse("id")));

        return new ApiResponse<>(("Page " + page.get()), HttpStatus.OK, pageItem);
    }

    private void isUserSuperAdmin() {
        User admin = UserInfo.getUser(session);
        Role role = admin.getRole();
        if(!role.equals(Role.SUPERADMIN))
            throw new UnAuthorizedException("You do not have the required privileges.");
    }
}
