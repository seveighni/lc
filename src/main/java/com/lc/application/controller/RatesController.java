package com.lc.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.application.dto.RatesDto;
import com.lc.application.dto.ResultDto;
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

	@GetMapping("/{id}")
	public String getRate(Model model, @PathVariable Long id) {
		try {
			Rates rates = ratesRepository.findById(id).get();
			RatesDto ratesToBeUpdated = new RatesDto(rates);
			model.addAttribute("rate", ratesToBeUpdated);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/rates/rates-edit";
	}

	@PostMapping("/{id}")
	public String editRate(@Valid @ModelAttribute("rate") RatesDto ratesDto, BindingResult result, Model model,
			@PathVariable Long id) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("rate", ratesDto);
				return "/rates/rates-edit";
			}

			var opt = ratesRepository.findById(id);
			if (opt.isEmpty()) {
				model.addAttribute("message", "Rates not found");
				return "redirect:/rates/rates-list";
			}
			var rates = opt.get();
			rates.setName(ratesDto.getName());
			rates.setPerKg(ratesDto.getPerKg());
			rates.setFlatRate(ratesDto.getFlatRate());
			ratesRepository.save(rates);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		model.addAttribute("result", new ResultDto("Rates updated successfully!", true));
		return "/rates/rates-edit";
	}
}
