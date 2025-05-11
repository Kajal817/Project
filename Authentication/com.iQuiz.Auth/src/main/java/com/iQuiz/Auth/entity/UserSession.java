package com.iQuiz.Auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_session")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSession {
	@Id
	private String tokenId;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private Date lastActivityTime;
	
	@Column(nullable = false)
	private boolean isActive;
}
