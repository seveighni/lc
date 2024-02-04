package com.lc.application.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.lc.application.dto.RegisterUserDto;
import com.lc.application.model.Role;
import com.lc.application.model.User;
import com.lc.application.repository.RoleRepository;
import com.lc.application.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        }

        RegisterUserDto user = new RegisterUserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") RegisterUserDto userDto,
            BindingResult result,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        }

        if (!userDto.getPassword().equals(userDto.getPassword1())) {
            result.rejectValue("password", null, "Passwords do not match");
        }

        User existing = userRepository.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null,
                    "The email is already taken. Please choose another one.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        User user = registerUser(userDto);
        if (user == null) {
            model.addAttribute("user", userDto);
            model.addAttribute("error", "An error occurred while registering.");
            return "/register";
        }

        return "redirect:/login?successRegistration";
    }

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        }
        return "login";
    }

    private User registerUser(RegisterUserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var role = roleRepository.findByName("ROLE_CUSTOMER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_CUSTOMER");
            roleRepository.save(role);
        }
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }
}
