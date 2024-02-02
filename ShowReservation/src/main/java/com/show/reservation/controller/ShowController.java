package com.show.reservation.controller;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.show.reservation.constant.CommandLineEnum;
import com.show.reservation.constant.StringConstant;
import com.show.reservation.dto.ShowBookingSetupInput;
import com.show.reservation.dto.ShowReservationDto;
import com.show.reservation.service.ShowBookingService;
import com.show.reservation.service.ShowReservationService;

@Component
public class ShowController {

    private static final Logger logger = LogManager.getLogger();
    
	@Autowired
	private ShowBookingService showBookingService;
	
	@Autowired
	private ShowReservationService showReservationService;
	
	public void beginAcceptingInput() {

		Scanner userScanner = new Scanner(System.in);
		selectUserTypeInput(userScanner);
		
	}
	
	private void selectUserTypeInput(Scanner userScanner) {
    	System.out.print(StringConstant.SELECTION_USERTYPE_OPTION_MESSAGE);
    	
    	String userInput = userScanner.nextLine();
    	 if (CommandLineEnum.EXIT.getCommand().equalsIgnoreCase(userInput) 
    			 || CommandLineEnum.QUIT.getCommand().equalsIgnoreCase(userInput)) {
    		 exitProgram(userScanner);
         } else {
        	 String userType = userInput;
        	 switch (userType) {
        	 case "1" :
        		 System.out.println(StringConstant.SELECTED_ADMIN_USER);
        		 processUserInput(userScanner, true);
        		 break;
        	 case "2" :
        		 System.out.println(StringConstant.SELECTED_BUYER_USER);
        		 processUserInput(userScanner, false);
        		 break;
        	 default :
        		System.out.println(StringConstant.INVALID_SELECTION);
        		selectUserTypeInput(userScanner);	 
        	 }
         } 
	}
	
	private void processUserInput(Scanner userScanner, boolean adminUser) {
        System.out.print(StringConstant.SELECTION_COMMAND_OPTION_MESSAGE);
        String userInput = userScanner.nextLine();
        
        String result = null;
        if (CommandLineEnum.EXIT.getCommand().equalsIgnoreCase(userInput) 
   			 || CommandLineEnum.QUIT.getCommand().equalsIgnoreCase(userInput)) {
        } else if (userInput.equalsIgnoreCase("0")) {
        	selectUserTypeInput(userScanner);
		} else {
        	if (adminUser) {
        		result = processAdminUserInput(userInput);
			} else {
				result = processBuyerUserInput(userInput);
			}
        	
        }
        System.out.println(result);
        // Recursive call to ask for input again
        processUserInput(userScanner, adminUser);
    }
	
	public String processBuyerUserInput(String userInput) {
		String result = null;

		ShowReservationDto showReservationDto = mapUserCommandToInput(userInput);
		if (StringUtils.isNotEmpty(showReservationDto.getErrorMessage())) {
			result = showReservationDto.getErrorMessage();
		} else {
			if (CommandLineEnum.CANCEL.getCommand().equals(showReservationDto.getCommand())) {
				result = showReservationService.cancelReservation(showReservationDto.getTicketNumber(),
						showReservationDto.getPhoneNumber());
			} else if (CommandLineEnum.BOOK.getCommand().equals(showReservationDto.getCommand())) {
				Integer ticketNumber = showReservationService.makeReservation(showReservationDto);
				if (ticketNumber < 0) {
					result = showReservationDto.getErrorMessage();
				} else {
					result = String.format(StringConstant.RESERVATION_CREATED, ticketNumber);
				}
			} else if (CommandLineEnum.AVAILABILITY.getCommand().equals(showReservationDto.getCommand())) {
				result = showReservationService.getShowSeatsAvailability(showReservationDto.getShowNumber());
			}
		}
		return result;
	}

	public String processAdminUserInput(String userInput) {
		String result = null;

		ShowBookingSetupInput showBookingSetupInput = convertToShowBookingInput(userInput);
		if (StringUtils.isNotEmpty(showBookingSetupInput.getErrorMessage())) {
			result = showBookingSetupInput.getErrorMessage();
		} else {
			if (CommandLineEnum.SETUP.getCommand().equals(showBookingSetupInput.getCommand())) {
				result = showBookingService.initializeShow(showBookingSetupInput);
			} else {
				result = showReservationService.viewShowReservation(showBookingSetupInput.getShowNumber());
			}
		}
		return result;
	}
	
	private ShowBookingSetupInput convertToShowBookingInput(String commandLine) {
		ShowBookingSetupInput showBookingSetupInput = mapAdminCommandToInput(commandLine);
		return showBookingSetupInput;
	}

	private ShowBookingSetupInput mapAdminCommandToInput(String commandLine) {
		ShowBookingSetupInput showBookingSetupInput = new ShowBookingSetupInput();
		String[] commandParts = commandLine.split(StringConstant.PATTERN_SPACE);

		String validationMessage = validateInputFields(commandLine, true);
		if (StringUtils.isNoneEmpty(validationMessage)) {
			showBookingSetupInput.setErrorMessage(validationMessage);
			return showBookingSetupInput;
		}
		String mainCommand = commandParts[0];

		Integer showId = Integer.valueOf(commandParts[1]);

		showBookingSetupInput.setCommand(mainCommand);
		showBookingSetupInput.setShowNumber(showId);
		if (CommandLineEnum.SETUP.getCommand().equals(mainCommand)) {

			Integer rowCount = Integer.valueOf(commandParts[2]);
			Integer seatCount = Integer.valueOf(commandParts[3]);
			showBookingSetupInput.setRowCount(rowCount);
			showBookingSetupInput.setSeatCount(seatCount);
			if (commandParts.length > 4) {
				Integer bookingExpiry = Integer.valueOf(commandParts[4]);
				showBookingSetupInput.setExpiryTime(bookingExpiry);
			}
		}
		return showBookingSetupInput;
	}
	
	private ShowReservationDto mapUserCommandToInput(String commandLine) {
		ShowReservationDto showReservationDto = new ShowReservationDto();
		try {

			String validationMessage = validateInputFields(commandLine, false);
			if (StringUtils.isNoneEmpty(validationMessage)) {
				
				showReservationDto.setErrorMessage(validationMessage);
				return showReservationDto;
			}
			String[] commandParts = commandLine.split(StringConstant.PATTERN_SPACE);
			String mainCommand = commandParts[0];

			showReservationDto.setCommand(mainCommand);
			if (CommandLineEnum.CANCEL.getCommand().equals(mainCommand)) {

				Integer ticketNumber = Integer.valueOf(commandParts[1]);
				String phoneNumber = String.valueOf(commandParts[2]);

				showReservationDto.setTicketNumber(ticketNumber);
				showReservationDto.setPhoneNumber(phoneNumber);
			} else {
				Integer showId = Integer.valueOf(commandParts[1]);
				showReservationDto.setShowNumber(showId);
				if (CommandLineEnum.BOOK.getCommand().equals(mainCommand)) {
					String phoneNumber = String.valueOf(commandParts[2]);
					String seatsInput = String.valueOf(commandParts[3]);
					showReservationDto.setPhoneNumber(phoneNumber);
					Set<String> seatsSet = Arrays.stream(seatsInput.split(StringConstant.PATTERN_COMMA_SPACE))
							.collect(Collectors.toSet());
					showReservationDto.setSeatSet(seatsSet);
				}
			}

		} catch (Exception e) {
			showReservationDto.setErrorMessage(String.format("Error processing your request: %s", e.getMessage()));
		}

		return showReservationDto;
	}
	
	public void exitProgram(Scanner scanner) {
		System.out.println(StringConstant.EXIT_PROGRAM_MESSAGE);
        // Close the scanner to prevent resource leaks
        scanner.close();
        System.exit(0);
	}
	
	public static boolean isNumeric(String str) {
        return str.matches(StringConstant.PATTERN_NUMERIC);
    }
	
	public static String validateInputFields(String inputCommand, boolean admin) {
		if (StringUtils.isEmpty(inputCommand)) {
			return StringConstant.INVALID_INPUT_FORMAT;
		}
		String[] commandParts = inputCommand.split(StringConstant.PATTERN_SPACE);
		String command = commandParts[0];
		
		CommandLineEnum cmdEnum = EnumUtils.getEnum(CommandLineEnum.class, command.toUpperCase());
		if (admin) {
			if (!(CommandLineEnum.VIEW.getCommand().equals(command)
					|| CommandLineEnum.SETUP.getCommand().equals(command))) {
				return StringConstant.INVALID_INPUT_FORMAT;
			}
			switch (cmdEnum) {
			case SETUP:
				if (commandParts.length < 4 || !isNumeric(commandParts[1]) || !isNumeric(commandParts[2])
						|| !isNumeric(commandParts[3])) {
					return StringConstant.INVALID_SETUP_COMMAND_INPUT_FORMAT;
				}
				if (!(commandParts.length >= 5 && isNumeric(commandParts[4]))) {
					return StringConstant.INVALID_SETUP_COMMAND_INPUT_FORMAT;
				}
				break;
			case VIEW:
				if (commandParts.length < 1 || !isNumeric(commandParts[1])) {
					return StringConstant.INVALID_VIEW_COMMAND_INPUT_FORMAT;
				}
				break;
			}
		} else {
			if (!(CommandLineEnum.AVAILABILITY.getCommand().equals(command)
					|| CommandLineEnum.BOOK.getCommand().equals(command)
					|| CommandLineEnum.CANCEL.getCommand().equals(command))) {
				return StringConstant.INVALID_INPUT_FORMAT;
			}
			switch (cmdEnum) {
			case AVAILABILITY:
				if (commandParts.length < 2 || !isNumeric(commandParts[1])) {
					return StringConstant.INVALID_AVAILABILITY_COMMAND_INPUT_FORMAT;
				}
				break;
			case CANCEL:
				if (commandParts.length < 3 || !isNumeric(commandParts[1]) || !isNumeric(commandParts[2])) {
					return StringConstant.INVALID_CANCEL_COMMAND_INPUT_FORMAT;
				}
				break;
			case BOOK:
				if (commandParts.length < 4 || !isNumeric(commandParts[1]) || !isNumeric(commandParts[2])) {
					return StringConstant.INVALID_BOOK_COMMAND_INPUT_FORMAT;
				}
				break;
			}
		}
		return StringUtils.EMPTY;
	}
	
}
