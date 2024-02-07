package com.lc.application.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.dto.CreateParcelDto;
import com.lc.application.dto.ResultDto;
import com.lc.application.dto.SearchParcelDto;
import com.lc.application.dto.UpdateParcelDto;
import com.lc.application.model.Customer;
import com.lc.application.model.DeliveryStatus;
import com.lc.application.model.Parcel;
import com.lc.application.repository.CustomerRepository;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.OfficeRepository;
import com.lc.application.repository.ParcelRepository;
import com.lc.application.repository.RatesRepository;
import com.lc.application.repository.UserRepository;
import com.lc.application.specification.ParcelSpecification;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/parcels")
public class ParcelController {

	@Autowired
	private ParcelRepository parcelRepository;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RatesRepository ratesRepository;

	@Autowired
	private OfficeRepository officeRepository;

	@GetMapping
	public String getParcels(Model model, @Valid @ModelAttribute("parcelDto") SearchParcelDto parcelDto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {

		try {
			List<Parcel> parcels = new ArrayList<Parcel>();
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));
			Page<Parcel> pageParcels = null;

			var startDate = parcelDto.getStartDate() == null ? LocalDate.of(1970, 1, 1).atTime(LocalTime.MIN)
					: parcelDto.getStartDate().atTime(LocalTime.MIN);
			var endDate = parcelDto.getEndDate() == null ? LocalDateTime.now()
					: parcelDto.getEndDate().atTime(LocalTime.MAX);

			var specInRange = ParcelSpecification.orderDateInRange(startDate, endDate);

			com.lc.application.model.User loggedInUser = getLoggedInUser();
			if (getLoggedInUserRole().equals("CUSTOMER")) {
				Optional<Customer> customer = customerRepository.getCustomerIdByUserEmail(loggedInUser.getEmail());
				if (customer.isPresent()) {
					Long customerId = customer.get().getId();
					var relatedToMe = Specification.where(ParcelSpecification.hasSenderId(customerId))
							.or(ParcelSpecification.hasReceiverId(customerId));
					var filter = specInRange.and(relatedToMe);
					if (parcelDto.getIsPaid() != null) {
						filter = filter.and(ParcelSpecification.isPaid(parcelDto.getIsPaid()));
					}

					if (parcelDto.getStatus() != null) {
						filter = filter.and(ParcelSpecification.hasStatus(parcelDto.getStatus()));
					}
					pageParcels = parcelRepository.findAll(filter, paging);
				} else {
					model.addAttribute("result", new ResultDto("Customer not found!", false));
				}
			} else if (getLoggedInUserRole().equals("EMPLOYEE")) {
				var filter = specInRange;
				if (parcelDto.getResponsible() != null && !parcelDto.getResponsible().isBlank()) {
					filter = filter.and(ParcelSpecification.hasEmployeeResponsible(parcelDto.getResponsible()));
				}
				if (parcelDto.getSender() != null && !parcelDto.getSender().isBlank()) {
					filter = filter.and(ParcelSpecification.hasSender(parcelDto.getSender()));
				}
				if (parcelDto.getReceiver() != null && !parcelDto.getReceiver().isBlank()) {
					filter = filter.and(ParcelSpecification.hasReceiver(parcelDto.getReceiver()));
				}

				if (parcelDto.getIsPaid() != null) {
					filter = filter.and(ParcelSpecification.isPaid(parcelDto.getIsPaid()));
				}

				if (parcelDto.getStatus() != null) {
					filter = filter.and(ParcelSpecification.hasStatus(parcelDto.getStatus()));
				}

				pageParcels = parcelRepository.findAll(filter, paging);

				var all = parcelRepository.findAll(filter);
				var totalPaid = all.stream().filter(p -> p.getIsPaid()).map(p -> p.getPrice()).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				var remainingPayment = all.stream().filter(p -> !p.getIsPaid()).map(p -> p.getPrice())
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				var parcels1 = pageParcels.getContent();
				model.addAttribute("parcels", parcels1);
				model.addAttribute("currentPage", pageParcels.getNumber() + 1);
				model.addAttribute("totalItems", pageParcels.getTotalElements());
				model.addAttribute("totalPages", pageParcels.getTotalPages());
				model.addAttribute("pageSize", size);
				model.addAttribute("totalPaid", totalPaid);
				model.addAttribute("remainingPayment", remainingPayment);
				return "/parcels/list-employee";
			} else if (getLoggedInUserRole().equals("ADMIN")) {
				var filter = specInRange;
				if (parcelDto.getResponsible() != null && !parcelDto.getResponsible().isBlank()) {
					filter = filter.and(ParcelSpecification.hasEmployeeResponsible(parcelDto.getResponsible()));
				}
				if (parcelDto.getSender() != null && !parcelDto.getSender().isBlank()) {
					filter = filter.and(ParcelSpecification.hasSender(parcelDto.getSender()));
				}
				if (parcelDto.getReceiver() != null && !parcelDto.getReceiver().isBlank()) {
					filter = filter.and(ParcelSpecification.hasReceiver(parcelDto.getReceiver()));
				}

				if (parcelDto.getIsPaid() != null) {
					filter = filter.and(ParcelSpecification.isPaid(parcelDto.getIsPaid()));
				}

				if (parcelDto.getStatus() != null) {
					filter = filter.and(ParcelSpecification.hasStatus(parcelDto.getStatus()));
				}
				pageParcels = parcelRepository.findAll(filter, paging);
			}

			parcels = pageParcels.getContent();

			model.addAttribute("parcelDto", parcelDto);
			model.addAttribute("parcels", parcels);
			model.addAttribute("currentPage", pageParcels.getNumber() + 1);
			model.addAttribute("totalItems", pageParcels.getTotalElements());
			model.addAttribute("totalPages", pageParcels.getTotalPages());
			model.addAttribute("pageSize", size);

		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		return "/parcels/list-employee";
	}

	private com.lc.application.model.User getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) auth.getPrincipal();
		String email = principal.getUsername();
		return userRepository.findByEmail(email);
	}

	private String getLoggedInUserRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().stream().findFirst().get().toString();
	}

	public static Date retirieveDate(String dateStr) throws ParseException {
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) parser.parse(dateStr);
		return date;
	}

	@GetMapping("/create")
	public String getParcelsCreateForm(Model model) {
		var dto = new CreateParcelDto();
		var offices = officeRepository.findAll().stream().filter(office -> office.getIsActive()).toList();
		var rates = ratesRepository.findAll();

		model.addAttribute("offices", offices);
		model.addAttribute("dto", dto);
		model.addAttribute("rates", rates);
		return "/parcels/create";
	}

	@PostMapping("/create")
	public String createParcel(@Valid @ModelAttribute("dto") CreateParcelDto dto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("dto", dto);
			return "/offices/create";
		}

		var offices = officeRepository.findAll().stream().filter(office -> office.getIsActive()).toList();
		var rates = ratesRepository.findAll();
		model.addAttribute("offices", offices);
		model.addAttribute("rates", rates);
		model.addAttribute("dto", dto);

		if (dto.getFrom() == dto.getTo()) {
			model.addAttribute("result", new ResultDto("Sender and receiver cannot be the same.", false));
			return "/parcels/create";
		}

		if (dto.getWeight() == null || dto.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
			model.addAttribute("result", new ResultDto("Weight cannot be less than or equal to 0.", false));
			return "/parcels/create";
		}

		if (dto.getRateId() == null) {
			model.addAttribute("result", new ResultDto("Rate cannot be empty.", false));
			return "/parcels/create";
		}

		var rateOpt = ratesRepository.findById(dto.getRateId());
		if (rateOpt.isEmpty()) {
			model.addAttribute("result", new ResultDto("Rate not found.", false));
			return "/parcels/create";
		}

		if (dto.getOfficeId() != null && dto.getAddress() != null && !dto.getAddress().isBlank()) {
			model.addAttribute("result",
					new ResultDto("Office and address cannot be provided at the same time.", false));
			return "/parcels/create";
		}

		var from = customerRepository.getCustomerIdByUserEmail(dto.getFrom());
		if (!from.isPresent()) {
			model.addAttribute("result", new ResultDto("Sender not found.", false));
			return "/parcels/create";
		}

		var to = customerRepository.getCustomerIdByUserEmail(dto.getTo());
		if (!to.isPresent()) {
			model.addAttribute("result", new ResultDto("Receiver not found.", false));
			return "/parcels/create";
		}

		var parcel = new Parcel();
		parcel.setSender(from.get());
		parcel.setReceiver(to.get());

		if (dto.getOfficeId() != null) {
			var office = officeRepository.findById(dto.getOfficeId());
			if (office.isEmpty()) {
				model.addAttribute("result", new ResultDto("Office not found.", false));
				return "/parcels/create";
			}
			parcel.setOffice(office.get());
		} else if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
			parcel.setAddress(dto.getAddress());
		} else {
			model.addAttribute("result", new ResultDto("Office or address should be provided.", false));
			return "/parcels/create";
		}

		parcel.setWeight(dto.getWeight());
		var rate = rateOpt.get();
		var price = dto.getWeight().multiply(rate.getPerKg());
		price = price.add(rate.getFlatRate());
		parcel.setPrice(price);

		var now = LocalDateTime.now();
		parcel.setOrderDate(now);
		parcel.setStatusLastUpdateDate(now);
		parcel.setIsPaid(dto.getIsPaid());

		var loggedInUser = getLoggedInUser();
		var employee = employeeRepository.getEmployeeIdByUserEmail(loggedInUser.getEmail());
		parcel.setRegisteredBy(employee.get());

		parcel.setStatus(DeliveryStatus.NEW);

		parcelRepository.save(parcel);

		model.addAttribute("result", new ResultDto("Parcel created successfully!", true));
		return "/parcels/create";
	}

	@GetMapping("/{id}")
	public String getParcel(Model model, @PathVariable Long id) {
		try {
			Optional<Parcel> parcelOpt = parcelRepository.findById(id);
			if (parcelOpt.isEmpty()) {
				model.addAttribute("message", "Parcel not found");
				return "/parcels/edit";
			}
			var parcel = parcelOpt.get();
			UpdateParcelDto parcelDto = new UpdateParcelDto();
			parcelDto.setId(parcel.getId());
			parcelDto.setIsPaid(parcel.getIsPaid());
			parcelDto.setStatus(parcel.getStatus());
			model.addAttribute("updateParcelDTO", parcelDto);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/parcels/edit";
	}

	@PostMapping("/{id}")
	public String putParcel(@Valid @ModelAttribute("parcel") UpdateParcelDto dto, BindingResult result, Model model,
			@PathVariable Long id) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("parcel", dto);
				return "/parcels/edit";
			}

			var opt = parcelRepository.findById(id);
			if (opt.isEmpty()) {
				model.addAttribute("message", "Parcel not found");
				return "/parcels/edit";
			}
			var parcel = opt.get();
			parcel.setIsPaid(dto.getIsPaid());
			parcel.setStatus(dto.getStatus());
			parcel.setStatusLastUpdateDate(LocalDateTime.now());
			parcelRepository.save(parcel);

			UpdateParcelDto parcelDto = new UpdateParcelDto();
			parcelDto.setId(parcel.getId());
			parcelDto.setIsPaid(parcel.getIsPaid());
			parcelDto.setStatus(parcel.getStatus());
			model.addAttribute("updateParcelDTO", parcelDto);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		model.addAttribute("result", new ResultDto("Parcel updated successfully!", true));
		return "/parcels/edit";
	}
}
