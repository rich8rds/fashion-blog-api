package com.richards.blog.entity;

import com.richards.blog.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "admins", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = {"email"})
})
public class Admin extends BaseClass {
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    Role role;
}
