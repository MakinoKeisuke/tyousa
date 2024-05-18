package com.example.nagoyameshi.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashGenerator {
	
	 public static void main(String[] args) {
		 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	     String rawPassword1 = "password";
	     String encodedPassword1 = passwordEncoder.encode(rawPassword1);
	     System.out.println("Hashed password for 'password' (user1): " + encodedPassword1);

	     String rawPassword2 = "password";
	     String encodedPassword2 = passwordEncoder.encode(rawPassword2);
	     System.out.println("Hashed password for 'password' (user2): " + encodedPassword2);

	    // Use these hashed passwords in your SQL file
	 }
}
