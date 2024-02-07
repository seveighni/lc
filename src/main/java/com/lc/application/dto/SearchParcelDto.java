package com.lc.application.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.lc.application.model.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParcelDto {

	private String responsible;

	private String sender;

	private String receiver;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private DeliveryStatus status;

	private Boolean isPaid;

}
