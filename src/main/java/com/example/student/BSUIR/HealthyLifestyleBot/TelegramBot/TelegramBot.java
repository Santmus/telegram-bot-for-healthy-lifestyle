package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.StartMessage;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.InlineKeyboard;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.KeyboardMarkUp;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.PhotoSender;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private ResourceBundle resourceBundle;
    private ResourceBundle localeLanguage;

    private User user = new User();

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
       try {
           if (update.hasCallbackQuery()) {
               handleCallBack(update.getCallbackQuery());
           }
           if (update.hasMessage() && update.getMessage().hasEntities()) {
               handleCommand(update.getMessage());
           }
           if (update.hasMessage()){
               String text = update.getMessage().getText();
               handleMessage(update.getMessage(), text);
           }
          } catch (TelegramApiRequestException e){
           log.info("User send a lot of message" + update.getMessage());
           execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId())).text(resourceBundle.getString("application.warning")).build());
       }
    }

    private void handleMessage(Message message, String text) throws TelegramApiException {
        log.info("We get message: " + message.getText());

        switch (text){
            case "Show all user information ℹ" : {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(localeLanguage)).build());
                break;
            }
            case "Show available calculators \uD83D\uDDA9" : {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Show all types of sports nutrition \uD83C\uDFD0": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Change user data \uD83D\uDCC0": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("user.change"))).build());
                break;
            }
            case "Add user information ✅": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                break;
            }

            case "Показать всю информацию о пользователе ℹ":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(localeLanguage)).build());
                break;
            }
            case "Показать доступные калькуляторы \uD83D\uDDA9":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Изменить данные о пользователе \uD83D\uDCC0":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("user.change"))).build());
                break;
            }
            case "Показать все типы спортивного питания \uD83C\uDFD0": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Добавить информацию о пользователе ✅" :{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                break;
            }

            case "すべてのユーザー情報を表示する ℹ":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(localeLanguage)).build());
                break;
            }
            case "利用可能な計算機を表示する \uD83D\uDDA9":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "ユーザーデータの変更 \uD83D\uDCC0":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("user.change"))).build());
                break;
            }
            case "すべての種類のスポーツ栄養を表示する \uD83C\uDFD0": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(localeLanguage);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "ユーザー情報を追加する ✅" :{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                break;
            }
            default:
                log.info("Unknown message which no handler");
                break;
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String value = callbackQuery.getData();
        log.info("Message is: " + message);
        log.info("We get callback:  " + value);
        switch (value) {
            case "ru": {
                localeLanguage = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
            case "en":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
            case "jp":{
                localeLanguage = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguage)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguage.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguage, localeLanguage.getString("menu.desc"))).build());
                break;
            }
        }
    }

    private void handleCommand(Message message) throws TelegramApiException {

        ResourceBundle basicLanguage = ResourceBundle.getBundle("application", new Locale("en", "EN"));
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
                }    // обработать команды для калькулятора
                case "/show_information_about_user":{
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(basicLanguage)).build());
                        break;
                } // брать данные у пользователя
                case "/show_sport_nutritions": {
                        List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(basicLanguage);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguage.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                        break;
                    } // обработать команды для калькулятора
                }
            }
    }




    
}

