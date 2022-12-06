package com.richards.blog.validator;

import com.richards.blog.dto.AdminDto;
import com.richards.blog.enums.ValidationResult;
import com.richards.blog.repository.AdminRepository;

import java.util.function.Function;
import java.util.regex.Pattern;

import static com.richards.blog.enums.ValidationResult.*;

public interface AdminRegValidatorService extends Function<AdminDto, ValidationResult> {
    String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    static AdminRegValidatorService doPasswordsMatch() {
        return admin -> admin.getPassword().equals(admin.getConfirmPassword()) ?
                SUCCESS : PASSWORDS_DO_NOT_MATCH;
    }
    static AdminRegValidatorService isPasswordValid() {
        return user -> user.getPassword() != null && user.getPassword().length() >= 7 ?
                ValidationResult.SUCCESS : ValidationResult.PASSWORD_MUST_BE_8_OR_MORE_CHARACTERS_LONG;
    }

    static AdminRegValidatorService isOtherPasswordValid() {
        return user -> user.getPassword() != null && user.getPassword().length() >= 7 ?
                ValidationResult.SUCCESS : ValidationResult.PASSWORD_MUST_BE_8_OR_MORE_CHARACTERS_LONG;
    }

    static AdminRegValidatorService isEmailValid() {
        return adminDto -> Pattern.matches(EMAIL_REGEX, adminDto.getEmail()) ? SUCCESS : EMAIL_NOT_VALID;
    }

    static AdminRegValidatorService emailExists(AdminRepository adminRepository) {
        return adminDto -> adminRepository.existsByEmail(adminDto.getEmail()) ? EMAIL_ALREADY_EXISTS : SUCCESS;
    }

    default AdminRegValidatorService and (AdminRegValidatorService other) {
        return adminDto -> {
            ValidationResult result = this.apply(adminDto);
            return result.equals(SUCCESS) ? other.apply(adminDto) : result;
        };
    }
}
