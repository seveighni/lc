package com.lc.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter    
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rates")
public class Rates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double perKg;
	
	private double shipToOffice;
	
	private double shipToAddress;
}
