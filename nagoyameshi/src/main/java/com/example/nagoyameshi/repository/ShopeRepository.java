package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Shope;

public interface ShopeRepository extends JpaRepository<Shope, Integer>{
	public Page<Shope> findByNameLike(String keyword, Pageable pageable);
}
