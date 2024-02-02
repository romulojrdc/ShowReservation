package com.show.reservation.constant;

public class StringConstant {

	public final static String SHOW_NUMBER = "Show Number";
	public final static String TICKET_NUMBER = "Ticket#";
	public final static String SEAT_NUMBERS = "Seat Numbers";
	public final static String BUYER_PHONE_NUMBER = "Buyer Phone#";
	public final static String COLON_SPACER = ": ";
	public final static String COMMA_SPACER = ", ";
	public static final String PATTERN_COMMA_SPACE = "\\s*,\\s*+";

	public static final String PATTERN_SPACE = "\\s+";
	public static final String PATTERN_NUMERIC = "\\d+";
	
	public static final String ROW_PREFEND = "R";
	public static final String NEW_LINE = "\n";
	
	// Should be moved to message.properties
	public static final String INVALID_INPUT_FORMAT = "Invalid Input format! ";
	public static final String INVALID_SELECTION = "Invalid selection!";
	public static final String SELECTED_BUYER_USER = "Selected Buyer user";
	public static final String SELECTED_ADMIN_USER = "Selected Admin user";
	
	public static final String RESERVATION_CREATED = "Reservation with ticketNumber %d created.";


	public static final String INVALID_AVAILABILITY_COMMAND_INPUT_FORMAT = "Invalid format for Availability command, should be 'Availability [numeric showid]'";
	public static final String INVALID_CANCEL_COMMAND_INPUT_FORMAT = "Invalid format for Cancel command, should be 'Cancel [numeric Ticket#] [numeric Phone#]'";
	public static final String INVALID_BOOK_COMMAND_INPUT_FORMAT = "Invalid format for Book command, should be 'Book [numeric ShowId] [numeric Phone#] [comma separated seat ids] ";
	public static final String INVALID_VIEW_COMMAND_INPUT_FORMAT = "Invalid format for View command, should be 'View [numeric ShowId]";
	public static final String INVALID_SETUP_COMMAND_INPUT_FORMAT = "Invalid format for Setup command, should be 'Setup [numeric ShowId] [numeric # of Rows(max 26)] [numeric # of Seats(max 10)] [numeric expiry in minutes(optional)] ";

	public static final String INVALID_TICKET_NUMBER_NOT_FOUND = "Ticket # %d is invalid. Reason : Cannot be found ";
	public static final String INVALID_PHONE_NOTMATCH_TICKET = "PhoneNumber %s not match to reservation ticket: %d.";
	public static final String INVALID_PHONE_ALREADY_IN_USED = "Cannot make reservation, phone is already in used for this show # %d";
	public static final String INVALID_SEATS_ALREADY_TAKEN = "Cannot make reservation, seats is already taken %s";
	public static final String INVALID_SEATS_NOT_FOUND = "Invalid Seats %s not found in show with id %d";

	public static final String INVALID_ROW_COUNT_RANGE = "Invalid Show Row Count, should be from %d to %d";
	public static final String INVALID_SEATS_COUNT_RANGE  = "Invalid Show Seat Count, should be from %d to %d";
	public static final String INVALID_SHOW_ID_EXISTS = "Cannot create show, ID already exists %d";

	public static final String INVALID_SEATS_REQUIRED = "Required seat number";
	
	public static final String SUCCESS_TICKET_CANCELLATION = "Reservation # %d using PhoneNumber %s is now cancelled";
	public static final String SUCCESS_SHOW_CREATED = "Show records added with ID %d";
	
	public static final String SHOW_NOT_FOUND_WITH_ID = "Show event not found ID: %d";
	public static final String NO_RESERVATION_FOUND_IN_SHOW = "No reservation found for showId %d";
	public static final String REACHED_MAXIMUM_TIME_CANCELLATION = "Cancellation not allowed. Maximum time allowance for cancellation reached";
	
	public static final String SELECTION_USERTYPE_OPTION_MESSAGE = "Select usertype 1 for 'Admin', 2 for Buyer (type 'exit' to quit): ";
	public static final String SELECTION_COMMAND_OPTION_MESSAGE = "Enter a command (type 'exit' to quit, type 0 to select user type): ";
	public static final String EXIT_PROGRAM_MESSAGE = "Exiting the program. Goodbye!";

}
