package com.example.project11.controller;

import com.example.project11.exception.CustomException;
import com.example.project11.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValidArgumentExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse body = ErrorResponse.builder()
                .status(e.getStatusCode().value())
                .message(e.getMessage())
                .build();


        for (FieldError fieldError : e.getFieldErrors()) {
            body.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(e.getStatusCode()).body(body);
    }

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        ErrorResponse body = ErrorResponse.builder()
                .status(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(body);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception e) {
        ErrorResponse body = ErrorResponse.builder()
                .status(500)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(500).body(body);
    }
}
