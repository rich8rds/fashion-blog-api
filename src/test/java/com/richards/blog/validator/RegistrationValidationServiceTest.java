package com.richards.blog.validator;


import com.richards.blog.dto.AddUserDto;
import com.richards.blog.enums.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest

class RegistrationValidationServiceTest {
  AddUserDto addUserDto;

    @BeforeEach
    public void before() throws Exception {
        addUserDto = new AddUserDto();
    }

    @Test
    @DisplayName("VALIDATE FIRSTNAME: should return FIRSTNAME_NOT_VALID")
    public void firstNameNotValid() {
        addUserDto.setUsername("Richards!!");
        ValidationResult result = RegistrationValidationService.isUsernameValid().apply(addUserDto);
        assertEquals(ValidationResult.USERNAME_NOT_VALID, result);
    }

    @Test
    @DisplayName("VALIDATE PASSWORD: " +
            "Should return PASSWORD_MUST_BE_7_OR_MORE_CHARACTERS_LONG")
    public void passwordNotValid() {
        addUserDto.setPassword("ioejfk");
        ValidationResult result = RegistrationValidationService.isPasswordValid().apply(addUserDto);
        assertEquals(ValidationResult.PASSWORD_MUST_BE_8_OR_MORE_CHARACTERS_LONG, result);

    }

    @Test
    @DisplayName("VALIDATE PASSWORD: Should return SUCCESS")
    public void passwordValid() {
        addUserDto.setPassword("ioejf%%%%%%k");
        ValidationResult result = RegistrationValidationService.isPasswordValid().apply(addUserDto);
        assertEquals(ValidationResult.SUCCESS, result);

    }

    @Test
    @DisplayName("VALIDATE EMAIL: Should return SUCCESS")
    public void emailValid() {
        addUserDto.setEmail("richards@gmail.com");
        ValidationResult result = RegistrationValidationService.isEmailValid().apply(addUserDto);
        assertEquals(ValidationResult.SUCCESS, result);
    }

    @Test
    @DisplayName("VALIDATE EMAIL: Should return SUCCESS")
    public void emailNotValid() {
        addUserDto.setEmail("richardsgmail.com");
        ValidationResult result = RegistrationValidationService.isEmailValid().apply(addUserDto);
        assertEquals(ValidationResult.EMAIL_NOT_VALID, result);
    }
}