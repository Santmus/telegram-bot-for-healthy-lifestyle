package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.InlineKeyboard;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.KeyboardMarkUp;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.StartMessage;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.PhotoSender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private ResourceBundle resourceBundle;

    String botUsername;
    String botToken;


    public TelegramBot(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        botToken = resourceBundle.getString("telegram.botToken");
        botUsername = resourceBundle.getString("telegram.userName");
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) { // обработчик событий
        log.info("We get a new Update. This updateID: " + update.getUpdateId());
       try {
           if (update.hasCallbackQuery()) {
               handleCallBack(update.getCallbackQuery());
           }
           if (update.hasMessage()) {
               handleMessage(update.getMessage());
           }
       } catch (TelegramApiRequestException e){
           log.info("User send a lot of message" + update.getMessage());
           execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId())).text(resourceBundle.getString("application.warning")).build());
       }
    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String value = callbackQuery.getData();
        log.info("We get callback:  " + value);
        switch (value) {
            case "ru": {
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
            case "en":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
            case "jp":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
       // для быстрых команд используется англ перевод
        ResourceBundle basicLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));

        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                log.info("We get command:  " + command);
                switch (command) {
                    case "/start": {
                        List<List<InlineKeyboardButton>> languageButton = InlineKeyboard.languageList();
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("application.language")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(languageButton).build()).build());
                        break;
                    }
                    case "/get_all_calculators": {
                         List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(basicLanguage);
                         execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguage.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                         break;
                    }
                    case "/show_information_about_user":{
                        break;
                    }
                    case "/show_sport_nutritions":{
                        break;
                    }
                    case "/show_menu": {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(basicLanguage, basicLanguage.getString("menu.desc"))).build());
                        break;
                    }

                    case "set_information_about_user": {
                        break;
                    }
                }
            }
        }
    }




    
}

