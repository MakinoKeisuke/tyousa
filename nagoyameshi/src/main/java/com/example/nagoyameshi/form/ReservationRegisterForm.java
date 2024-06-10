package com.example.nagoyameshi.form;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {
	private Integer shopeId;
	
	private Integer memberId;
	
	private String reservationDate;
	
	private String reservationTime;
	
	private Integer numberOfPeople;
	
}
