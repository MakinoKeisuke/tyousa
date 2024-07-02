package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Shope;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	public List<Review> findTop6ByShopeOrderByCreatedAtDesc(Shope shope);
	public Review findByShopeAndMember(Shope shope,Member member);
	public long countByShope(Shope shope);
	public Page<Review> findByShopeOrderByCreatedAtDesc(Shope shope, Pageable pageable);
}
