package com.show.reservation.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RowModel {
	
	private Integer showId;
	private String rowId;
	private Integer seatCount;
	
	private Set<String> seatsSet;
}
