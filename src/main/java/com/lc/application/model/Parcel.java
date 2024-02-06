package com.lc.application.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "sender_id", nullable = false)
	private Customer sender;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Customer receiver;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "office_id", nullable = true)
	private Office office;

	private String address;

	private BigDecimal weight;

	private LocalDate orderDate;

	private LocalDate deliveryDate;

	private Boolean isPaid;

	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee registeredBy;
}
