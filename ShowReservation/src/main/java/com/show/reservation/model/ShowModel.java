package com.show.reservation.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowModel {

	private Integer showId;
	private Integer rowCount;
	private Integer seatCount;
	private Integer expiryTime;
	
	private List<RowModel> rowModelList;
	
}
