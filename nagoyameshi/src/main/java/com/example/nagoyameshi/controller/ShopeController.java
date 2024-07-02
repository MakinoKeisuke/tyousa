package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.ShopeRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/shopes")
public class ShopeController {
		private final ShopeRepository shopeRepository;
		private final ReviewRepository reviewRepository;
		private final ReviewService reviewService;
		private final FavoriteRepository favoriteRepository;
		private final FavoriteService favoriteService;
		
		public  ShopeController(ShopeRepository shopeRepository,ReviewRepository reviewRepository,ReviewService reviewService,FavoriteRepository favoriteRepository,FavoriteService favoriteService) {
			this.shopeRepository = shopeRepository;
			this.reviewRepository = reviewRepository;
			this.reviewService = reviewService;
			this.favoriteRepository = favoriteRepository;
			this.favoriteService = favoriteService;
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
		public String show(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
			Shope shope = shopeRepository.getReferenceById(id);
			Favorite favorite = null;
			boolean hasMemberAlreadyReviewed = false;
			boolean isFavorite = false;
			
			if(userDetailsImpl != null) {
				Member memebr = userDetailsImpl.getMember();
				hasMemberAlreadyReviewed = reviewService.hasMemberAlreadyReviewed(shope, memebr);
				isFavorite = favoriteService.isFavorite(shope, memebr);
				if(isFavorite) {
					favorite = favoriteRepository.findByShopeAndMember(shope, memebr);
				}
			}
			
			List<Review> newReviews = reviewRepository.findTop6ByShopeOrderByCreatedAtDesc(shope);
			long totalReviewCount = reviewRepository.countByShope(shope);
			
			model.addAttribute("shope", shope);
			model.addAttribute("reservationInputForm", new ReservationInputForm());
			model.addAttribute("hasMemberAlreadyReviewed",  hasMemberAlreadyReviewed);
			model.addAttribute("newReviews", newReviews);
			model.addAttribute("totalReviewCount", totalReviewCount);
			model.addAttribute("isFavorite", isFavorite);
			model.addAttribute("favorite", favorite );
			
			return "shopes/show";
		}
	}


