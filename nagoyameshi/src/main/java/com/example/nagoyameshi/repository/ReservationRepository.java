package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>{
	Page<Reservation> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
	
	
}
