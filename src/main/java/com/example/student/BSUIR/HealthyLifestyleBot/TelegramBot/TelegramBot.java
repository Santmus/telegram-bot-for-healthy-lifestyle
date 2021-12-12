package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.State;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import com.example.student.BSUIR.HealthyLifestyleBot.Database.Configs.DatabaseHandler;
import com.example.student.BSUIR.HealthyLifestyleBot.Exception.RangeExceededException;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators.BMICalculator;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators.CaloriesBurnedCalculator;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.HtmlSiteParser;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.StartMessage;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.InlineKeyboard;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.KeyboardMarkUp;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.PhotoSender;
import lombok.*;
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

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final ResourceBundle resourceBundle;
    private ResourceBundle nBundle;

    private static final ResourceBundle basicLanguageEN = ResourceBundle.getBundle("application", new Locale("en", "EN"));
    private static final ResourceBundle localeLanguageRU = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
    private static final ResourceBundle localeLanguageJP = ResourceBundle.getBundle("application", new Locale("jp", "JP"));
    
    private State state;

    private DatabaseHandler databaseHandler;

    private String botUsername;
    private String botToken;

    private boolean registrationProcess = false;

    private User newUser;

    private int meters;
    private int minutes;

    public TelegramBot(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        botToken = resourceBundle.getString("telegram.botToken");
        botUsername = resourceBundle.getString("telegram.userName");

        databaseHandler = new DatabaseHandler();
        state = State.NONE_STATE;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        log.info("Bot get a new Update. This updateID: " + update.getUpdateId());
        log.info("Telegram bot has state: " + state.toString());

        if (update.hasCallbackQuery()) {
            handleCallBack(update.getCallbackQuery());
        }
        if (update.hasMessage() && update.getMessage().hasEntities()) {
            handleCommand(update.getMessage());
        }
        if (update.hasMessage()){
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {

        log.info("Bot get message: " + message.getText());
        log.info(String.valueOf(message.getFrom().getId()));

        switch (message.getText()){
            /* Обработка кнопок основного меню */
            case "Show all user information ℹ" : {
                nBundle = basicLanguageEN;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                }
                break;
            }
            case "Show available calculators \uD83D\uDDA9" : {
                nBundle = basicLanguageEN;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                    break;
                } else {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(nBundle);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
            }
            case "Show all types of sports nutrition \uD83C\uDFD0": {
                nBundle = basicLanguageEN;
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
                break;
            }
            case "Change user data \uD83D\uDCC0": {
                nBundle = basicLanguageEN;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                    break;
                }
            }
            case "Add user information ✅": {
                nBundle = basicLanguageEN;
                if (message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exist")).build());
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                } else {
                    registrationProcess = true;
                    newUser = new User();
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.name")).build());
                    state = State.ADD_USER_NAME;
                }
                break;
            }

            case "Показать всю информацию о пользователе ℹ":{
                nBundle = localeLanguageRU;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                }
                break;
            }
            case "Показать доступные калькуляторы \uD83D\uDDA9":{
                nBundle = localeLanguageRU;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                    break;
                } else {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(nBundle);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
            }
            case "Изменить данные о пользователе \uD83D\uDCC0":{
                nBundle = localeLanguageRU;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                }
                break;
            }
            case "Показать все типы спортивного питания \uD83C\uDFD0": {
                nBundle = localeLanguageRU;
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
                break;
            }
            case "Добавить информацию о пользователе ✅" :{
                nBundle = localeLanguageRU;
                if (message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exist")).build());
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                } else {
                    registrationProcess = true;
                    newUser = new User();
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.name")).build());
                    state = State.ADD_USER_NAME;
                }
                break;
            }

            case "すべてのユーザー情報を表示する ℹ":{
                nBundle = localeLanguageJP;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                }
                break;
            }
            case "利用可能な計算機を表示する \uD83D\uDDA9":{
                nBundle = localeLanguageJP;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                    break;
                } else {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(nBundle);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
            }
            case "ユーザーデータの変更 \uD83D\uDCC0":{
                nBundle = localeLanguageJP;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                    break;
                }
            }
            case "すべての種類のスポーツ栄養を表示する \uD83C\uDFD0": {
                nBundle = localeLanguageJP;
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
                break;
            }
            case "ユーザー情報を追加する ✅" :{
                nBundle = localeLanguageJP;
                if (message.getChatId().equals(databaseHandler.getIdUser(message))) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exist")).build());
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    state = State.MENU;
                } else {
                    registrationProcess = true;
                    newUser = new User();
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.name")).build());
                    state = State.ADD_USER_NAME;
                }
                break;
            }

            /* Изменение данных пользователя */
            case "Имя":
                case "Name":
                    case "名前" : {
                state = State.SET_USER_NAME;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.name")).build());
                break;
            }
            case "Фамилия":
                case "Surname":
                    case "姓": {
                state = State.SET_USER_SURNAME;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.surname")).build());
                break;
            }
            case "Возраст":
                case "Age":
                    case "年" :{
                state = State.SET_USER_AGE;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.age")).build());
                break;
            }
            case "Рост":
                case "Height":
                    case "身長" :{
                state = State.SET_USER_HEIGHT;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.height")).build());
                break;
            }
            case "Вес":
                case "Weight":
                    case "重量": {
                state = State.SET_USER_WEIGHT;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.weight")).build());
                break;
            }
            case "Болезни":
                case "Disease" :
                    case "病気": {
                state = State.SET_USER_DISEASE;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.diesease")).build());
                break;
            }

            /* Обработка о сообщении пользователя */
            default:
                /* Изменение параметра пользователя */
                if (state.equals(State.SET_USER_NAME)) {
                    log.info("New user name: " + message.getText());
                    databaseHandler.updateName(message, message.getText(), "user_name");
                    changeMessage(message, nBundle);
                    state = State.MENU;
                }
                else if(state.equals(State.SET_USER_SURNAME)){
                    log.info("New user surname: " + message.getText());
                    databaseHandler.updateName(message, message.getText(), "user_surname");
                    changeMessage(message, nBundle);
                    state = State.MENU;
                }
                else if(state.equals(State.SET_USER_AGE)){
                    log.info("New user age: " + message.getText());
                    try {
                        databaseHandler.updateName(message, message.getText(), "user_age");
                        changeMessage(message, nBundle);
                        state = State.MENU;
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format age");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range age");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_age")).build());
                    }
                }
                else if(state.equals(State.SET_USER_HEIGHT)){
                    log.info("New user height: " + message.getText());
                    try {
                        databaseHandler.updateName(message, message.getText(), "user_height");
                        changeMessage(message, nBundle);
                        state = State.MENU;
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format height");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range height");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_height")).build());
                    }
                }
                else if(state.equals(State.SET_USER_WEIGHT)){
                    log.info("New user weight: " + message.getText());
                    try {
                        databaseHandler.updateName(message, message.getText(), "user_weight");
                        changeMessage(message, nBundle);
                        state = State.MENU;
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format weight");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range weight");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_weight")).build());
                    }
                }
                else if(state.equals(State.SET_USER_DISEASE)){
                    log.info("New user disease: " + message.getText());
                    databaseHandler.updateName(message, message.getText(), "user_list_of_disease");
                    changeMessage(message, nBundle);
                    state = State.MENU;
                }
                /* Добавление нового пользователя */
                else if(state.equals(State.ADD_USER_NAME) && registrationProcess) {
                    newUser.setName(message.getText());
                    state = State.ADD_USER_SURNAME;
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.surname")).build());
                }
                else if(state.equals(State.ADD_USER_SURNAME) && registrationProcess) {
                    newUser.setSurname(message.getText());
                    state = State.ADD_USER_AGE;
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.age")).build());
                }
                else if(state.equals(State.ADD_USER_AGE) && registrationProcess) {
                    try {
                        newUser.setAge(Integer.parseInt(message.getText()));
                        state = State.ADD_USER_HEIGHT;
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.height")).build());
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format age");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range age");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_age")).build());
                    }
                }
                else if(state.equals(State.ADD_USER_HEIGHT) && registrationProcess) {
                    try {
                        newUser.setHeight(Float.parseFloat(message.getText()));
                        state = State.ADD_USER_WEIGHT;
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.weight")).build());
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format height");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range height");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_height")).build());
                    }
                }
                else if(state.equals(State.ADD_USER_WEIGHT) && registrationProcess) {
                    try {
                        newUser.setWeight(Float.parseFloat(message.getText()));
                        state = State.ADD_USER_DISEASE;
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.disease")).build());
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format weight");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    } catch (RangeExceededException rangeExceededException){
                        log.error("Exceeding the data range weight");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.large_small_weight")).build());
                    }
                }
                else if(state.equals(State.ADD_USER_DISEASE) && registrationProcess) {
                    newUser.setDisease(message.getText());
                    databaseHandler.addUserData(newUser, message);
                    state = State.MENU;
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.new_member")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    registrationProcess = false;
                    newUser = null;
                }
                /* Заполнение доп. данных для посчета сожженных калорий */
                else if (state.equals(State.CALORIES_BURNED_CALCULATOR_METERS)){
                    try {
                        meters = Integer.parseInt(message.getText());
                        state = State.CALORIES_BURNED_CALCULATOR_MINUTES;
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("Burned.add_minutes")).build());
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format metrs");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    }
                }
                else if (state.equals(State.CALORIES_BURNED_CALCULATOR_MINUTES)){
                    try {
                        minutes = Integer.parseInt(message.getText());
                        state = State.MENU;
                        float data = CaloriesBurnedCalculator.calculateBurnedCalories(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), meters, minutes);
                        System.out.println(data);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("Burned.info")).build());
                        Thread.sleep(5000);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(CaloriesBurnedCalculator.infoBurned(data, nBundle, meters, minutes)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    } catch (NumberFormatException numberFormatException){
                        log.error("Invalid data format minutes");
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exception")).build());
                    }
                }
        }
    }

    private void changeMessage(Message message, ResourceBundle nBundle) throws TelegramApiException {
        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("data.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
    }

    @SneakyThrows
    private void handleCallBack(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String value = callbackQuery.getData();
        log.info("Message is: " + message);
        log.info("Bot get callback:  " + value);
        switch (value) {
            /* Выбор языка */
            case "ru" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguageRU)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageRU.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageRU, localeLanguageRU.getString("menu.desc"))).build());
                state = State.MENU;
            }
            case "en" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(basicLanguageEN)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(basicLanguageEN, basicLanguageEN.getString("menu.desc"))).build());
                state = State.MENU;
            }
            case "jp" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(localeLanguageJP)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(localeLanguageJP.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(localeLanguageJP, localeLanguageJP.getString("menu.desc"))).build());
                state = State.MENU;
            }
            /* Вывод спортивного питания */
            case "sp_amino_acids" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\amino_acid.png"), message, this, nBundle.getString("sport.nutrition.amino_acids"));
                HtmlSiteParser.parseSportNutritionInformation(State.AMINO_ACIDS, this, message, nBundle);
                state = State.AMINO_ACIDS;
            }
            case "sp_anticatabolic" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\anticabolic.png"), message, this, nBundle.getString("sport.nutrition.anticatabolic"));
                HtmlSiteParser.parseSportNutritionInformation(State.ANTICATABOLIC, this, message, nBundle);
                state = State.ANTICATABOLIC;
            }
            case "sp_en_drink" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\energy_drink.png"), message, this, nBundle.getString("sport.nutrition.energy.drink"));
                HtmlSiteParser.parseSportNutritionInformation(State.ENERGY_DRINK, this, message, nBundle);
                state = State.ENERGY_DRINK;
            }
            case "sp_creatin" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\creatine.png"), message, this, nBundle.getString("sport.nutrition.creatin "));
                HtmlSiteParser.parseSportNutritionInformation(State.CREATIN, this, message, nBundle);
                state = State.CREATIN;
            }
            case "sp_gr_hormone" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\booster.png"), message, this, nBundle.getString("sport.nutrition.growth_hormone"));
                HtmlSiteParser.parseSportNutritionInformation(State.GROWTH_HORMONE, this, message, nBundle);
                state = State.GROWTH_HORMONE;
            }
            case "sp_fat_burners" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\fat_burner.png"), message, this, nBundle.getString("sport.nutrition.fat_burners"));
                HtmlSiteParser.parseSportNutritionInformation(State.FAT_BURNERS, this, message, nBundle);
                state = State.FAT_BURNERS;
            }
            case "sp_collagen" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\collagen.png"), message, this, nBundle.getString("sport.nutrition.collagen"));
                HtmlSiteParser.parseSportNutritionInformation(State.COLLAGEN, this, message, nBundle);
                state = State.COLLAGEN;
            }
            case "sp_glucosamine" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\glucosamine.png"), message, this, nBundle.getString("sport.nutrition.glucosamine"));
                HtmlSiteParser.parseSportNutritionInformation(State.GLUCOSAMINE, this, message, nBundle);
                state = State.GLUCOSAMINE;
            }
            case "sp_isotonic" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\isotonic.png"), message, this, nBundle.getString("sport.nutrition.isotonic"));
                HtmlSiteParser.parseSportNutritionInformation(State.ISOTONIC, this, message, nBundle);
                state = State.ISOTONIC;
            }
            case "sp_vitamine_comp" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\vitamine.png"), message, this, nBundle.getString("sport.nutrition.vitamin_complexes"));
                HtmlSiteParser.parseSportNutritionInformation(State.VITAMINE_COMPLEX, this, message, nBundle);
                state = State.VITAMINE_COMPLEX;
            }
            case "sp_testosterone" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\testosterone.png"), message, this, nBundle.getString("sport.nutrition.testosterone"));
                HtmlSiteParser.parseSportNutritionInformation(State.TESTOSTERONE, this, message, nBundle);
                state = State.TESTOSTERONE;
            }
            case "sp_meal_replace" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\meal.png"), message, this, nBundle.getString("sport.nutrition.meal_replacemen"));
                HtmlSiteParser.parseSportNutritionInformation(State.MEAL_REPLACE, this, message, nBundle);
                state = State.MEAL_REPLACE;
            }

            case "return" -> {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
            }

            case "return_main_menu" -> {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                state = State.MENU;
            }
            /* Обработка калькулятора */
            case "BMI Calculator" -> {
                float data = BMICalculator.calculateBMI(databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_height")) * 10000;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("BMI.info")).build());
                Thread.sleep(5000);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(BMICalculator.info(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
            }
            case "Calories Burned Calculator" -> {
                state = State.CALORIES_BURNED_CALCULATOR_METERS;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("Burned.add_meters")).build());
            }
        }
    }

    @SneakyThrows
    private void handleCommand(Message message) {

        Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            log.info("Bot get command:  " + command);
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
                    nBundle = basicLanguageEN;
                    if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    } else {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    }
                    break;
                }
                case "/show_sport_nutritions": {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(basicLanguageEN);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(basicLanguageEN.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
                case "/set_information_about_user": {
                    nBundle = basicLanguageEN;
                    if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    } else {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                        break;
                    }
                }
                case "/add_information_about_user" :{
                    nBundle = basicLanguageEN;
                    if (message.getChatId().equals(databaseHandler.getIdUser(message))) {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.exist")).build());
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        state = State.MENU;
                    } else {
                        registrationProcess = true;
                        newUser = new User();
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.name")).build());
                        state = State.ADD_USER_NAME;
                    }
                    break;
                }
            }
        }
    }
}

