package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	@Transactional
	public void create(Shope shope,Member member) {
		Favorite favorite = new Favorite();
		
		favorite.setShope(shope);
		favorite.setMember(member);
		
		favoriteRepository.save(favorite);
	}
	
	public boolean isFavorite(Shope shope, Member member) {
		return favoriteRepository.findByShopeAndMember(shope, member) != null;
	}
}
