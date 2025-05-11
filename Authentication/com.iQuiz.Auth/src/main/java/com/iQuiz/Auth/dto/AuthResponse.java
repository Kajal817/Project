package com.iQuiz.Auth.dto;

import java.util.Set;

import com.iQuiz.Auth.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	private String token;
	private String email;
	private Set<Role> roles;
}
