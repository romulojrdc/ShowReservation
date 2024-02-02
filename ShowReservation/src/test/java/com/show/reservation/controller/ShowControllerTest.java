package com.show.reservation.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.show.reservation.constant.StringConstant;
import com.show.reservation.service.ShowBookingService;
import com.show.reservation.service.ShowReservationService;

@ExtendWith(MockitoExtension.class)
public class ShowControllerTest {
	
	@InjectMocks
	private ShowController showController;
	
	@Mock
	private ShowReservationService showReservationService;
	
	@Mock
	private ShowBookingService showBookingService;
	
	@Test
	public void processBuyerUserInput_Cancel() {
		String commandLineInput = "Cancel 1 098231233232";
		
		String expected = String.format(StringConstant.SUCCESS_TICKET_CANCELLATION, 1, "098231233232");
		when(showReservationService.cancelReservation(any(), any())).thenReturn(expected);
		String result = showController.processBuyerUserInput(commandLineInput);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void processBuyerUserInput_Book() {
		String commandLineInput = "Book 1 093392 R3-4";
		Integer expectedTicketNumber = 1;
		String expected = String.format(StringConstant.RESERVATION_CREATED, expectedTicketNumber);
		when(showReservationService.makeReservation(any())).thenReturn(expectedTicketNumber);
		String result = showController.processBuyerUserInput(commandLineInput);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void processBuyerUserInput_Availabilty() {
		String commandLineInput = "Availability 1";
		String expected = "Show Number: 1";
		when(showReservationService.getShowSeatsAvailability(any())).thenReturn(expected);
		String result = showController.processBuyerUserInput(commandLineInput);
		
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void processAdminUserInput_ViewNoReservation() {
		String commandLineInput = "View 1";
		String expected = String.format(StringConstant.NO_RESERVATION_FOUND_IN_SHOW, 1);
		when(showReservationService.viewShowReservation(any())).thenReturn(expected);
		String result = showController.processAdminUserInput(commandLineInput);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	@Test
	public void processAdminUserInput_Setup() {
		String commandLineInput = "Setup 1 3 4 1";
		String expected = String.format(StringConstant.SUCCESS_SHOW_CREATED, 1);
		when(showBookingService.initializeShow(any())).thenReturn(expected);
		String result = showController.processAdminUserInput(commandLineInput);
		assertNotNull(result);
		assertEquals(expected, result);
	}
	
	// TODO More test
}
