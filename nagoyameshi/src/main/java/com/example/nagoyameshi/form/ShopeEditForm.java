package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopeEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "店舗名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "説明を入力してください。")
	private String description;
	
	@NotNull(message = "最低料金を入力してください。")
	private Integer lowPrice;
	
	@NotNull(message = "最高料金を入力してください。")
	private Integer heighPrice;
	
	@NotNull(message = "開始時間を入力してください。")
	private java.sql.Time openTime;
	
	@NotNull(message = "終了時間を入力してください。")
	private java.sql.Time closeTime;
	
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;
	
	@NotBlank(message = "定休日を入力してください。")
	private String holiday;
	
	@NotNull(message = "カテゴリを選択してください。")
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
}
