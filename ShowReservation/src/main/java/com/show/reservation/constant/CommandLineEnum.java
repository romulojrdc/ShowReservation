package com.show.reservation.constant;

public enum CommandLineEnum {

	SETUP("Setup"), VIEW("View"), AVAILABILITY("Availability"), BOOK("Book"), CANCEL("Cancel"), QUIT("quit"), EXIT("exit");
	
	private String command;
	
	CommandLineEnum(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
}
