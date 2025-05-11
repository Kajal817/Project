package com.iQuiz.Auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestParam String email){
		logger.info("Logout attempt for user: {}", email);
		authService.logout(email);
		logger.info("User logged out successfully: {}", email);
		return ResponseEntity.ok("User logout successfully.");
	}
}
