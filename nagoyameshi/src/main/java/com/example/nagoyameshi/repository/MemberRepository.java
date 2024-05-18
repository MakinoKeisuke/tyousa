package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	public Member findByEmail(String email);
}
