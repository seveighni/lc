package com.lc.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.application.dto.ResultDto;
import com.lc.application.dto.UserDto;
import com.lc.application.repository.UserRepository;

import jakarta.validation.Valid;

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

	@PostMapping
	public String updateUser(@Valid @ModelAttribute("userDto") UserDto dto, BindingResult result, Model model) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("userDto", dto);
				return "/profile";
			}

			com.lc.application.model.User user = userRepository.findByEmail(dto.getEmail());
			if (user == null) {
				model.addAttribute("result", new ResultDto("User not found!", false));
				return "redirect:/home";
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User principal = (User) auth.getPrincipal();
			String email = principal.getUsername();
			com.lc.application.model.User currentUser = userRepository.findByEmail(email);
			if (currentUser.getEmail() != user.getEmail()) {
				model.addAttribute("result", new ResultDto("Can not update other user!", false));
				return "redirect:/home";
			}

			user.setEmail(dto.getEmail());
			user.setFirstName(dto.getFirstName());
			user.setLastName(dto.getLastName());
			userRepository.save(user);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		model.addAttribute("result", new ResultDto("User updated successfully!", true));
		return "/profile";
	}
}
