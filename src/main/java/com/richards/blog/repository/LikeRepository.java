package com.richards.blog.repository;

import com.richards.blog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query(value = "SELECT * FROM likes WHERE customer_id=? AND product_id=?", nativeQuery = true)
    Optional<Like> findLikeByUserIdAndProductId(Long customerId, Long productId);
}
