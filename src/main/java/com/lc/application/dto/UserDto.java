package com.lc.application.dto;

import com.lc.application.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private long id;
	@Email
	@NotEmpty(message = "Email should not be empty")
	private String email;
	@NotEmpty(message = "First name should not be empty")
	private String firstName;
	@NotEmpty(message = "Last name should not be empty")
	private String lastName;

	public UserDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
	}
}
