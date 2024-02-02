package com.show.reservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.show.reservation.dao.InMemoryShowDAO;
import com.show.reservation.dto.ShowBookingSetupInput;
import com.show.reservation.model.ShowModel;

@ExtendWith(MockitoExtension.class)
public class ShowBookingServiceTest {
	
	@InjectMocks
	private ShowBookingService showBookingService;
	
	@InjectMocks
	private ShowModelTestData showModelTestData;
	
	@Mock
	private InMemoryShowDAO showDAO;
	
	@Test
	public void findByShowNumber() {
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showDAO.findById(1)).thenReturn(showModel);
		ShowModel modelResult = showBookingService.findByShowNumber(1);
		assertEquals(showModel.getShowId(), modelResult.getShowId());
	}
	
	@Test
	public void initializeShow_ShowExists() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showDAO.findById(showModel.getShowId())).thenReturn(showModel);
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertFalse(result.contains("added"));
		verify(showDAO, never()).save(any());
	}
	
	@Test
	public void initializeShow() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		ShowModel showModel = showModelTestData.createShowModelDataTest();
		when(showDAO.findById(showModel.getShowId())).thenReturn(null);
		when(showDAO.save(any())).thenReturn(showModel.getShowId());
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("added"));
	}

	@Test
	public void initializeShow_RowInvalidZero() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		showInitializerInput.setRowCount(0);
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("Invalid Show Row Count"));
		verify(showDAO, never()).findById(any());
        verify(showDAO, never()).save(any());
	}
	
	@Test
	public void initializeShow_SeatsInvalidZero() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		showInitializerInput.setSeatCount(0);
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("Invalid Show Seat Count"));
		verify(showDAO, never()).findById(any());
        verify(showDAO, never()).save(any());
	}
	@Test
	public void initializeShow_RowInvalidMax() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		showInitializerInput.setRowCount(ShowBookingService.MAX_ROW_COUNT + 1);
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("Invalid Show Row Count"));
		verify(showDAO, never()).findById(any());
        verify(showDAO, never()).save(any());
	}
	
	@Test
	public void initializeShow_SeatsInvalidMax() {
		ShowBookingSetupInput showInitializerInput = createShowBookingSetupInputTestData();
		showInitializerInput.setSeatCount(ShowBookingService.MAX_SEAT_COUNT + 1);
		
		String result = showBookingService.initializeShow(showInitializerInput);
		assertTrue(StringUtils.isNotEmpty(result));
		assertTrue(result.contains("Invalid Show Seat Count"));
		verify(showDAO, never()).findById(any());
        verify(showDAO, never()).save(any());
	}
	
	
	private ShowBookingSetupInput createShowBookingSetupInputTestData() {
		ShowBookingSetupInput setupInput = new ShowBookingSetupInput();
		setupInput.setRowCount(2);
		setupInput.setSeatCount(3);
		setupInput.setShowNumber(1);
		return setupInput;
	}
}
