package com.lc.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.application.dto.UserDto;
import com.lc.application.repository.UserRepository;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String getUser(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) auth.getPrincipal();
		String userEmail = principal.getUsername();

		try {
			com.lc.application.model.User currentUser = userRepository.findByEmail(userEmail);
			UserDto userDto = new UserDto();
			userDto.setEmail(currentUser.getEmail());
			userDto.setFirstName(currentUser.getFirstName());
			userDto.setLastName(currentUser.getLastName());
			model.addAttribute("userDto", userDto);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/profile";
	}
}
