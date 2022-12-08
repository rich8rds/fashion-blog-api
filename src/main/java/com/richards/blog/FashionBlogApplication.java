package com.richards.blog;

import com.richards.blog.entity.User;
import com.richards.blog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.richards.blog.enums.Role.ADMIN;

@SpringBootApplication
public class FashionBlogApplication {
    public static void main(String[] args) {SpringApplication.run(FashionBlogApplication.class, args);}

    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return (user) -> {
            userRepository.save(User.builder()
                            .username("kohlson")
                            .email("admino8@gmail.com")
                            .password("password123")
                            .role(ADMIN)
                    .build());
        };
    }
}
