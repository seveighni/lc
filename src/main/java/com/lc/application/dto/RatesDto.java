package com.lc.application.dto;

import java.math.BigDecimal;

import com.lc.application.model.Rates;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatesDto {

	private long id;

	@NotEmpty(message = "Name should not be empty")
	private String name;

	@Min(value = 0L, message = "The value must be a positive number")
	private BigDecimal perKg;

	@Min(value = 0L, message = "The value must be a positive number")
	private BigDecimal flatRate;

	public RatesDto(Rates rates) {
		this.id = rates.getId();
		this.name = rates.getName();
		this.perKg = rates.getPerKg();
		this.flatRate = rates.getFlatRate();
	}
}
