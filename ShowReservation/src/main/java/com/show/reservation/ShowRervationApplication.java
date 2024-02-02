package com.show.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.show.reservation.controller.ShowController;

@SpringBootApplication
public class ShowRervationApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ShowRervationApplication.class, args);
	}

	@Autowired
	private ShowController showController;
	
	@Override
	public void run(String... args) throws Exception {
		showController.beginAcceptingInput();
	}
}
