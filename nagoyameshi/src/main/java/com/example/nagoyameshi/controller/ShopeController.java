package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.ShopeRepository;

@Controller
@RequestMapping("/shopes")
public class ShopeController {
		private final ShopeRepository shopeRepository;
		
		
		public  ShopeController(ShopeRepository shopeRepository) {
			this.shopeRepository = shopeRepository;
		}
		
		@GetMapping
		public String index(@RequestParam(name = "keyword", required = false) String keyword,
							 @RequestParam(name = "category", required = false) Category category,
							 @RequestParam(name = "lowPrice", required = false) Integer lowPrice,
							 @RequestParam(name = "heighPrice", required = false) Integer heighPrice,
							 @RequestParam(name = "order", required = false) String order,
							 @PageableDefault(page = 0, size = 10, sort= "id", direction =  Direction.ASC) Pageable pageable,
							 Model model)
		{
			Page<Shope> shopePage = Page.empty();
			
			if(keyword != null && !keyword.isEmpty()) {
				//shopePage = shopeRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
				if(order != null && order.equals("priceAsc")) {
					shopePage = shopeRepository.findByNameLikeOrAddressLikeOrderByLowPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
				}else {
					shopePage = shopeRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
				}
			}else if(category != null) {
				shopePage = shopeRepository.findByCategory(category, pageable);
			}else if(lowPrice != null) {
				//shopePage = shopeRepository.findByLowPriceLessThanEqual(lowPrice, pageable);
				if(order != null && order.equals("priceAsc")) {
					shopePage = shopeRepository.findByLowPriceLessThanEqualOrderByLowPriceAsc(lowPrice, pageable);
				}else {
					shopePage = shopeRepository.findByLowPriceLessThanEqualOrderByCreatedAtDesc(lowPrice, pageable);
				}
			}else if(heighPrice != null){
				//shopePage = shopeRepository.findByHeighPriceLessThanEqual(heighPrice, pageable);
				if(order != null && order.equals("priceAsc")) {
					shopePage = shopeRepository.findByHeighPriceLessThanEqualOrderByLowPriceAsc(heighPrice, pageable);
				}else {
					shopePage = shopeRepository.findByHeighPriceLessThanEqualOrderByCreatedAtDesc(heighPrice, pageable);
				}
			}else {
				//shopePage = shopeRepository.findAll(pageable);
				if(order != null && order.equals("priceAsc")) {
					shopePage = shopeRepository.findAllByOrderByLowPriceAsc(pageable);
				}else {
					shopePage = shopeRepository.findAllByOrderByCreatedAtDesc(pageable);
				}
			}
		
			
			model.addAttribute("shopePage", shopePage);
			model.addAttribute("keyword", keyword);
			model.addAttribute("category", category);
			model.addAttribute("lowPrice", lowPrice);
			model.addAttribute("heighPrice", heighPrice);
			model.addAttribute("order", order);
			
			return "shopes/index";
		}
		
		@GetMapping("/{id}")
		public String show(@PathVariable(name = "id") Integer id, Model model) {
			Shope shope = shopeRepository.getReferenceById(id);
			
			model.addAttribute("shope", shope);
			model.addAttribute("reservationInputForm", new ReservationInputForm());
			
			return "shopes/show";
		}
	}


