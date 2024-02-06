package com.lc.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.dto.CustomerDto;
import com.lc.application.model.Customer;
import com.lc.application.repository.CustomerRepository;

@Controller
@RequestMapping(value = "/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping
	public String getCustomers(Model model, @RequestParam(required = false) String email,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		try {
			List<CustomerDto> customers = new ArrayList<CustomerDto>();
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));

			Page<Customer> pageOfCustomers;
			if (email == null) {
				pageOfCustomers = customerRepository.findAll(paging);
			} else {
				pageOfCustomers = customerRepository.findByUserEmailContainingIgnoreCase(email, paging);
				model.addAttribute("email", email);
			}
			customers = pageOfCustomers.getContent().stream().map(c -> new CustomerDto(c)).collect(Collectors.toList());

			model.addAttribute("customers", customers);
			model.addAttribute("currentPage", pageOfCustomers.getNumber() + 1);
			model.addAttribute("totalItems", pageOfCustomers.getTotalElements());
			model.addAttribute("totalPages", pageOfCustomers.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/customers/customers-list";
	}
}
