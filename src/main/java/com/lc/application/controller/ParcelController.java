package com.lc.application.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.lc.application.dto.ParcelDto;
import com.lc.application.dto.ResultDto;
import com.lc.application.dto.UpdateOfficeDto;
import com.lc.application.model.Customer;
import com.lc.application.model.Employee;
import com.lc.application.model.Office;
import com.lc.application.model.Parcel;
import com.lc.application.model.Rates;
import com.lc.application.repository.CustomerRepository;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.OfficeRepository;
import com.lc.application.repository.ParcelRepository;
import com.lc.application.repository.RatesRepository;

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
	private RatesRepository ratesRepository;

	@Autowired
	private OfficeRepository officeRepository;

	@GetMapping
	public String getParcels(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		// TODO
		loadModelWithWantedPagedParcels(model, false, page, size);
		return "/parcels/parcels-list";
	}

	// TODO
	private void loadModelWithWantedPagedParcels(Model model, Boolean sentByMe, int page, int size) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User principal = (User) auth.getPrincipal();
			String userRoleType = auth.getAuthorities().stream().findFirst().get().toString();

			List<ParcelDto> parcels = new ArrayList<ParcelDto>();
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));
			Page<Parcel> pageOfParcels = null;

			String userEmail = principal.getUsername();
			if (userRoleType.equals("CUSTOMER")) {
				Optional<Customer> customer = customerRepository.getCustomerIdByUserEmail(userEmail);
				if (customer.isPresent()) {
					Long userId = customer.get().getId();
					if (sentByMe) {
						pageOfParcels = parcelRepository.findAllBySenderId(paging, userId);
						parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p))
								.collect(Collectors.toList());

						// TODO remove hardcoded entity
						parcels.add(new ParcelDto(1));
					} else {
						pageOfParcels = parcelRepository.findAllByReceiverId(paging, userId);
						parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p))
								.collect(Collectors.toList());

						// TODO remove hardcoded entity
						parcels.add(new ParcelDto(2));
					}
				}
			} else if (userRoleType.equals("EMPLOYEE")) {
				if (sentByMe) {
					Optional<Employee> employee = employeeRepository.getEmployeeIdByUserEmail(userEmail);
					if (employee.isPresent()) {
						Long userId = employee.get().getId();
						pageOfParcels = parcelRepository.findAllByRegisteredById(paging, userId);
						parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p))
								.collect(Collectors.toList());

						// TODO remove hardcoded entity
						parcels.add(new ParcelDto(3));
					}
				} else {
					pageOfParcels = parcelRepository.findAll(paging);
					parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p))
							.collect(Collectors.toList());

					// TODO remove hardcoded entity
					parcels.add(new ParcelDto(4));
				}
			} else if (userRoleType.equals("ADMIN")) {
				pageOfParcels = parcelRepository.findAll(paging);
				parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p)).collect(Collectors.toList());

				// TODO remove hardcoded entity
				parcels.add(new ParcelDto(5));
			}
			model.addAttribute("parcels", parcels);
			model.addAttribute("currentPage", pageOfParcels.getNumber() + 1);
			model.addAttribute("totalItems", pageOfParcels.getTotalElements());
			model.addAttribute("totalPages", pageOfParcels.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
	}

	@GetMapping("/sentBy")
	public String getParcelsSentByMe(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		loadModelWithWantedPagedParcels(model, true, page, size);
		return "/parcels/parcels-list";
	}

	@GetMapping("/sentTo")
	public String getParcelsSentToMe(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		loadModelWithWantedPagedParcels(model, false, page, size);
		return "/parcels/parcels-list";
	}

	@GetMapping("/registeredBy")
	public String getParcelsRegisteredByMe(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		loadModelWithWantedPagedParcels(model, true, page, size);
		return "/parcels/parcels-list";
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
		parcel.setOrderDate(LocalDate.now());
		parcel.setIsPaid(dto.getIsPaid());

		System.out.println(dto.getFrom());
		System.out.println(dto.getTo());
		System.out.println(dto.getWeight());
		System.out.println(dto.getOfficeId());
		System.out.println(dto.getAddress());
		System.out.println(dto.getRateId());
		System.out.println(dto.getIsPaid());

		model.addAttribute("result", new ResultDto("Parcel created successfully!", true));
		return "/parcels/create";
	}

}
