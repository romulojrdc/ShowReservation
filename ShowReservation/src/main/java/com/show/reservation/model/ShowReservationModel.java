package com.show.reservation.model;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowReservationModel {
	
	private Integer ticketNumber;
	private String phoneNumber;
	private Integer showId;
	private Date dateCreated;
	private Set<String> seatsSelected;

}
