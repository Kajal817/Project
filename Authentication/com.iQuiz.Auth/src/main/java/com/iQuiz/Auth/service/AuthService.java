package com.iQuiz.Auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iQuiz.Auth.dto.AuthResponse;
import com.iQuiz.Auth.dto.RegisterRequest;
import com.iQuiz.Auth.entity.User;
import com.iQuiz.Auth.exception.CustomException;
import com.iQuiz.Auth.repository.UserRepository;
import com.iQuiz.Auth.security.JwtTokenProvider;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	
	public void register(RegisterRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException("Email is already registered");
		}
		User user = User.builder()
				.fullName(request.getFullName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role("USER")
				.build();
		userRepository.save(user);
		
	}
}
