package com.example.nagoyameshi.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private final PasswordEncoder passwordEncoder;

	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void createUser(String username, String rawPassword) {
		String encodedPassword = passwordEncoder.encode(rawPassword);
		// Database insertion logic here
		// e.g., userRepository.save(new User(username, encodedPassword));
	}

	// This might be part of your initialization logic
	public static void main(String[] args) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// Example encoded passwords
		System.out.println(passwordEncoder.encode("password")); // For manual insertion in your SQL file
	}
}
