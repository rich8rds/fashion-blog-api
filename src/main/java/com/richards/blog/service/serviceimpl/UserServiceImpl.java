package com.richards.blog.service.serviceimpl;

import com.richards.blog.apiresponse.ApiResponse;
import com.richards.blog.dto.AddUserDto;
import com.richards.blog.entity.User;
import com.richards.blog.enums.ValidationResult;
import com.richards.blog.repository.UserRepository;
import com.richards.blog.service.UserService;
import com.richards.blog.validator.RegistrationValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public ApiResponse<User> registerNewUser(AddUserDto addUserDto) {
        ValidationResult result = RegistrationValidationService.isUsernameValid()
                .and(RegistrationValidationService.isEmailValid())
                .and(RegistrationValidationService.emailExists(userRepository))
                .apply(addUserDto);

        if(result.equals(ValidationResult.SUCCESS)) {
            User user = User.builder()
                    .username(addUserDto.getUsername())
                    .email(addUserDto.getEmail())
                    .build();

            User newUser = userRepository.save(user);
            session.setAttribute("userDetails", newUser);
            return new ApiResponse<>("Registration Successful", HttpStatus.CREATED, newUser);

        }return new ApiResponse<>(result.toString(), HttpStatus.NOT_ACCEPTABLE, null);
    }
}
