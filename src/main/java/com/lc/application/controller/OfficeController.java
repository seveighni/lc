package com.lc.application.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lc.application.model.Office;
import com.lc.application.repository.OfficeRepository;

@Controller
public class OfficeController {

    @Autowired
    private OfficeRepository officeRepository;

    @GetMapping("/offices")
    public String getOffices(
            Model model, @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        try {
            List<Office> offices = new ArrayList<Office>();
            var paging = PageRequest.of(page - 1, size);

            Page<Office> pageOffices;
            if (address == null) {
                pageOffices = officeRepository.findAll(paging);
            } else {
                pageOffices = officeRepository.findByAddressContainingIgnoreCase(address, paging);
                model.addAttribute("address", address);
            }

            offices = pageOffices.getContent();

            model.addAttribute("offices", offices);
            model.addAttribute("currentPage", pageOffices.getNumber() + 1);
            model.addAttribute("totalItems", pageOffices.getTotalElements());
            model.addAttribute("totalPages", pageOffices.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (
        Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "offices";
    }
}
