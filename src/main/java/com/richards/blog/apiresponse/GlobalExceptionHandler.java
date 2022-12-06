package com.richards.blog.apiresponse;

import com.richards.blog.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(CustomerNotFoundException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(value = SessionIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(SessionIdNotFoundException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorMessage(ex.getMessage()).build();
    }


    @ExceptionHandler(value = ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(ProductNotFoundException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorMessage(ex.getMessage()).build();
    }
    @ExceptionHandler(value = UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorResponse handleException(UnAuthorizedException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(value = ProductAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleException(ProductAlreadyExistsException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.CONFLICT.value())
                .errorMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(value = FieldBlankException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody ErrorResponse handleException(FieldBlankException ex) {
        return ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .errorMessage(ex.getMessage()).build();
    }
}
