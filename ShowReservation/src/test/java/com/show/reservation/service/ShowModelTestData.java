package com.show.reservation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.show.reservation.model.RowModel;
import com.show.reservation.model.ShowModel;
import com.show.reservation.model.ShowReservationModel;

@Component
public class ShowModelTestData {
	
	public ShowModel createShowModelDataTest() {
		Set<String> seatstList = new HashSet<>(Arrays.asList("R1-1", "R1-2", "R1-3", "R1-4", "R1-5"));
		List<RowModel> rowList = Arrays
				.asList(RowModel.builder().rowId("R1").showId(1).seatCount(5).seatsSet(seatstList).build());
		return ShowModel.builder().showId(1).expiryTime(2).rowCount(5).seatCount(150).rowModelList(rowList).build();
	}
	
	public List<ShowReservationModel> createShowReservationDataTest(Integer showId, Integer seatCount) {
		
		List<ShowReservationModel> showReservationModels = new ArrayList<ShowReservationModel>();
		showReservationModels.add(createShowReservationModel(showId, seatCount));
		return showReservationModels;
	}
	
	public ShowReservationModel createShowReservationModel(Integer showId, Integer seatCount) {
		return ShowReservationModel.builder().showId(showId).phoneNumber("1234").dateCreated(new Date())
		.ticketNumber(11).seatsSelected(createSeatSet(seatCount, "R1")).build();
	}
	
	public Set<String> createSeatSet(int seatsCount, String rowId) {
		return IntStream.rangeClosed(1, seatsCount).mapToObj(i -> rowId + "-" + i)
				.collect(Collectors.toSet());
        
	}

}
