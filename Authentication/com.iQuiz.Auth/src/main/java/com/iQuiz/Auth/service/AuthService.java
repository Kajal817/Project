package com.iQuiz.Auth.service;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iQuiz.Auth.dto.AuthRequest;
import com.iQuiz.Auth.dto.AuthResponse;
import com.iQuiz.Auth.dto.RegisterRequest;
import com.iQuiz.Auth.entity.User;
import com.iQuiz.Auth.entity.UserSession;
import com.iQuiz.Auth.exception.CustomException;
import com.iQuiz.Auth.repository.UserRepository;
import com.iQuiz.Auth.repository.UserSessionRepository;
import com.iQuiz.Auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	public static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	 
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserSessionRepository userSessionRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Value("${app.jwt.expiration}")
	private long jwtExpiration;
	
	public String register(RegisterRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException("Email is already registered");
		}
		User user = User.builder()
				.fullName(request.getFullName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(request.getRole())
				.build();
		userRepository.save(user);
		return "User Registered Successfully";
	}
	
	@Transactional
	public AuthResponse login(AuthRequest authRequest) throws CustomException{
		logger.info("Attempting to login with: {}", authRequest.getEmail());
		User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new CustomException("Invalid Username or password"));
		
		if(!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
			logger.error("Invalid credentials");
			throw new BadCredentialsException("Password or Username is invalid");
		}
		
		String token = jwtTokenProvider.generateToken(user);
		String tokenId = jwtTokenProvider.extractTokenId(token);
		
		UserSession userSession = new UserSession(tokenId, user.getEmail(), new Date(), true);
		userSessionRepository.save(userSession);
		logger.info("User loged-in successfully");
		return new AuthResponse(token, user.getEmail(), user.getRoles());
	}
	
//	public void logout(String email, boolean isActive) {
//		logger.info("Logging out user: {}", email);
//		Optional<UserSession> userSession = userSessionRepository.findByEmailAndIsActive(email, isActive);
//		userSession.ifPresent(session -> {
//			session.setActive(false);
//			userSessionRepository.save(session);
//			logger.info("User logged-out successfully: {}", email);
//		});
//	}
	public void logout(String email, boolean isActive) {
	    logger.info("Logging out user: {}", email);

	    // Attempt to find the active session for the user
	    Optional<UserSession> userSession = userSessionRepository.findByEmailAndIsActive(email, isActive);

	    // If a session is found, deactivate it
	    userSession.ifPresentOrElse(session -> {
	        session.setActive(false);
	        userSessionRepository.save(session);  // Save the updated session
	        logger.info("User logged-out successfully: {}", email);
	    }, () -> {
	        // Handle the case when the user session is not found
	        logger.warn("No active session found for user: {}", email);
	    });
	}
	
	public User getUserByEmailId(String email) throws CustomException {
		return userRepository.findByEmail(email).orElseThrow(()-> new CustomException("User not found with email: " + email));
	}
	
}
