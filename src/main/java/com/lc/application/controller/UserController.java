package com.lc.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/employees")
	public String getUsers(Model model) {
		List<User> users = userRepository.findAll().stream().filter(u -> u.getRoles().isEmpty()).toList();
		model.addAttribute("users", users);
		return "/users/users-list";
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
			// TODO employee
			return "redirect:/users/employees";
		}
		user.addRole(role);
		userRepository.save(user);
		// TODO employee
		return "redirect:/users/employees";
	}
}
