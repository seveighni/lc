package com.lc.application.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "parcels")
public class Parcel {

	// TODO

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "sender_id", nullable = false)
	private Customer sender;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Customer receiver;

	private String address;

	private double weight;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date orderDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deliveryDate;

	private Boolean isPaid;

	// @Transient ?
	// private Rates rate;

	// @Formula
	private BigDecimal price;

	// enum ?
	private String status;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee registeredBy;

}
