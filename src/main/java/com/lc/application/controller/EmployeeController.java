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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.dto.EmployeeDto;
import com.lc.application.dto.ResultDto;
import com.lc.application.model.Employee;
import com.lc.application.model.Office;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.OfficeRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private OfficeRepository officeRepository;

	@GetMapping
	public String getEmployees(Model model, @RequestParam(required = false) String email,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		try {
			List<EmployeeDto> employees = new ArrayList<EmployeeDto>();
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));

			Page<Employee> pageEmployees;
			if (email == null) {
				pageEmployees = employeeRepository.findAll(paging);
			} else {
				pageEmployees = employeeRepository.findByUserEmailContainingIgnoreCase(email, paging);
				model.addAttribute("email", email);
			}
			employees = pageEmployees.getContent().stream().map(e -> new EmployeeDto(e)).collect(Collectors.toList());
			model.addAttribute("employees", employees);
			model.addAttribute("currentPage", pageEmployees.getNumber() + 1);
			model.addAttribute("totalItems", pageEmployees.getTotalElements());
			model.addAttribute("totalPages", pageEmployees.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/employees/employees-list";
	}

	@GetMapping(value = "/{id}")
	public String editEmployeeForm(@PathVariable long id, Model model) {
		EmployeeDto employeeToUpdate = new EmployeeDto(employeeRepository.findById(id).get());
		List<Office> offices = officeRepository.findAll().stream().filter(office -> office.getIsActive()).toList();
		model.addAttribute("offices", offices);
		model.addAttribute("employee", employeeToUpdate);
		return "/employees/employees-edit";
	}

	@PostMapping(value = "/{id}")
	public String updateEmployee(@PathVariable("id") long id,
			@Valid @ModelAttribute("employeeDto") EmployeeDto employeeDto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("employee", employeeDto);
			return "/employees/employees-edit";
		}

		var opt = employeeRepository.findById(id);
		if (opt.isEmpty()) {
			model.addAttribute("message", "Employee not found");
			return "redirect:/employees/employees-list";
		}

		var employee = opt.get();
		employee.setActive(employeeDto.getIsActive());
		employee.setType(employeeDto.getType());

		Long officeId = employeeDto.getOfficeId();
		if (officeId != null) {
			var office = officeRepository.findById(officeId);
			if (office.isPresent()) {
				employee.setOffice(office.get());
			} else {
				model.addAttribute("message", "Office not found");
				model.addAttribute("employee", employeeDto);
				return "/employees/employees-edit";
			}
		}

		employeeRepository.save(employee);
		model.addAttribute("offices", officeRepository.findAll());
		model.addAttribute("result", new ResultDto("Employee updated successfully!", true));
		model.addAttribute("employee", employeeDto);
		return "/employees/employees-edit";
	}

}
