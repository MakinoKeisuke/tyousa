package com.example.nagoyameshi.form;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "予約日を選択してください。")
	private String fromReservationDate;
	
	@NotBlank(message = "予約時間を選択してください。")
	private String fromReservationTime;
	
	@NotNull(message = "予約人数を入力してください。")
	@Min(value = 1, message = "予約人数は1人以上に設定してください。")
	private Integer numberOfPeople;
	
	public LocalDate getReservationDate() {
		String reservationDate = getFromReservationDate();
		return LocalDate.parse(reservationDate);
	}
	public LocalTime getReservationTime() {
		String reservationTime = getFromReservationTime();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(reservationTime, timeFormatter);
	}
	
}
