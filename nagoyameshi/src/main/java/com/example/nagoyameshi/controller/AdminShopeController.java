package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ShopeEditForm;
import com.example.nagoyameshi.form.ShopeRegisterForm;
import com.example.nagoyameshi.repository.ShopeRepository;
import com.example.nagoyameshi.service.ShopeService;

@Controller
@RequestMapping("/admin/shopes")
public class AdminShopeController {
	private final ShopeRepository shopeRepository;
	private final ShopeService shopeService;
	
	public AdminShopeController(ShopeRepository shopeRepository, ShopeService shopeService) {
		this.shopeRepository = shopeRepository;
		this.shopeService = shopeService;
	}
	@GetMapping
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort="id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		Page<Shope> shopPage;
		
		if(keyword != null && !keyword.isEmpty()) {
			shopPage = shopeRepository.findByNameLike("%" + keyword + "%", pageable);
		}else {
			shopPage = shopeRepository.findAll(pageable);
		}
		
		model.addAttribute("shopePage", shopPage);
		model.addAttribute("keyword", keyword);
		
		return "admin/shopes/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		Shope shope = shopeRepository.getReferenceById(id);
		
		model.addAttribute("shope", shope);
		
		return "admin/shopes/show";
	}
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("shopeRegisterForm", new ShopeRegisterForm());
		return "admin/shopes/register";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated  ShopeRegisterForm  shopeRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "admin/shopes/register";
		}
		
		shopeService.create(shopeRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");
		
		return "redirect:/admin/shopes";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		Shope shope = shopeRepository.getReferenceById(id);
		String imageName = shope.getImageName();
		ShopeEditForm shopeEditForm = new ShopeEditForm(shope.getId(), shope.getName(), null, shope.getDescription(), shope.getLowPrice(), shope.getHeighPrice(), shope.getOpenTime(), shope.getCloseTime(), shope.getPostalCode(), shope.getAddress(), shope.getPhoneNumber(), shope.getHoliday(), shope.getCategory());
		
		model.addAttribute("imageName", imageName);
		model.addAttribute("shopeEditForm", shopeEditForm);
		
		return "admin/shopes/edit";
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated ShopeEditForm shopeEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "admin/shopes/edit";
		}
		
		shopeService.update(shopeEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
		
		return "redirect:/admin/shopes";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		shopeRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
		
		return "redirect:/admin/shopes";
	}
}
