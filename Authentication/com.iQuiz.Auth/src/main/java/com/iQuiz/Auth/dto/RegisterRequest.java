package com.iQuiz.Auth.dto;

import java.util.Set;

import com.iQuiz.Auth.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
	@NotBlank(message = "Name is required")
	@Pattern(regexp = "^[A-Za-z]{2,50}$", message = "Full name contains only alphabetic character and whitespace")
	private String fullName;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email is invalid")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password must be atleast 8 character long. Password should contains alteast one upper case alphabet, one lower case alphabet, one digit and one special character")
	private String password;
	
	@NotEmpty(message = "At least one role must be provided")
	private Set<Role> role;
}
