package com.example.student.BSUIR.HealthyLifestyleBot;

import com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ResourceBundle;

@SpringBootApplication
@Slf4j
public class HealthyLifestyleBotApplication {

	final static int RECONNECT_PAUSE = 10000;

	public static void main(String[] args) {
		System.out.println("Telegram bot is now working");
		SpringApplication.run(HealthyLifestyleBotApplication.class, args);
		registerBot();
	}

	public static void registerBot() {
		TelegramBot telegramBot = new TelegramBot(ResourceBundle.getBundle("application"));
		TelegramBotsApi telegramBotsApi;
		try {
			telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(telegramBot);
			log.info("TelegramAPI started. Look for messages");
		} catch (TelegramApiException e) {
			log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
		}
	}
}
