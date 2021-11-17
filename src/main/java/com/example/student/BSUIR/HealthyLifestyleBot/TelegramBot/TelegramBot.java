package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Service.StartMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private ResourceBundle resourceBundle;
    private StartMessage startMessage = new StartMessage();

    String botUsername;
    String botToken;


    public TelegramBot(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        botToken = resourceBundle.getString("telegram.botToken");
        botUsername = resourceBundle.getString("telegram.userName");
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        log.info("We get a new Update. This updateID: " + update.getUpdateId());
        if (update.hasCallbackQuery()){
            handleCallBack(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }

    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String value = callbackQuery.getData();
        log.info("We get callback:  " + value);
        switch (value) {
            case "ru": {
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessage.greetingMessage(localeLanguage)).build());
                break;
            }
            case "en":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessage.greetingMessage(localeLanguage)).build());
                break;
            }
            case "jp":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessage.greetingMessage(localeLanguage)).build());
                break;
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start":
                        List<List<InlineKeyboardButton>> languageButton = new ArrayList<>();
                        languageButton.add(List.of(InlineKeyboardButton.builder().text("Russian \t\uD83C\uDDF7\uD83C\uDDFA").callbackData("ru").build()));
                        languageButton.add(List.of(InlineKeyboardButton.builder().text("English \t\uD83C\uDDEC\uD83C\uDDE7").callbackData("en").build()));
                        languageButton.add(List.of(InlineKeyboardButton.builder().text("Japanese \t\uD83C\uDDEF\uD83C\uDDF5").callbackData("jp").build()));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("application.language")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(languageButton).build()).build());
                }
            }
        }
    }
}