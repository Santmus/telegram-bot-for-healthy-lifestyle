package com.example.student.BSUIR.HealthyLifestyleBot;

import com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ResourceBundle;

@SpringBootApplication
public class HealthyLifestyleBotApplication {

	public static void main(String[] args) {
		System.out.println("Telegram bot is now working");
		SpringApplication.run(HealthyLifestyleBotApplication.class, args);
		registerBot();
	}

	public static void registerBot() {
		TelegramBot telegramBot = new TelegramBot(ResourceBundle.getBundle("application"));
		TelegramBotsApi telegramBotsApi = null;
		try {
			telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		} catch (TelegramApiException e) {
			System.out.println(e.getMessage());
		}
		try {
			assert telegramBotsApi != null;
			telegramBotsApi.registerBot(telegramBot);
		} catch (TelegramApiException e) {
			System.out.println(e.getMessage());
		}
	}
}
