package com.lc.application.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.dto.ParcelDto;
import com.lc.application.model.Customer;
import com.lc.application.model.Parcel;
import com.lc.application.repository.CustomerRepository;
import com.lc.application.repository.ParcelRepository;

@Controller
@RequestMapping(value = "/parcels")
public class ParcelController {

	@Autowired
	private ParcelRepository parcelRepository;

	@Autowired
	private CustomerRepository customerRepository;

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

			if (userRoleType.equals("CUSTOMER")) {
				String customerEmail = principal.getUsername();
				Optional<Customer> customer = customerRepository.getCustomerIdByUserEmail(customerEmail);
				if (customer.isPresent()) {
					Long userId = customer.get().getId();
					if (sentByMe) {
						pageOfParcels = parcelRepository.findAllBySenderId(paging, userId);
						parcels = pageOfParcels.getContent().stream().filter(p -> p.getSender().getId() == userId)
								.map(p -> new ParcelDto(p)).collect(Collectors.toList());

						// TODO remove hardcoded entity
						parcels.add(new ParcelDto(2));
					} else {
						pageOfParcels = parcelRepository.findAllByReceiverId(paging, userId);
						parcels = pageOfParcels.getContent().stream().filter(p -> p.getReceiver().getId() == userId)
								.map(p -> new ParcelDto(p)).collect(Collectors.toList());

						// TODO remove hardcoded entity
						parcels.add(new ParcelDto(3));
					}

				}
			} else if (userRoleType.equals("ADMIN") || userRoleType.equals("EMPLOYEE")) {
				pageOfParcels = parcelRepository.findAll(paging);
				parcels = pageOfParcels.getContent().stream().map(p -> new ParcelDto(p)).collect(Collectors.toList());

				// TODO remove hardcoded entity
				parcels.add(new ParcelDto(1));
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

}
