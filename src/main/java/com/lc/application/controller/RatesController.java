package com.lc.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.application.model.Rates;
import com.lc.application.repository.RatesRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/rates")
public class RatesController {

	@Autowired
	private RatesRepository ratesRepository;

	@GetMapping
	public String getRates(Model model) {
		List<Rates> rates = ratesRepository.findAll().stream().toList();
		model.addAttribute("rate", new Rates());
		model.addAttribute("rates", rates);
		return "/rates/rates-list";
	}

	@PostMapping
	public String createRate(@Valid @ModelAttribute("rate") Rates rate, BindingResult result, Model model) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("rate", rate);
				return "/rates/rates-list";
			}
			Rates rateToBeCreated = new Rates();
			rateToBeCreated.setName(rate.getName());
			rateToBeCreated.setPerKg(rate.getPerKg());
			rateToBeCreated.setFlatRate(rate.getFlatRate());
			ratesRepository.save(rateToBeCreated);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		model.addAttribute("rates", ratesRepository.findAll());
		return "/rates/rates-list";

		// TODO fix create form padding
	}
}
