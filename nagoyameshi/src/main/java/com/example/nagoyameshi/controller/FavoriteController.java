package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ShopeRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
public class FavoriteController {
	 private final FavoriteRepository  favoriteRepository;
	 private final ShopeRepository	  shopeRepository;
	 private final FavoriteService favoriteService;
	 
	 public  FavoriteController(FavoriteRepository  favoriteRepository,ShopeRepository	shopeRepository,FavoriteService favoriteService) {
		 this.favoriteRepository = favoriteRepository;
		 this.shopeRepository = shopeRepository;
		 this.favoriteService = favoriteService;
	 }
	 
	 @GetMapping("/favorites")
	 public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,Model model) {
		 Member member = userDetailsImpl.getMember();
		 Page<Favorite> favoritePage = favoriteRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
		 
		 model.addAttribute("favoritePage", favoritePage);
		 
		 return "favorites/index";
	 }
	 
	 @PostMapping("/shopes/{shopeId}/favorites/create")
	 public String create(@PathVariable(name = "shopeId") Integer shopeId,
			 			   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			 			   RedirectAttributes redirectAttributes,
			 			   Model model) 
	 {
		 Shope shope = shopeRepository.getReferenceById(shopeId);
		 Member member = userDetailsImpl.getMember();
		 
		 favoriteService.create(shope, member);
		 redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
		 
		 return "redirect:/shopes/{shopeId}";
	 }
	 
	 @PostMapping("/shopes/{shopeId}/favorites/{favoriteId}/delete")
	 public String delete(@PathVariable(name = "favoriteId") Integer favoriteId, RedirectAttributes redirectAttributes) {
		 favoriteRepository.deleteById(favoriteId);
		 
		 redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
		 
		 return "redirect:/shopes/{shopeId}";
	 }
}
