package com.iquiz.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "email_otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class EmailOtp {
	
	private Long id;
	
	private String email;
	
	private String otp;
	
	private Long expiryTime;
	
	private boolean verified;
	
	public void markVerified() {
		this.verified = true;
		log.info("OTP verified for user {}", email);
	}
	
	public boolean isExpired(long currentTimeMillis) {
		boolean expired = expiryTime < currentTimeMillis;
		if(expired) {
			log.warn("OTP expired for email {}", email);
		}
		return expired;
	}
}
