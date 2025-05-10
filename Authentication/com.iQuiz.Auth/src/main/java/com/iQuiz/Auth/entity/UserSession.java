package com.iQuiz.Auth.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
	@Id
	private String tokenId;
	private String email;
	private Date lastActive;
	private boolean isActive;
}
