package com.iQuiz.Auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_session")
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
	@Id
	private String tokenId;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private Date lastActivityTime;
	
	@Column(nullable = false)
	private boolean isActive;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	public Date getLastActivityTime() {
		return lastActivityTime;
	}
	public void setLastActivityTime(Date lastActiveTime) {
		this.lastActivityTime = lastActiveTime;
	}
	
	public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
