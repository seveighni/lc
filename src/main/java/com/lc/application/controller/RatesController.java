package com.lc.application.controller;

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
	public String getRates(Model model, @RequestParam(required = false) String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		try {
			var paging = PageRequest.of(page - 1, size, Sort.by("id"));
			Page<Rates> pageRates;
			if (name == null) {
				pageRates = ratesRepository.findAll(paging);
			} else {
				pageRates = ratesRepository.findByNameContainingIgnoreCase(name, paging);
				model.addAttribute("name", name);
			}
			model.addAttribute("rate", new Rates());
			model.addAttribute("rates", pageRates.getContent());
			model.addAttribute("currentPage", pageRates.getNumber() + 1);
			model.addAttribute("totalItems", pageRates.getTotalElements());
			model.addAttribute("totalPages", pageRates.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/rates/rates-list";
	}

	@PostMapping
	public String createRate(@Valid @ModelAttribute("rate") Rates rate, BindingResult result, Model model) {
		try {
			if (result.hasErrors() || rate.getName().isBlank()) {
				model.addAttribute("rate", rate);
				model.addAttribute("rates", ratesRepository.findAll());
				return "redirect:/rates";
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
		return "redirect:/rates";
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
