package com.iquiz.user.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
	
	public static final Logger logger = LoggerFactory.getLogger(UserProfile.class);
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String fullName;
	private String email;
	private String phoneNumber;
	private String gender;
	private String country;
	private boolean emailVerified;
	private Subscription subscription;
	private void afterCreation() {
		logger.info("User Profile created successfully.");
	}
	
}
