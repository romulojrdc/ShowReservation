package com.show.reservation.dto;

import java.util.Set;

import lombok.Data;

@Data
public class ShowReservationDto extends ShowBookingSetupInput {

	private Integer ticketNumber;
	private String phoneNumber;
	private Set<String> seatSet;
	
}
