package com.richards.blog.validator;


import com.richards.blog.dto.AddUserDto;
import com.richards.blog.enums.ValidationResult;
import com.richards.blog.repository.UserRepository;

import java.util.function.Function;
import java.util.regex.Pattern;

public interface RegistrationValidationService extends Function<AddUserDto, ValidationResult> {
    String REGEX = "^[a-zA-Z]+([a-zA-Z]+)+$";
    String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    static RegistrationValidationService isUsernameValid() {
        return user -> user.getUsername() != null &&
                Pattern.matches(REGEX, user.getUsername()) ?
                ValidationResult.SUCCESS : ValidationResult.USERNAME_NOT_VALID;
    }


    static RegistrationValidationService isPasswordValid() {
        return user -> user.getPassword() != null && user.getPassword().length() >= 7 ?
                ValidationResult.SUCCESS : ValidationResult.PASSWORD_MUST_BE_8_OR_MORE_CHARACTERS_LONG;
    }

    static RegistrationValidationService isEmailValid() {
        return user -> user.getEmail() != null &&
                Pattern.matches(EMAIL_REGEX, user.getEmail()) ?
                ValidationResult.SUCCESS : ValidationResult.EMAIL_NOT_VALID;
    }

    static RegistrationValidationService emailExists(UserRepository userRepository) {
        return user ->  userRepository.findByEmail(user.getEmail()).isEmpty() ? ValidationResult.SUCCESS : ValidationResult.EMAIL_ALREADY_EXISTS;
    }


    default RegistrationValidationService and (RegistrationValidationService other) {
        return user -> {
            ValidationResult result = this.apply(user);
            return result.equals(ValidationResult.SUCCESS) ? other.apply(user) : result;
        };
    }
}