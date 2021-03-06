package com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.Activity;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.SportNutrition;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.State;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;
import com.example.student.BSUIR.HealthyLifestyleBot.Database.Configs.DatabaseHandler;
import com.example.student.BSUIR.HealthyLifestyleBot.Exception.RangeExceededException;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators.*;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.DowloadData;
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
    private SportNutrition sportNutrition;
    private Activity activity;

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
            /* ?????????????????? ???????????? ?????????????????? ???????? */
            case "Show all user information ???" :
            case "???????????????? ?????? ???????????????????? ?? ???????????????????????? ???":
            case "????????????????????????????????????????????? ???": {
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                }
                break;
            }

            case "Show available calculators \uD83D\uDDA9" :
            case "???????????????? ?????????????????? ???????????????????????? \uD83D\uDDA9":
            case "??????????????????????????????????????? \uD83D\uDDA9": {
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

            case "Show all types of sports nutrition \uD83C\uDFD0":
            case "???????????????? ?????? ???????? ?????????????????????? ?????????????? \uD83C\uDFD0":
            case "?????????????????????????????????????????????????????? \uD83C\uDFD0": {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
                break;
            }

            case "???????????????? ???????????????????? ?? ???????????????????????? ???" :
            case "????????????????????????????????? ???" :
            case "Add user information ???": {
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

            case "Change user data \uD83D\uDCC0":
            case "???????????????? ???????????? ?? ???????????????????????? \uD83D\uDCC0":
            case "?????????????????????????????? \uD83D\uDCC0": {
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                } else {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                }
                break;
            }

            case "???????????????? ???????? \uD83D\uDDE3":
            case "Change the language \uD83D\uDDE3":
            case "????????????????????? \uD83D\uDDE3":{
                List<List<InlineKeyboardButton>> languageButton = InlineKeyboard.languageList();
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.changer1")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(languageButton).build()).build());
                break;
            }

            /* ?????????????????? ???????????? ???????????????????????? */
            case "??????":
                case "Name":
                    case "??????" : {
                state = State.SET_USER_NAME;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.name")).build());
                break;
            }
            case "??????????????":
                case "Surname":
                    case "???": {
                state = State.SET_USER_SURNAME;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.surname")).build());
                break;
            }
            case "??????????????":
                case "Age":
                    case "???" :{
                state = State.SET_USER_AGE;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.age")).build());
                break;
            }
            case "????????":
                case "Height":
                    case "??????" :{
                state = State.SET_USER_HEIGHT;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.height")).build());
                break;
            }
            case "??????":
                case "Weight":
                    case "??????": {
                state = State.SET_USER_WEIGHT;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.weight")).build());
                break;
            }
            case "??????????????":
                case "Disease" :
                    case "??????": {
                state = State.SET_USER_DISEASE;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change.diesease")).build());
                break;
            }

            case "???????????????????????? (????????????????????????)":
                case "Asthenic (thin bone)":
                    case "Asthenic???????????????":{
                        state = State.WEIGHT_LEAN;
                        float data = IdealWeightCalculator.calculateIdealWeight(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_age"), state);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(IdealWeightCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        state = State.MENU;
                        break;
            }

            case "???????????????????????????????? (????????????????????)":
                case "Normosthenic (normal)":
                    case "Normosthenic????????????":{
                        state = State.WEIGHT_NORMAL;
                        float data = IdealWeightCalculator.calculateIdealWeight(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_age"), state);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(IdealWeightCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        state = State.MENU;
                        break;
            }

            case "???????????????????????????????? (??????????????????????????)":
                case "Hypersthenic (broad-boned)":
                    case "Hypersthenic????????????": {
                        state = State.WEIGHT_FAT;
                        float data = IdealWeightCalculator.calculateIdealWeight(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_age"), state);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(IdealWeightCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        state = State.MENU;
                        break;
            }

            /* ?????????????????? ???????????????????????? ??????. ???????????????? ?????????? */
            case "?????????????????????? ????????????????????":
                case "Minimum activity":
                    case "??????????????????":{
                        float data = DailyCalorieCalculator.calculateDailyCalorie(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"), Activity.MIN_ACTIVITY);
                        log.info(String.valueOf(data));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(DailyCalorieCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        break;
                    }
            case "???????????? ????????????????????":
                case "Weak activity":
                    case "????????????": {
                        float data = DailyCalorieCalculator.calculateDailyCalorie(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"), Activity.LOW_ACTIVITY);
                        log.info(String.valueOf(data));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(DailyCalorieCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        break;
                    }
            case "?????????????? ????????????????????":
                case "Average activity":
                    case "????????????": {
                        float data = DailyCalorieCalculator.calculateDailyCalorie(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"), Activity.NORMAL_ACTIVITY);
                        log.info(String.valueOf(data));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(DailyCalorieCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        break;
                    }
            case "?????????????? ????????????????????":
                case "High activity":
                    case "?????????": {
                        float data = DailyCalorieCalculator.calculateDailyCalorie(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"), Activity.HIGH_ACTIVITY);
                        log.info(String.valueOf(data));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(DailyCalorieCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        break;
                    }
            case "????????????-????????????????????":
                case "Extra activity":
                    case "????????????": {
                        float data = DailyCalorieCalculator.calculateDailyCalorie(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"), Activity.EXTRA_ACTIVITY);
                        log.info(String.valueOf(data));
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(DailyCalorieCalculator.infoBurned(data, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                        break;
                    }


            /* ?????????????????? ?? ?????????????????? ???????????????????????? */
            default:
                /*?????????? ???????????????????? ?????????????????? ???? ??????????*/
                if (message.getText().equals("1") && (state.equals(State.DOWLOAD_TXT_LIST) || state.equals(State.DOWLOAD_SHOW_LIST))) {
                    DowloadData.findProduct(sportNutrition, 1, state, this, message, nBundle);
                    state = State.MENU;
                }
                if (message.getText().equals("2") && (state.equals(State.DOWLOAD_TXT_LIST) || state.equals(State.DOWLOAD_SHOW_LIST))) {
                    DowloadData.findProduct(sportNutrition, 2, state, this, message, nBundle);
                    state = State.MENU;
                }
                if (message.getText().equals("5") && (state.equals(State.DOWLOAD_TXT_LIST) || state.equals(State.DOWLOAD_SHOW_LIST))) {
                    DowloadData.findProduct(sportNutrition, 5, state, this, message, nBundle);
                    state = State.MENU;
                }
                if (message.getText().equals("10") && (state.equals(State.DOWLOAD_TXT_LIST) || state.equals(State.DOWLOAD_SHOW_LIST))) {
                    DowloadData.findProduct(sportNutrition, 10, state, this, message, nBundle);
                    state = State.MENU;
                }
                if (message.getText().equals("20") && (state.equals(State.DOWLOAD_TXT_LIST) || state.equals(State.DOWLOAD_SHOW_LIST))) {
                    DowloadData.findProduct(sportNutrition, 20, state, this, message, nBundle);
                    state = State.MENU;
                }
                /* ?????????????????? ?????????????????? ???????????????????????? */
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
                /* ???????????????????? ???????????? ???????????????????????? */
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
                /* ???????????????????? ??????. ???????????? ?????? ?????????????? ?????????????????? ?????????????? */
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
        log.info("Bot has state: " + state);
        switch (value) {
            /* ?????????? ?????????? */
            case "ru" -> {
                nBundle = localeLanguageRU;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.create")).build());
                    Thread.sleep(2000);
                }
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\bill.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(nBundle)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                state = State.MENU;
            }
            case "en" -> {
                nBundle = basicLanguageEN;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.create")).build());
                    Thread.sleep(2000);
                }
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\ricardo.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(nBundle)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                state = State.MENU;
            }
            case "jp" -> {
                nBundle = localeLanguageJP;
                if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.create")).build());
                    Thread.sleep(2000);
                }
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\van.png"), message, this);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(StartMessage.greetingMessage(nBundle)).build());
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                state = State.MENU;
            }
            /* ?????????? ?????????????????????? ?????????????? */
            case "sp_amino_acids" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\amino_acid.png"), message, this, nBundle.getString("sport.nutrition.amino_acids"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.AMINO_ACIDS, this, message, nBundle);
                sportNutrition = SportNutrition.AMINO_ACIDS;
                state = State.DOWLOAD_LIST;
            }
            case "sp_anticatabolic" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\anticabolic.png"), message, this, nBundle.getString("sport.nutrition.anticatabolic"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ANTICATABOLIC, this, message, nBundle);
                sportNutrition = SportNutrition.ANTICATABOLIC;
                state = State.DOWLOAD_LIST;
            }
            case "sp_en_drink" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\energy_drink.png"), message, this, nBundle.getString("sport.nutrition.energy.drink"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ENERGY_DRINK, this, message, nBundle);
                sportNutrition = SportNutrition.ENERGY_DRINK;
                state = State.DOWLOAD_LIST;
            }
            case "sp_creatin" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\creatine.png"), message, this, nBundle.getString("sport.nutrition.creatin"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.CREATIN, this, message, nBundle);
                sportNutrition = SportNutrition.CREATIN;
                state = State.DOWLOAD_LIST;
            }
            case "sp_gr_hormone" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\booster.png"), message, this, nBundle.getString("sport.nutrition.growth_hormone"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.GROWTH_HORMONE, this, message, nBundle);
                sportNutrition = SportNutrition.GROWTH_HORMONE;
                state = State.DOWLOAD_LIST;
            }
            case "sp_fat_burners" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\fat_burner.png"), message, this, nBundle.getString("sport.nutrition.fat_burners"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.FAT_BURNERS, this, message, nBundle);
                sportNutrition = SportNutrition.FAT_BURNERS;
                state = State.DOWLOAD_LIST;
            }
            case "sp_collagen" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\collagen.png"), message, this, nBundle.getString("sport.nutrition.collagen"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.COLLAGEN, this, message, nBundle);
                sportNutrition = SportNutrition.COLLAGEN;
                state = State.DOWLOAD_LIST;
            }
            case "sp_glucosamine" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\glucosamine.png"), message, this, nBundle.getString("sport.nutrition.glucosamine"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.GLUCOSAMINE, this, message, nBundle);
                sportNutrition = SportNutrition.GLUCOSAMINE;
                state = State.DOWLOAD_LIST;
            }
            case "sp_isotonic" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\isotonic.png"), message, this, nBundle.getString("sport.nutrition.isotonic"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.ISOTONIC, this, message, nBundle);
                sportNutrition = SportNutrition.ISOTONIC;
                state = State.DOWLOAD_LIST;
            }
            case "sp_vitamine_comp" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\vitamine.png"), message, this, nBundle.getString("sport.nutrition.vitamin_complexes"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.VITAMINE_COMPLEX, this, message, nBundle);
                sportNutrition = SportNutrition.VITAMINE_COMPLEX;
                state = State.DOWLOAD_LIST;
            }
            case "sp_testosterone" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\testosterone.png"), message, this, nBundle.getString("sport.nutrition.testosterone"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.TESTOSTERONE, this, message, nBundle);
                sportNutrition = SportNutrition.TESTOSTERONE;
                state = State.DOWLOAD_LIST;
            }
            case "sp_meal_replace" -> {
                PhotoSender.sendPhoto(new File("src\\main\\java\\pictures\\meal.png"), message, this, nBundle.getString("sport.nutrition.meal_replacemen"));
                HtmlSiteParser.parseSportNutritionInformation(SportNutrition.MEAL_REPLACE, this, message, nBundle);
                sportNutrition = SportNutrition.MEAL_REPLACE;
                state = State.DOWLOAD_LIST;
            }

            /* ?????????????????? ?????????????? ?????????? */
            case "return" -> {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.SHOW_ALL_SPORT_NUTRITION;
            }
            case "return_main_menu" -> {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                state = State.MENU;
            }
            case "return_sp" -> {
                List<List<InlineKeyboardButton>> listOfHandler = InlineKeyboard.functionalOfSportNutritionList(resourceBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("sport.nutrition.menu.1")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfHandler).build()).build());
                state = State.FUNC_ABOUT_SPORT_NUTRITION;
            }

            /* ?????????????????? ???????????????????????? */
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
            case "Daily Calorie Calculator" -> {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("Daily.info")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("Daily.menu"))).build());
            }
            case "dowload_list" -> {
                List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.dowloadSportNutritionList(nBundle);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("dowload.text")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                state = State.DOWLOAD_LIST;
            } // ???????????????? ??????????
            case "txt_file" -> {
                state = State.DOWLOAD_TXT_LIST;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("dowload.number")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("dowload.number"))).build());

            }
            case "message_list" -> {
                state = State.DOWLOAD_SHOW_LIST;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("dowload.number")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("dowload.number"))).build());
            }

            case "Basic Metabolism Calculator" -> {
                float firstAlgorithmMetabolismMaffin = BasicMetabolismCalculator.calculateBasicMetabolismMaffin(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"));
                float secondAlgorithmMetabolismXarris = BasicMetabolismCalculator.calculateBasicMetabolismXarris(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"));
                float thirdAlgorithmMetabolismVenutto = BasicMetabolismCalculator.calculateBasicMetabolismVenutto(databaseHandler.getDataSizePerson(message, "user_height"), databaseHandler.getDataSizePerson(message, "user_weight"), databaseHandler.getDataSizePerson(message, "user_age"));

                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.Metabolism.info")).build());
                Thread.sleep(5000);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.Metabolism.desc")).text(BasicMetabolismCalculator.infoBurned(firstAlgorithmMetabolismMaffin, secondAlgorithmMetabolismXarris, thirdAlgorithmMetabolismVenutto, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
            }

            case "Ideal Weight Calculator" -> {
                state = State.BODY_INFO;
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.Ideal.weight_info")).build());
                Thread.sleep(2000);
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.Ideal.enter_body_type")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("calculator.Ideal.enter_body_type"))).build());

            }
        }
    }

    @SneakyThrows
    private void handleCommand(Message message) {
        if (nBundle == null){
            nBundle = basicLanguageEN;
        }
        Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            log.info("Bot get command:  " + command);
            switch (command) {
                case "/start": {
                    List<List<InlineKeyboardButton>> languageButton = InlineKeyboard.languageList();
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("application.language")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(languageButton).build()).build());
                    break;
                }
                case "/get_all_calculators": {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.calculatorList(nBundle);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("calculator.menu")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
                case "/show_information_about_user": {

                    if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    } else {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(databaseHandler.showUserData(message, nBundle)).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    }
                    break;
                }
                case "/show_sport_nutritions": {
                    List<List<InlineKeyboardButton>> listOfCalculator = InlineKeyboard.sportNutritionList(nBundle);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("sport.nutrition.menu") + ":").replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfCalculator).build()).build());
                    break;
                }
                case "/set_information_about_user": {

                    if (!message.getChatId().equals(databaseHandler.getIdUser(message))){
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("auth.isnot_exist")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("menu.desc"))).build());
                    } else {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(nBundle.getString("user.change")).replyMarkup(KeyboardMarkUp.initButtons(nBundle, nBundle.getString("user.change"))).build());
                        break;
                    }
                }
                case "/add_information_about_user" :{
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

