package com.lc.application.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import com.lc.application.dto.RegisterUserDto;
import com.lc.application.security.UserService;

@Controller
public class AuthController {
    private UserService userService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        RegisterUserDto user = new RegisterUserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
