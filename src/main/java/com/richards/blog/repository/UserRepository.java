package com.richards.blog.repository;

import com.richards.blog.entity.User;
import com.richards.blog.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByRole(Role admin);
}
