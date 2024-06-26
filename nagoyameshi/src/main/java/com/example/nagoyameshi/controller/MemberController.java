package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.form.MemberEditForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	private final MemberRepository  memberRepository;
	private final MemberService memberService;
	
	public  MemberController(MemberRepository  memberRepository,MemberService memberService) {
		this.memberRepository = memberRepository;
		this.memberService = memberService;
	}
	
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		Member member = memberRepository.getReferenceById(userDetailsImpl.getMember().getId());
		
		model.addAttribute("member", member);
		
		return "member/index";
	}
	
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		Member member = memberRepository.getReferenceById(userDetailsImpl.getMember().getId());
		MemberEditForm memberEditForm = new MemberEditForm(member.getId(), member.getName(),member.getFurigana(),member.getPostalCode(),member.getAddress(),member.getPhoneNumber(),member.getEmail());
		
		model.addAttribute("memberEditForm", memberEditForm);
		
		return "member/edit";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated MemberEditForm memberEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		//メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
		if(memberService.isEmailChanged(memberEditForm) && memberService.isEmailRegistered(memberEditForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(),"email","すでに登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}
		
		if(bindingResult.hasErrors()) {
			return "member/edit";
		}
		
		memberService.update(memberEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
		
		return "redirect:/member";
	}
	
}
