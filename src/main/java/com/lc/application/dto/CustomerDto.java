package com.lc.application.dto;

import com.lc.application.model.Customer;

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
public class CustomerDto {
	private long id;
	@Email
	@NotEmpty(message = "Email should not be empty")
	private String email;
	@NotEmpty(message = "First name should not be empty")
	private String firstName;
	@NotEmpty(message = "Last name should not be empty")
	private String lastName;

	public CustomerDto(Customer customer) {
		this.id = customer.getId();
		this.email = customer.getUser() != null ? customer.getUser().getEmail() : null;
		this.firstName = customer.getUser() != null ? customer.getUser().getFirstName() : null;
		this.lastName = customer.getUser() != null ? customer.getUser().getLastName() : null;
	}
}
