package com.richards.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.richards.blog.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="products", uniqueConstraints = @UniqueConstraint(name = "name", columnNames = {"name"}))
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Category category;
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Like> likes;
}
