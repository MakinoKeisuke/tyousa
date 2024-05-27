package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Shope;
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
							 @RequestParam(name = "category", required = false) String categoryName,
							 @RequestParam(name = "lowPrice", required = false) Integer lowPrice,
							 @RequestParam(name = "heighPrice", required = false) Integer heighPrice,
							 @PageableDefault(page = 0, size = 10, sort= "id", direction =  Direction.ASC) Pageable pageable,
							 Model model)
		{
			Page<Shope> shopePage = Page.empty();
			
			if(keyword != null && !keyword.isEmpty()) {
				shopePage = shopeRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
			}else if(categoryName != null && !categoryName.isEmpty()) {
				Category category = new Category();
				category.setName(categoryName);
				shopePage = shopeRepository.findByCategory(category, pageable);
			}else if(lowPrice != null) {
				shopePage = shopeRepository.findByLowPriceLessThanEqual(lowPrice, pageable);
			}else if(heighPrice != null){
				shopePage = shopeRepository.findByHeighPriceLessThanEqual(heighPrice, pageable);
			}else {
				shopePage = shopeRepository.findAll(pageable);
			}
		
			
			model.addAttribute("shopePage", shopePage);
			model.addAttribute("keyword", keyword);
			model.addAttribute("category", categoryName);
			model.addAttribute("lowPrice", lowPrice);
			model.addAttribute("heighPrice", heighPrice);
			
			return "shopes/index";
		}
	}


