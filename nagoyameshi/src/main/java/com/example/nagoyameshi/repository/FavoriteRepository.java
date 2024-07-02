package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Shope;

public interface FavoriteRepository extends JpaRepository<Favorite,Integer>{
	public Page<Favorite> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
	public Favorite findByShopeAndMember(Shope shope, Member member);
}
