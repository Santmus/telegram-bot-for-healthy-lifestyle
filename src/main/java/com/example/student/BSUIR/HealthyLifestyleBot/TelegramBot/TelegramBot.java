package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.SportNutrition;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import com.example.student.BSUIR.HealthyLifestyleBot.Database.Configs.DatabaseHandler;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators.BMICalculator;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.HtmlSiteParser;
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
import java.sql.SQLException;
import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private ResourceBundle resourceBundle ;
    private ResourceBundle basicLanguageEN = ResourceBundle.getBundle("application", new Locale("en", "EN"));
    private ResourceBundle localeLanguageRU = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
    private ResourceBundle localeLanguageJP = ResourceBundle.getBundle("application", new Locale("jp", "JP"));


    private User user = new User("Yauheni", "Kazachenka", 21, 187f, 84.5f, "COVID-19");
    private DatabaseHandler databaseHandler;

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
               handleMessage(update.getMessage());
           }
          } catch (TelegramApiRequestException e){
           log.info("User send a lot of message" + update.getMessage());
           execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId())).text(resourceBundle.getString("application.warning")).build());
       }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        log.info("We get message: " + message.getText());

        switch (message.getText()){
            case "Show all user information ℹ" : {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(basicLanguageEN)).build());
                break;
            }
            case "Show available calculators \uD83D\uDDA9" : {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(basicLanguageEN);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Show all types of sports nutrition \uD83C\uDFD0": {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(basicLanguageEN);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Change user data \uD83D\uDCC0": {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(basicLanguageEN, basicLanguageEN.getString("user.change"))).build());
                break;
            }
            case "Add user information ✅": {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("auth.name")).build());
                break;
            }

            case "Показать всю информацию о пользователе ℹ":{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(localeLanguageRU)).build());
                break;
            }
            case "Показать доступные калькуляторы \uD83D\uDDA9":{
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(localeLanguageRU);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Изменить данные о пользователе \uD83D\uDCC0":{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageRU, localeLanguageRU.getString("user.change"))).build());
                break;
            }
            case "Показать все типы спортивного питания \uD83C\uDFD0": {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(localeLanguageRU);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "Добавить информацию о пользователе ✅" :{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("auth.name")).build());
                break;
            }

            case "すべてのユーザー情報を表示する ℹ":{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(localeLanguageJP)).build());
                break;
            }
            case "利用可能な計算機を表示する \uD83D\uDDA9":{
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(localeLanguageJP);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "ユーザーデータの変更 \uD83D\uDCC0":{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageJP, localeLanguageJP.getString("user.change"))).build());
                break;
            }
            case "すべての種類のスポーツ栄養を表示する \uD83C\uDFD0": {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(localeLanguageJP);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }
            case "ユーザー情報を追加する ✅" :{
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("auth.name")).build());
                break;
            }
        }
    }


    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException, SQLException {
        Message message = callbackQuery.getMessage();
        String value = callbackQuery.getData();
        log.info("Message is: " + message);
        log.info("We get callback:  " + value);
        switch (value) {
            case "ru": {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguageRU)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageRU, localeLanguageRU.getString("menu.desc"))).build());
                break;
            }
            case "en":{
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(basicLanguageEN)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(basicLanguageEN, basicLanguageEN.getString("menu.desc"))).build());
                break;
            }
            case "jp":{
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguageJP)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageJP, localeLanguageJP.getString("menu.desc"))).build());
                break;
            }

            case "sp_amino_acids":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\amino_acid.png"), message, this, check.getString("sport.nutrition.amino_acids"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.AMINO_ACIDS, this, message, check);
                break;
            }
            case "sp_anticatabolic":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\anticabolic.png"), message, this, check.getString("sport.nutrition.anticatabolic"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ANTICATABOLIC, this, message, check);
                break;
            }
            case "sp_en_drink":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\energy_drink.png"), message, this, check.getString("sport.nutrition.energy.drink"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ENERGY_DRINK, this, message, check);
                break;
            }
            case "sp_creatin":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\creatine.png"), message, this, check.getString("sport.nutrition.creatin "));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.CREATIN, this, message, check);
                break;
            }
            case "sp_gr_hormone":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\booster.png"), message, this, check.getString("sport.nutrition.growth_hormone"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.GROWTH_HORMONE, this, message, check);
                break;
            }
            case "sp_fat_burners":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\fat_burner.png"), message, this, check.getString("sport.nutrition.fat_burners"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.FAT_BURNERS, this, message, check);
                break;
            }
            case "sp_collagen":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\collagen.png"), message, this, check.getString("sport.nutrition.collagen"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.COLLAGEN, this, message, check);
                break;
            }
            case "sp_glucosamine":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\glucosamine.png"), message, this, check.getString("sport.nutrition.glucosamine"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.GLUCOSAMINE, this, message, check);
                break;
            }
            case "sp_isotonic":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\isotonic.png"), message, this, check.getString("sport.nutrition.isotonic"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ISOTONIC, this, message, check);
                break;
            }
            case "sp_vitamine_comp":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\vitamine.png"), message, this, check.getString("sport.nutrition.vitamin_complexes"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.VITAMINE_COMPLEX, this, message, check);
                break;
            }
            case "sp_testosterone":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\testosterone.png"), message, this, check.getString("sport.nutrition.testosterone"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.TESTOSTERONE, this, message, check);
                break;
            }
            case "sp_meal_replace":{
                ResourceBundle check = checkLanguage(message);
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\meal.png"), message, this, check.getString("sport.nutrition.meal_replacemen"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.MEAL_REPLACE, this, message, check);
                break;
            }

            case "return": {
                ResourceBundle check = checkLanguage(message);
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(check);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(check.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                break;
            }

            case "BMI Calculator": {
                ResourceBundle check = checkLanguage(message);
                float data = BMICalculator.calculateBMI(user) * 10000;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(BMICalculator.info(data, check)).build());
                break;
            }
        }
    }

    private void handleCommand(Message message) throws TelegramApiException {

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
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(basicLanguageEN);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
                case "/show_information_about_user": {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(user.showAllInformationAboutUser(basicLanguageEN)).build());
                    break;
                }
                case "/show_sport_nutritions": {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(basicLanguageEN);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
                case "/set_information_about_user": {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(basicLanguageEN, basicLanguageEN.getString("user.change"))).build());
                    break;
                }
            }
        }
    }

    private ResourceBundle checkLanguage(Message message){
        if ("Select the type of sports nutrition you want to know:".equals(message.getText())){
            return basicLanguageEN;
        } else if ("Выберите тип спортивного питания, которое вы хотите узнать:".equals(message.getText())){
            return localeLanguageRU;
        } else if ("知りたいスポーツ栄養の種類を選択してください:".equals(message.getText())){
            return localeLanguageJP;
        }
        return basicLanguageEN;
    }


    
}

