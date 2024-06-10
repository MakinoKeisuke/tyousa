package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Shope;

public interface ShopeRepository extends JpaRepository<Shope, Integer>{
	public Page<Shope> findByNameLike(String keyword, Pageable pageable);
	
	//public Page<Shope> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
	
	//public Page<Shope> findByCategoryLikeOrderByCreatedAtDesc(Category category, Pageable pageable);
	//public Page<Shope> findByCategoryLikeOrderByHeighPriceAsc(Category category, Pageable pageable);
	//public Page<Shope> findByLowPriceLessThanEqual(Integer lowPrice, Pageable pageable);
	//public Page<Shope> findByHeighPriceLessThanEqual(Integer heighPrice, Pageable pageable);
	
	public Page<Shope> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	public Page<Shope> findByNameLikeOrAddressLikeOrderByLowPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
	public Page<Shope> findByCategory(Category category, Pageable pageable);
	public Page<Shope> findByLowPriceLessThanEqualOrderByCreatedAtDesc(Integer lowPrice, Pageable pageable);
	public Page<Shope> findByLowPriceLessThanEqualOrderByLowPriceAsc(Integer lowPrice, Pageable pageable);
	public Page<Shope> findByHeighPriceLessThanEqualOrderByCreatedAtDesc(Integer heighPrice, Pageable pageable);
	public Page<Shope> findByHeighPriceLessThanEqualOrderByLowPriceAsc(Integer heighPrice, Pageable pageable);
    public Page<Shope> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Shope> findAllByOrderByLowPriceAsc(Pageable pageable);
    
    public List<Shope> findTop10ByOrderByCreatedAtDesc();
 // 複合条件: `lowPrice` 以下の値段で特定のカテゴリに属するショップを取得
    public Page<Shope> findByLowPriceLessThanEqualAndCategory(Integer lowPrice, Category category, Pageable pageable);
    // 他の必要なクエリメソッドをここに追加できます

	
}

