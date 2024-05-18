package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ShopeEditForm;
import com.example.nagoyameshi.form.ShopeRegisterForm;
import com.example.nagoyameshi.repository.ShopeRepository;

@Service
public class ShopeService {
	private final ShopeRepository shopeRepository;
	
	public ShopeService(ShopeRepository shopeRepository) {
		this.shopeRepository = shopeRepository;
	}
	
	@Transactional
	public void create(ShopeRegisterForm shopeRegisterForm) {
		Shope shope = new Shope();
		MultipartFile imageFile = shopeRegisterForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath =  Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			shope.setImageName(hashedImageName);
		}
		shope.setName(shopeRegisterForm.getName());
		shope.setDescription(shopeRegisterForm.getDescription());
		shope.setLowPrice(shopeRegisterForm.getLowPrice());
		shope.setHeighPrice(shopeRegisterForm.getHeighPrice());
		shope.setOpenTime(shopeRegisterForm.getOpenTime());
		shope.setCloseTime(shopeRegisterForm.getCloseTime());
		shope.setPostalCode(shopeRegisterForm.getPostalCode());
		shope.setAddress(shopeRegisterForm.getAddress());
		shope.setPhoneNumber(shopeRegisterForm.getPhoneNumber());
		shope.setHoliday(shopeRegisterForm.getHoliday());
		shope.setCategory(shopeRegisterForm.getCategory());
		
		shopeRepository.save(shope);
	}
	
	@Transactional
	public void update(ShopeEditForm shopeEditForm) {
		Shope shope = shopeRepository.getReferenceById(shopeEditForm.getId());
		MultipartFile imageFile = shopeEditForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			shope.setImageName(hashedImageName);
		}
		
		shope.setName(shopeEditForm.getName());
		shope.setDescription(shopeEditForm.getDescription());
		shope.setLowPrice(shopeEditForm.getLowPrice());
		shope.setHeighPrice(shopeEditForm.getHeighPrice());
		shope.setOpenTime(shopeEditForm.getOpenTime());
		shope.setCloseTime(shopeEditForm.getCloseTime());
		shope.setPostalCode(shopeEditForm.getPostalCode());
		shope.setAddress(shopeEditForm.getAddress());
		shope.setPhoneNumber(shopeEditForm.getPhoneNumber());
		shope.setHoliday(shopeEditForm.getHoliday());
		shope.setCategory(shopeEditForm.getCategory());
		
		shopeRepository.save(shope);
	}
	
	//UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\."); 
		for(int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}
	
	//画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
