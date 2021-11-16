package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Service.StartMessageAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.ResourceBundle;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TelegramBot extends TelegramLongPollingBot {

    private ResourceBundle resourceBundle;
    private StartMessageAPI startMessageAPI = new StartMessageAPI();

    String botUsername;
    String botToken;


    public TelegramBot(ResourceBundle resourceBundle){
        this.resourceBundle = resourceBundle;
        botToken = resourceBundle.getString("telegram.botToken");
        botUsername = resourceBundle.getString("telegram.userName");
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.hasText()) {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessageAPI.greetingMessage(ResourceBundle.getBundle("application", new Locale("ru", "RU")))).build());
            }
        }
    }

    private void handleMessage(Message message){}
}
