package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.ShopeRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final ShopeRepository shopeRepository;
	private final ReservationService reservationService;
	
	public ReservationController(ReservationRepository reservationRepository,ShopeRepository shopeRepository,ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.shopeRepository = shopeRepository;
		this.reservationService = reservationService;
	}
	
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		Member member = userDetailsImpl.getMember();
		Page<Reservation> reservationPage = reservationRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		
		return "reservations/index";
	}
	
	@GetMapping("/shopes/{id}/reservations/input")
	public String input(@PathVariable(name = "id") Integer id,
						 @ModelAttribute @Validated ReservationInputForm reservationInputForm,
						 BindingResult bindingResult,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		Shope shope = shopeRepository.getReferenceById(id);
		Member member = userDetailsImpl.getMember();
		if(bindingResult.hasErrors()) {
			model.addAttribute("shope", shope);
			model.addAttribute("errorMessage", "予約内容に不備があります。");
			return "shopes/show";
		}
		
		try {
			// 追加部分: ReservationRegisterFormを作成
			
			ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(
						shope.getId(), member.getId(), reservationInputForm.getReservationDate().toString(),
						reservationInputForm.getReservationTime().toString(),
						reservationInputForm.getNumberOfPeople()
				);
			reservationService.validateReservation(reservationRegisterForm);
			// 一旦予約作成を行わずに確認画面へリダイレクト
            redirectAttributes.addFlashAttribute("reservationRegisterForm", reservationRegisterForm);
            redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm); // 確認画面に必要な場合
        } catch (IllegalArgumentException e) {
	         model.addAttribute("shope", shope);
	         model.addAttribute("errorMessage", e.getMessage());
	         return "shopes/show";
	    }
		
		return "redirect:/shopes/{id}/reservations/confirm";
	}
	
	@GetMapping("/shopes/{id}/reservations/confirm")
	public String confirm(@PathVariable(name = "id") Integer id,
						   @ModelAttribute ReservationInputForm reservationInputForm,
						   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						   Model model) {
		Shope shope = shopeRepository.getReferenceById(id);
		Member member = userDetailsImpl.getMember();
		
		//予約日と予約時間を取得する
		String fromReservationDate = reservationInputForm.getFromReservationDate();
		String fromReservationTime = reservationInputForm.getFromReservationTime();
		
		ReservationRegisterForm  reservationRegisterForm = new ReservationRegisterForm(shope.getId(),member.getId(),fromReservationDate,fromReservationTime,reservationInputForm.getNumberOfPeople());
		
		model.addAttribute("shope", shope);
		model.addAttribute("reservationRegisterForm", reservationRegisterForm);
		
		return "reservations/confirm";
	}
	
	@PostMapping("/shopes/{id}/reservations/create")
	public String create(@ModelAttribute ReservationRegisterForm  reservationRegisterForm, RedirectAttributes redirectAttributes,Model model) {
		 try {
	            reservationService.create(reservationRegisterForm);
	        } catch (IllegalArgumentException e) {
	            Shope shope = shopeRepository.getReferenceById(reservationRegisterForm.getShopeId());
	            model.addAttribute("shope", shope);
	            model.addAttribute("errorMessage", e.getMessage());
	            return "reservations/confirm";
	        }
		
		redirectAttributes.addFlashAttribute("reserved", true);
		return "redirect:/reservations";
	}
}

