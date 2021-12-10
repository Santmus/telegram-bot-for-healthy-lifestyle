package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;

public class CheckLanguage {

    private static final ResourceBundle basicLanguageEN = ResourceBundle.getBundle("application", new Locale("en", "EN"));
    private static final ResourceBundle localeLanguageRU = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
    private static final ResourceBundle localeLanguageJP = ResourceBundle.getBundle("application", new Locale("jp", "JP"));

    public static ResourceBundle checkLanguageSportNutrition(Message message){
        switch (message.getText()) {
            case "Выберите тип спортивного питания, которое вы хотите узнать:" -> {
                return localeLanguageRU;
            }
            case "知りたいスポーツ栄養の種類を選択してください:" -> {
                return localeLanguageJP;
            }
            default -> {
                return basicLanguageEN;
            }
        }
    }

    public static ResourceBundle checkReturnSportNutrition(Message message) {
        switch (message.getText()) {
            case "Что вы хотите сделать?" -> {
                return localeLanguageRU;
            }
            case "何をしたいですか？" -> {
                return localeLanguageJP;
            }
            default -> {
                return basicLanguageEN;
            }
        }
    }

    public static ResourceBundle checkLanguageCalculatorBMI(Message message){
        switch (message.getText()){
            case "使用する計算機を選択します。"->{
                return localeLanguageJP;
            }
            case "Выберите калькулятор, который хотите использовать:"-> {
                return localeLanguageRU;
            }
            default -> {
                return basicLanguageEN;
            }
        }
    }

    public static ResourceBundle checkDataAboutUser(Message message){
        switch (message.getText()){
            case "Имя":
                case "Фамилия":
                    case "Возраст":
                        case "Рост":
                            case "Вес":
                                case "Болезни": {
                                    return localeLanguageRU;
                                }
            case "名前":
                case "姓":
                    case "年":
                        case "身長":
                            case "重量":
                                case "病気":{
                                    return localeLanguageJP;
                                }
            default: return basicLanguageEN;
        }
    }
}
