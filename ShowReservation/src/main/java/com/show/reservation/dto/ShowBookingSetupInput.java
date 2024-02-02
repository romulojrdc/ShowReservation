package com.show.reservation.dto;

import lombok.Data;

@Data
public class ShowBookingSetupInput {
	
	private String command;
	private int showNumber;
	private int rowCount;
	private int seatCount;
	private int expiryTime; // minutes
	private String errorMessage;
}
