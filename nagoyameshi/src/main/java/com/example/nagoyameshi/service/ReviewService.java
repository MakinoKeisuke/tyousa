package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository  reviewRepository;
	
	public ReviewService(ReviewRepository  reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	@Transactional
	public void create(Shope shope, Member member, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();
		
		review.setShope(shope);
		review.setMember(member);
		review.setScore(reviewRegisterForm.getScore());
		review.setContent(reviewRegisterForm.getContent());
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());
		
		reviewRepository.save(review);
	}
	
	public boolean hasMemberAlreadyReviewed(Shope shope, Member member) {
		return reviewRepository.findByShopeAndMember(shope, member) != null;
	}
}
