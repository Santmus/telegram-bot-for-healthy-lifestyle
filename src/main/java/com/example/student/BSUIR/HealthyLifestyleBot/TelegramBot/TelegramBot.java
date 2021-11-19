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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
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
                sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessage.greetingMessage(localeLanguage)).build());
                break;
            }
            case "en":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(startMessage.greetingMessage(localeLanguage)).build());
                break;
            }
            case "jp":{
                ResourceBundle localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message);
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
                log.info("We get command:  " + command);
                switch (command) {
                    case "/start":
                        List<List<InlineKeyboardButton>> languageButton = languageList();
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("application.language")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(languageButton).build()).build());
                        break;
                     case "/get_all_calculators":
                     case "/show_all_information_about_user":
                     case "/show_all_information_about_sport_nutritions":
                     case "/show_menu":
                     case "set_information_about_user":
                }
            }
        }
    }

    private void sendPhoto(File file, Message message) throws TelegramApiException {
        InputFile inputFile = new InputFile(file);
        execute(SendPhoto.builder().chatId(message.getChatId().toString()).photo(inputFile).build());
    }

    private List<List<InlineKeyboardButton>> languageList(){
        List<List<InlineKeyboardButton>> languageList = new ArrayList<>();
        languageList.add(List.of(InlineKeyboardButton.builder().text("Russian \t\uD83C\uDDF7\uD83C\uDDFA").callbackData("ru").build()));
        languageList.add(List.of(InlineKeyboardButton.builder().text("English \t\uD83C\uDDEC\uD83C\uDDE7").callbackData("en").build()));
        languageList.add(List.of(InlineKeyboardButton.builder().text("Japanese \t\uD83C\uDDEF\uD83C\uDDF5").callbackData("jp").build()));
        return languageList;
    }

    private static ReplyKeyboardMarkup getMainMenu(String name){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        keyboardFirstRow.add("Вывести информацию о пользователе");
        keyboardFirstRow.add("Показать доступные калькуляторы");

        keyboardSecondRow.add("Изменить информацию о пользователе");
        keyboardSecondRow.add("Показать все типы спортивного питания");


        keyboardRows.add(keyboardFirstRow);
        keyboardRows.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
    
}

