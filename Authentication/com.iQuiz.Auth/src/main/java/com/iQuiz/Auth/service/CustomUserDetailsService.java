package com.iQuiz.Auth.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.iQuiz.Auth.entity.User;
import com.iQuiz.Auth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	public static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	public UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		logger.debug("Loading User by email: {}", email);
		User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Not Found with email: "+ email)); 
		logger.info("User found with: {}", user.getEmail());
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList()))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}


	
}
