package com.show.reservation.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.show.reservation.constant.StringConstant;
import com.show.reservation.dao.ShowDAO;
import com.show.reservation.dto.ShowBookingSetupInput;
import com.show.reservation.model.RowModel;
import com.show.reservation.model.ShowModel;

@Service
public class ShowBookingService {
	
	// TODO Can be configure in property file
	public static final Integer MAX_ROW_COUNT = 26;
	public static final Integer MAX_SEAT_COUNT = 10;
	public static final Integer MINUMUM_EXPIRY_TIME = 2; // default if empty
	
	@Autowired
	private ShowDAO showDAO;
	
	// if DB Design this will be repository
	
	public ShowModel findByShowNumber(Integer showId) {
		return showDAO.findById(showId);
	}
	
	public String initializeShow(ShowBookingSetupInput showInitializerInput) {
		String errorMessage = isValidShowBookingSetupInput(showInitializerInput);
		if (null == errorMessage || errorMessage.isEmpty()) {
			return createShowData(showInitializerInput);
		}
		return errorMessage;
	}

	
	private String createShowData(ShowBookingSetupInput showInitializerInput) {
		// check if id already exists
		if (null != showDAO.findById(showInitializerInput.getShowNumber())) {
			return String.format(StringConstant.INVALID_SHOW_ID_EXISTS, showInitializerInput.getShowNumber());
		}
		// 
		ShowModel showModel = populateShowData(showInitializerInput);
		Integer showId = showDAO.save(showModel);
		return String.format(StringConstant.SUCCESS_SHOW_CREATED, showId);
	}


	private ShowModel populateShowData(ShowBookingSetupInput showInitializerInput) {
		ShowModel showModel= mapShowModel(showInitializerInput);
		showModel.setRowModelList(populateRowRecord(showModel));
		return showModel;
	}


	private List<RowModel> populateRowRecord(ShowModel showModel) {
		List<RowModel> rowModelList = new LinkedList<RowModel>();
		for (int i = 1; i <= showModel.getRowCount(); i++) {
			String rowId = StringConstant.ROW_PREFEND + i;
			rowModelList.add(RowModel.builder().rowId(rowId).showId(showModel.getShowId()).seatCount(showModel.getSeatCount())
					.seatsSet(populateSeats(rowId, showModel.getSeatCount())).build());
		}
		return rowModelList;
	}


	private Set<String> populateSeats(String rowId, int seatsCount) {
		return IntStream.rangeClosed(1, seatsCount).mapToObj(i -> rowId + "-" + i)
				.collect(Collectors.toSet());
	}


	private ShowModel mapShowModel(ShowBookingSetupInput showInitializerInput) {
		ShowModel showModel= new ShowModel();
		showModel.setShowId(showInitializerInput.getShowNumber());
		showModel.setRowCount(showInitializerInput.getRowCount());
		showModel.setSeatCount(showInitializerInput.getSeatCount());
		showModel.setExpiryTime(showInitializerInput.getExpiryTime());
		
		return showModel;
	}


	private String isValidShowBookingSetupInput(ShowBookingSetupInput showBookingSetupInput) {
		
		showBookingSetupInput = Optional.ofNullable(showBookingSetupInput).orElse(new ShowBookingSetupInput());
		Integer rowCount = Optional.ofNullable(showBookingSetupInput.getRowCount()).orElse(0);
		Integer seatCount = Optional.ofNullable(showBookingSetupInput.getSeatCount()).orElse(0);

		StringBuilder errorMessage = new StringBuilder();
		if (!(0 < rowCount && MAX_ROW_COUNT >= rowCount)) {
			errorMessage.append(String.format(StringConstant.INVALID_ROW_COUNT_RANGE, 0, MAX_ROW_COUNT));
		}
		if (!(0 < seatCount && MAX_SEAT_COUNT >= seatCount)) {
			if (errorMessage.length() > 0) {
				errorMessage.append(StringConstant.NEW_LINE);
			}	
			errorMessage.append(String.format(StringConstant.INVALID_SEATS_COUNT_RANGE,0, MAX_SEAT_COUNT));
		}
		return errorMessage.toString();
	}
}
