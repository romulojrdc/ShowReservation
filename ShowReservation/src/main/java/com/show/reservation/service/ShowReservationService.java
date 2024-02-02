package com.show.reservation.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.show.reservation.constant.StringConstant;
import com.show.reservation.dao.InMemoryShowReservationDAO;
import com.show.reservation.dto.ShowReservationDto;
import com.show.reservation.model.ShowModel;
import com.show.reservation.model.ShowReservationModel;

@Service
public class ShowReservationService {

	@Autowired
	private InMemoryShowReservationDAO showReservationDAO;

	@Autowired
	private ShowBookingService showBookingService;
	
	
	public String viewShowReservation(Integer showId) {
		ShowModel showModel = showBookingService.findByShowNumber(showId);
		if (Objects.isNull(showModel)) {
			return String.format(StringConstant.SHOW_NOT_FOUND_WITH_ID, showId);
		} else {
			List<ShowReservationModel> showReservationList = showReservationDAO.findByShowId(showId);
			if (CollectionUtils.isEmpty(showReservationList)) {
				return String.format(StringConstant.NO_RESERVATION_FOUND_IN_SHOW, showId);
			}
			return populateShowReservations(showReservationList);
		}
	}
	
	public String getShowSeatsAvailability(Integer showId) {
		ShowModel showModel = showBookingService.findByShowNumber(showId);
		if (Objects.isNull(showModel)) {
			return String.format(StringConstant.SHOW_NOT_FOUND_WITH_ID, showId);
		}
		Set<String> showSeats = showModel.getRowModelList().stream()
				.flatMap(seatsSet -> seatsSet.getSeatsSet().stream()).collect(Collectors.toSet());
		
		List<ShowReservationModel> showReservationList = showReservationDAO.findByShowId(showId);
		if (!CollectionUtils.isEmpty(showReservationList)) {
			Set<String> reserveSeats = showReservationList.stream()
					.flatMap(srModel -> srModel.getSeatsSelected().stream()).collect(Collectors.toSet());
			
			return showSeats.stream()
	                .filter(srm -> !reserveSeats.contains(srm))
	                .collect(Collectors.joining(StringConstant.COMMA_SPACER));
		}
		// will return all
		return showSeats.stream().collect(Collectors.joining(StringConstant.COMMA_SPACER));
	}
	
	public String cancelReservation(Integer ticketNumber, String phoneNumber) {
		ShowReservationModel showReservationModel = showReservationDAO.findById(ticketNumber);
		if (Objects.isNull(showReservationModel)) {
			return String.format(StringConstant.INVALID_TICKET_NUMBER_NOT_FOUND, ticketNumber);
		} else {
			if (!showReservationModel.getPhoneNumber().equals(phoneNumber)) {
				return String.format(StringConstant.INVALID_PHONE_NOTMATCH_TICKET, phoneNumber, ticketNumber);
			}
			// validate if reservation can be cancelled
			boolean validForCancellation = isValidForCancellation(showReservationModel);
			if (!validForCancellation) {
				return StringConstant.REACHED_MAXIMUM_TIME_CANCELLATION;
			}
			showReservationDAO.delete(ticketNumber);
		}
		
		return String.format(StringConstant.SUCCESS_TICKET_CANCELLATION, ticketNumber, phoneNumber);
	}
	
	private boolean isValidForCancellation(ShowReservationModel showReservationModel) {
		ShowModel showModel = showBookingService.findByShowNumber(showReservationModel.getShowId());
		if (null != showModel) {
			Date currentDate = new Date();
			long dateDifference = currentDate.getTime() - showReservationModel.getDateCreated().getTime();
			Long secondsDifference = dateDifference / 1000;
			long maxSecondsAllowed = showModel.getExpiryTime().longValue()*60;
			return secondsDifference < maxSecondsAllowed;
		}
		return false;
	}
							
	// will return -1 if error in reservation
	public Integer makeReservation(ShowReservationDto showReservationDto) {
		
		if (CollectionUtils.isEmpty(showReservationDto.getSeatSet())) {
			showReservationDto.setErrorMessage(StringConstant.INVALID_SEATS_REQUIRED);
			return -1;
		}
		// validation duplicate phone and show exist
		boolean phoneShowExists = checkPhoneReservation(showReservationDto);
		if (phoneShowExists) {
			showReservationDto.setErrorMessage(String.format(StringConstant.INVALID_PHONE_ALREADY_IN_USED, showReservationDto.getShowNumber()));
			return -1;
		}
		// check if Seat is valid
		boolean validSeats = validateSeatsInShow(showReservationDto);
		if (!validSeats) {
			return -1;
		}
		// validation for Taken Seats
		String listTakenSeats = checkAvailableSeat(showReservationDto);
		ShowReservationModel showReservationModel = mapShowReservationModel(showReservationDto);
		if (StringUtils.isNoneEmpty(listTakenSeats)) {
			showReservationDto.setErrorMessage(String.format(StringConstant.INVALID_SEATS_ALREADY_TAKEN, listTakenSeats));
			return -1;
		}
		// save reservation
		Integer ticketNumber = showReservationDAO.save(showReservationModel);
		return ticketNumber;
		
	}
	

	private boolean validateSeatsInShow(ShowReservationDto showReservationDto) {
		ShowModel showModel = showBookingService.findByShowNumber(showReservationDto.getShowNumber());
		if (Objects.isNull(showModel)) {
			showReservationDto
					.setErrorMessage(String.format(StringConstant.SHOW_NOT_FOUND_WITH_ID, showReservationDto.getShowNumber()));
			return false;
		}
		// make sure not null
		Set<String> showModelSeats = Optional.ofNullable(showModel.getRowModelList())
				.map(List::stream).orElseGet(Stream::empty)
				.flatMap(seatsSet -> Optional.ofNullable(seatsSet.getSeatsSet())
						.map(Set::stream).orElseGet(Stream::empty)).collect(Collectors.toSet());
		Set<String> showModelSeatsForReserved = Optional.ofNullable(showReservationDto.getSeatSet())
				.map(Set::stream).orElseGet(Stream::empty)
				.filter(seatsInput -> showModelSeats.stream().noneMatch(seatsInput::equals))
				.collect(Collectors.toSet());
		if (!showModelSeatsForReserved.isEmpty()) {
			showReservationDto.setErrorMessage(String.format(StringConstant.INVALID_SEATS_NOT_FOUND,
					showModelSeatsForReserved.stream().collect(Collectors.joining(StringConstant.COMMA_SPACER)),
					showReservationDto.getShowNumber()));
			return false;
		}
		return true;
	}

	private String checkAvailableSeat(ShowReservationDto showReservationDto) {
		List<ShowReservationModel> showReservationList = showReservationDAO.findByShowId(showReservationDto.getShowNumber());
		if (CollectionUtils.isEmpty(showReservationList)) {
			return StringUtils.EMPTY;
		} else {
			Set<String> seatsSelectedSet = Optional.ofNullable(showReservationDto.getSeatSet())
	                .orElse(new HashSet<>());
			String matchingValues = showReservationList.stream()
		                .filter(srm -> Optional.ofNullable(showReservationDto.getSeatSet())
		        				.map(Set::stream).orElseGet(Stream::empty).anyMatch(seatsSelectedSet::contains))
		                .flatMap(srm -> Optional.ofNullable(showReservationDto.getSeatSet())
		        				.map(Set::stream).orElseGet(Stream::empty))
		                .distinct()
		                .collect(Collectors.joining(StringConstant.COMMA_SPACER));
			return matchingValues;
		}
	}


	private Boolean checkPhoneReservation(ShowReservationDto showReservationDto) {
		List<ShowReservationModel> showReservationList = showReservationDAO
				.findByShowIdPhoneNumber(showReservationDto.getShowNumber(), showReservationDto.getPhoneNumber());
		return !CollectionUtils.isEmpty(showReservationList);
	}

	private ShowReservationModel mapShowReservationModel(ShowReservationDto showReservationDto) {
		ShowReservationModel showReservationModel = new ShowReservationModel();
		showReservationModel.setShowId(showReservationDto.getShowNumber());
		showReservationModel.setDateCreated(new Date());
		showReservationModel.setPhoneNumber(showReservationDto.getPhoneNumber());
		showReservationModel.setSeatsSelected(showReservationDto.getSeatSet());
		return showReservationModel;
	}
	
	private String populateShowReservations(List<ShowReservationModel> showReservationModels) {
		StringBuilder stringBuilder = new StringBuilder();
		for (ShowReservationModel srModel : showReservationModels) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(System.lineSeparator());
			}
			stringBuilder.append(StringConstant.SHOW_NUMBER).append(StringConstant.COLON_SPACER)
					.append(srModel.getShowId()).append(StringConstant.COMMA_SPACER)
					.append(StringConstant.TICKET_NUMBER).append(StringConstant.COLON_SPACER)
					.append(srModel.getTicketNumber()).append(StringConstant.COMMA_SPACER)
					.append(StringConstant.BUYER_PHONE_NUMBER).append(StringConstant.COLON_SPACER)
					.append(srModel.getPhoneNumber()).append(srModel.getPhoneNumber())
					.append(StringConstant.COMMA_SPACER).append(StringConstant.SEAT_NUMBERS)
					.append(StringConstant.COLON_SPACER)
					.append(srModel.getSeatsSelected().stream().collect(Collectors.joining(", ")));
		}
		return stringBuilder.toString();
	}
	
}
