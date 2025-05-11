package com.iQuiz.Auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import com.iQuiz.Auth.exception.ErrorDetails;
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorDetails(request.getRequestURI(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorDetails(request.getRequestURI(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
