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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.dto.UserDto;
import com.lc.application.model.Employee;
import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.EmployeeRepository;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping
	public String getUsers(Model model, @RequestParam(required = false) String email,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		loadModelWithWantedPagedUsers(model, email, false, page, size);
		return "/users/users-list";
	}

	@GetMapping("/employees")
	public String getUsersWaitingForApproval(Model model, @RequestParam(required = false) String email,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		loadModelWithWantedPagedUsers(model, email, true, page, size);
		return "/users/users-list";
	}

	private void loadModelWithWantedPagedUsers(Model model, String email, boolean fetchUsersRequestingApproval,
			int page, int size) {
		try {
			List<UserDto> users = new ArrayList<UserDto>();
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));

			Page<User> pageUsers;
			if (fetchUsersRequestingApproval) {
				pageUsers = userRepository.findAll(paging);
				users = pageUsers.getContent().stream().filter(u -> u.getRoles().isEmpty()).map(u -> new UserDto(u))
						.collect(Collectors.toList());
			} else if (email == null) {
				pageUsers = userRepository.findAll(paging);
				users = fetchUserDtos(pageUsers);
			} else {
				pageUsers = userRepository.findByEmailContainingIgnoreCase(email, paging);
				model.addAttribute("email", email);
				users = fetchUserDtos(pageUsers);
			}
			model.addAttribute("users", users);
			model.addAttribute("usersRequestingEmployeeAccess", fetchUsersRequestingApproval);
			model.addAttribute("currentPage", pageUsers.getNumber() + 1);
			model.addAttribute("totalItems", pageUsers.getTotalElements());
			model.addAttribute("totalPages", pageUsers.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
	}

	private List<UserDto> fetchUserDtos(Page<User> pageUsers) {
		return pageUsers.getContent().stream().map(u -> new UserDto(u)).collect(Collectors.toList());
	}

	@GetMapping(value = "/{id}/edit")
	public String updateUser(@PathVariable("id") long id) {
		User user = userRepository.findById(id).get();
		var role = roleRepository.findByName("EMPLOYEE");
		if (role == null) {
			role = new Role();
			role.setName("EMPLOYEE");
			user.addRole(role);
			roleRepository.save(role);
			createEmployee(user);
			return "redirect:/users/employees";
		}
		user.addRole(role);
		userRepository.save(user);
		createEmployee(user);
		return "redirect:/users/employees";
	}

	private void createEmployee(User user) {
		Employee employee = new Employee();
		employee.setUser(user);
		employee.setActive(true);
		employeeRepository.save(employee);
	}
}
