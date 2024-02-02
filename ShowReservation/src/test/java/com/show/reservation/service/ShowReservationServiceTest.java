package com.show.reservation.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.show.reservation.constant.StringConstant;
import com.show.reservation.dao.InMemoryShowReservationDAO;
import com.show.reservation.dto.ShowReservationDto;
import com.show.reservation.model.ShowModel;
import com.show.reservation.model.ShowReservationModel;

@ExtendWith(MockitoExtension.class)
public class ShowReservationServiceTest {

	@InjectMocks
	private ShowReservationService showReservationService;
	
	@Mock
	private ShowBookingService showBookingService;
	
    @InjectMocks
	private ShowModelTestData showModelTestData;
	
	@Mock 
	private InMemoryShowReservationDAO showReservationDAO;
	
	@Test
	public void viewShowReservation() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		List<ShowReservationModel> showReservationModels = showModelTestData
				.createShowReservationDataTest(showModel.getShowId(), 5);
		when(showBookingService.findByShowNumber(anyInt())).thenReturn(showModel);
		when(showReservationDAO.findByShowId(anyInt())).thenReturn(showReservationModels);
		String result = showReservationService.viewShowReservation(anyInt());
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.startsWith(StringConstant.SHOW_NUMBER));
	}
	
	@Test
	public void viewShowReservation_ShowModelNull() {
		when(showBookingService.findByShowNumber(anyInt())).thenReturn(null);
		
		String result = showReservationService.viewShowReservation(anyInt());
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("not found"));

        verify(showBookingService, times(1)).findByShowNumber(anyInt());
        verify(showReservationDAO, never()).findByShowId(anyInt());
	}
	
	@Test
	public void viewShowReservation_ShowReservationNull() {
		
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showBookingService.findByShowNumber(anyInt())).thenReturn(showModel);
		when(showReservationDAO.findByShowId(anyInt())).thenReturn(null);
		String result = showReservationService.viewShowReservation(anyInt());
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("No reservation found"));

        verify(showBookingService, times(1)).findByShowNumber(anyInt());
        verify(showReservationDAO, times(1)).findByShowId(anyInt());
	}
	
	@Test
	public void getShowSeatsAvailability_EmptySeats() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showBookingService.findByShowNumber(showModel.getShowId())).thenReturn(showModel);
		List<ShowReservationModel> showReservationModels = showModelTestData
				.createShowReservationDataTest(showModel.getShowId(), 5);
		when(showReservationDAO.findByShowId(showModel.getShowId())).thenReturn(showReservationModels);
		String result = showReservationService.getShowSeatsAvailability(showModel.getShowId());
		
		// expected Empty
		assertTrue(StringUtils.isEmpty(result));
	}
	
	@Test
	public void getShowSeatsAvailability_WithAvailableSeat() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showBookingService.findByShowNumber(showModel.getShowId())).thenReturn(showModel);
		List<ShowReservationModel> showReservationModels = showModelTestData
				.createShowReservationDataTest(showModel.getShowId(), 2);
		when(showReservationDAO.findByShowId(showModel.getShowId())).thenReturn(showReservationModels);
		String result = showReservationService.getShowSeatsAvailability(showModel.getShowId());
		
		// expected Empty
		assertTrue(StringUtils.isNotEmpty(result));
		assertEquals(3, result.split(StringConstant.PATTERN_COMMA_SPACE).length);
	}
	
	@Test
	public void cancelReservation() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		ShowReservationModel reservationModel = showModelTestData.createShowReservationModel(1, 3);
		when(showReservationDAO.findById(reservationModel.getTicketNumber())).thenReturn(reservationModel);
		when(showBookingService.findByShowNumber(showModel.getShowId())).thenReturn(showModel);
		

		doNothing().when(showReservationDAO).delete(reservationModel.getTicketNumber());
		String result = showReservationService.cancelReservation(reservationModel.getTicketNumber(), reservationModel.getPhoneNumber());
		assertTrue(StringUtils.isNotEmpty(result));
		verify(showReservationDAO, times(1)).findById(reservationModel.getTicketNumber());
		verify(showBookingService, times(1)).findByShowNumber(showModel.getShowId());
		verify(showReservationDAO, times(1)).delete(reservationModel.getTicketNumber());
	}
	
	@Test
	public void cancelReservation_TicketNumberNotFound() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		ShowReservationModel reservationModel = showModelTestData.createShowReservationModel(1, 3);
		
		// not FOUND
		when(showReservationDAO.findById(reservationModel.getTicketNumber())).thenReturn(null);
		
		String result = showReservationService.cancelReservation(reservationModel.getTicketNumber(), reservationModel.getPhoneNumber());
		assertTrue(StringUtils.isNotEmpty(result));
		verify(showReservationDAO, times(1)).findById(reservationModel.getTicketNumber());
		verify(showBookingService, never()).findByShowNumber(showModel.getShowId());
		verify(showReservationDAO, never()).delete(reservationModel.getTicketNumber());
	}
	
	@Test
	public void makeReservation() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		ShowReservationDto reservationDto = createShowReservationDtoTestData(showModel.getShowId());
		ShowReservationModel reservationModel = showModelTestData.createShowReservationModel(1, 3);
		// should not exists
		when(showReservationDAO.findByShowIdPhoneNumber(reservationDto.getShowNumber(), reservationDto.getPhoneNumber())).thenReturn(null);
		when(showBookingService.findByShowNumber(reservationDto.getShowNumber())).thenReturn(showModel);
		when(showReservationDAO.findByShowId(reservationDto.getShowNumber())).thenReturn(null);
		when(showReservationDAO.save(any())).thenReturn(reservationModel.getTicketNumber());

		Integer ticketNumber = showReservationService.makeReservation(reservationDto);
		assertEquals(reservationModel.getTicketNumber(), ticketNumber);
	}
	
	@Test
	public void makeReservation_InvalidInput() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		ShowReservationDto reservationDto = createShowReservationDtoTestData(showModel.getShowId());
		ShowReservationModel reservationModel = showModelTestData.createShowReservationModel(1, 3);

		List<ShowReservationModel> showReservationList = new ArrayList<>(List.of(reservationModel));
		// should not exists
		when(showReservationDAO.findByShowIdPhoneNumber(reservationDto.getShowNumber(), reservationDto.getPhoneNumber())).thenReturn(null);
		when(showBookingService.findByShowNumber(reservationDto.getShowNumber())).thenReturn(showModel);
		when(showReservationDAO.findByShowId(reservationDto.getShowNumber())).thenReturn(showReservationList);

		Integer ticketNumber = showReservationService.makeReservation(reservationDto);
		assertEquals(-1, ticketNumber);
		verify(showReservationDAO, never()).save(any());
	}
	
	public ShowReservationDto createShowReservationDtoTestData(Integer showId) {
		ShowReservationDto showReservationDto = new ShowReservationDto();
		showReservationDto.setPhoneNumber("12346");
		showReservationDto.setTicketNumber(1);
		showReservationDto.setShowNumber(showId);
		showReservationDto.setSeatSet(showModelTestData.createSeatSet(1, "R1"));
		return showReservationDto;
	}
}
