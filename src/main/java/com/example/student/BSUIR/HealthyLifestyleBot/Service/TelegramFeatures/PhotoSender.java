package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class PhotoSender {

    public static void sendPhoto(File file, Message message, TelegramBot telegramBot) throws TelegramApiException {
        InputFile inputFile = new InputFile(file);
        telegramBot.execute(SendPhoto.builder().chatId(message.getChatId().toString()).photo(inputFile).build());
    }

    public static void sendPhoto(File file, Message message, TelegramBot telegramBot, String text) throws TelegramApiException {
        InputFile inputFile = new InputFile(file);
        telegramBot.execute(SendPhoto.builder().chatId(message.getChatId().toString()).caption(text).photo(inputFile).build());
    }
}
