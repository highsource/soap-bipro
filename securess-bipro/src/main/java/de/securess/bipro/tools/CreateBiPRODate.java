package de.securess.bipro.tools;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.securess.bipro.SecuressBiproApplication;


public  class CreateBiPRODate {
	
	private static final Logger log = LoggerFactory.getLogger(CreateBiPRODate.class);
	
	public static String createBiPRODateFromCurrentLocalTime() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String biPRODateString = date.format(dateFormatter);
		LocalTime time = LocalTime.now();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String biPROTimeString = time.format(timeFormatter);

		log.info(biPRODateString+"T"+biPROTimeString);
		return biPRODateString+"T"+biPROTimeString;
	}
	
	public static String createBiPRODateFromCurrentLocalDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String biPRODateString = date.format(dateFormatter);
		

		log.info(biPRODateString);
		return biPRODateString;
	}

}
