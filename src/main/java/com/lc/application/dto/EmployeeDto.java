package com.lc.application.dto;

import com.lc.application.model.Employee;

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
public class EmployeeDto {
	private long id;
	@Email
	@NotEmpty(message = "Email should not be empty")
	private String email;
	@NotEmpty(message = "First name should not be empty")
	private String firstName;
	@NotEmpty(message = "Last name should not be empty")
	private String lastName;
	private Long officeId;
	private String officeAddress;
	@NotEmpty(message = "Type should not be empty")
	private String type;
	private Boolean isActive;

	public EmployeeDto(Employee employee) {
		this.id = employee.getId();
		this.email = employee.getUser() != null ? employee.getUser().getEmail() : null;
		this.firstName = employee.getUser() != null ? employee.getUser().getFirstName() : null;
		this.lastName = employee.getUser() != null ? employee.getUser().getLastName() : null;
		this.officeId = employee.getOffice() != null ? employee.getOffice().getId() : null;
		this.officeAddress = employee.getOffice() != null ? employee.getOffice().getAddress() : null;
		this.type = employee.getType();
		this.isActive = employee.isActive();
	}
}
