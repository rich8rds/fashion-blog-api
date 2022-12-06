package com.richards.blog.dto;

import com.richards.blog.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto {
    private String email;
    private String password;
    private String confirmPassword;
    Role role;
}
