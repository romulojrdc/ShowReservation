package com.show.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatModel {
	
	private Integer seatId;
	private int rowId;
	private int showId;
	

}
