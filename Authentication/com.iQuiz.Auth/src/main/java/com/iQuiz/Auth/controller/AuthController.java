package com.iQuiz.Auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iQuiz.Auth.dto.AuthRequest;
import com.iQuiz.Auth.dto.AuthResponse;
import com.iQuiz.Auth.exception.CustomException;
import com.iQuiz.Auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	public static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws CustomException{
		logger.info("Trying logging in for user: " + authRequest.getEmail());
		AuthResponse authResponse = authService.login(authRequest);
		logger.info("Login successful for user: {}", authRequest.getEmail());
		return ResponseEntity.ok(authResponse);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthRequest authRequest){
		 logger.info("Registration attempt for user: {}", authRequest.getEmail());
	     logger.info("User registered successfully: {}", authRequest.getEmail());
	     return ResponseEntity.status(201).body("User Registered Successfully");
	}
	
//	@PostMapping("/logout")
//	public ResponseEntity<?> logout(@RequestParam String email, @RequestParam boolean isActive){
//		logger.info("Logout attempt for user: {}", email);
//		authService.logout(email, isActive);
//		logger.info("User logged out successfully: {}", email);
//		return ResponseEntity.ok("User logout successfully.");
//	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestParam String email, @RequestParam boolean isActive) {
	    logger.info("Logout attempt for user: {}", email);

	    try {
	        // Call the service to handle the logout
	        authService.logout(email, isActive);

	        // If successful, return a success message
	        logger.info("User logged out successfully: {}", email);
	        return ResponseEntity.ok("User logged out successfully.");
	    } catch (UsernameNotFoundException e) {
	        // Handle case where the user session is not found or any other specific exception
	        logger.error("Logout failed for user: {}", email, e);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("No active session found for user: " + email);
	    } catch (Exception e) {
	        // Generic exception handler for other errors
	        logger.error("An unexpected error occurred during logout for user: {}", email, e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while logging out.");
	    }
	}

}
