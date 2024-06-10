package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.repository.ShopeRepository;

@Controller
public class HomeController {
	private final ShopeRepository shopeRepository;
	
	public HomeController(ShopeRepository shopeRepository) {
		this.shopeRepository = shopeRepository;
	}
	@GetMapping("/")
	public String index(Model model) {
		List<Shope> newShopes = shopeRepository.findTop10ByOrderByCreatedAtDesc();
		model.addAttribute("newShopes", newShopes);
		
		return "index";
	}
}
