package com.example.student.BSUIR.HealthyLifestyleBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthyLifestyleBotApplication {

	public static void main(String[] args) {
		System.out.println("Telegram bot is now working");
		SpringApplication.run(HealthyLifestyleBotApplication.class, args);
	}

}
