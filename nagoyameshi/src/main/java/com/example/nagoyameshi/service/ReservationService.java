package com.example.nagoyameshi.service;


import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Shope;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.ShopeRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final ShopeRepository shopeRepository;
	private final MemberRepository memberRepository;
	
	public ReservationService(ReservationRepository reservationRepository, ShopeRepository shopeRepository,MemberRepository memberRepository) {
		this.reservationRepository = reservationRepository;
		this.shopeRepository = shopeRepository;
		this.memberRepository = memberRepository;
	}
	
	@Transactional
	public void create(ReservationRegisterForm reservationRegisterForm) {
		 Reservation reservation = new  Reservation();
		 Shope shope = shopeRepository.getReferenceById(reservationRegisterForm.getShopeId());
		 Member member = memberRepository.getReferenceById(reservationRegisterForm.getMemberId());
		 LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
		 LocalTime reservationTime = LocalTime.parse(reservationRegisterForm.getReservationTime(), DateTimeFormatter.ofPattern("HH:mm"));
		 
		 if(isHoliday(reservationDate, shope.getHoliday())) {
			 throw new IllegalArgumentException("予約日は店舗の定休日です");
		 }
		 if(!isWithinOpenTime(reservationTime, shope.getOpenTime())) {
			 throw new IllegalArgumentException("予約時間は店舗の営業開始時間より前です。");
		 }
		 
		 if(!isWithinCloseTime(reservationTime, shope.getCloseTime())) {
			 throw new IllegalArgumentException("予約時間は店舗の営業終了時間の後です。");
		 }
		 
		 reservation.setShope(shope);
		 reservation.setMember(member);
		 reservation.setReservationDate(reservationDate);
		 reservation.setReservationTime(reservationTime);
		 reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
		 
		 reservationRepository.save(reservation);
		 
	}
	public boolean isWithinOpenTime(LocalTime reservationTime, Time openTime) {
		return !reservationTime.isBefore(openTime.toLocalTime());
	}
	public boolean isWithinCloseTime(LocalTime reservationTime, Time closeTime) {
	    return !reservationTime.isAfter(closeTime.toLocalTime());
	}
	
	public boolean isHoliday(LocalDate reservationDate, String holidays) {
        List<String> holidayList = Arrays.asList(holidays.split(","));
        DayOfWeek dayOfWeek = reservationDate.getDayOfWeek();
        String dayOfWeekJapanese = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.JAPANESE);
        String dayOfWeekStr = dayOfWeek.toString().substring(0, 1).toUpperCase() + dayOfWeek.toString().substring(1).toLowerCase(); // "MONDAY" -> "Monday"
        return holidayList.contains(dayOfWeekJapanese) || holidayList.contains(dayOfWeekStr);
       
    }
	
	public void validateReservation(ReservationRegisterForm reservationRegisterForm) {
        Shope shope = shopeRepository.getReferenceById(reservationRegisterForm.getShopeId());
        LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
        LocalTime reservationTime = LocalTime.parse(reservationRegisterForm.getReservationTime(), DateTimeFormatter.ofPattern("HH:mm"));

        if (isHoliday(reservationDate, shope.getHoliday())) {
            throw new IllegalArgumentException("予約日は店舗の定休日です");
        }
        if (!isWithinOpenTime(reservationTime, shope.getOpenTime())) {
            throw new IllegalArgumentException("予約時間は店舗の営業開始時間より前です。");
        }
        if (!isWithinCloseTime(reservationTime, shope.getCloseTime())) {
            throw new IllegalArgumentException("予約時間は店舗の営業終了時間の後です。");
        }
    }
}
