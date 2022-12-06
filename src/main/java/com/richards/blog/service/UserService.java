package com.richards.blog.service;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.AddUserDto;
import com.richards.blog.entity.User;

import java.util.Optional;

public interface UserService {

    ApiResponse<User> registerNewUser(AddUserDto addUserDto);
}
