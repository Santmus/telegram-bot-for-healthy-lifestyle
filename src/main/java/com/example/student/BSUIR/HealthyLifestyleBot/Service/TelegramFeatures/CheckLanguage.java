package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;

public class CheckLanguage {

    private static final ResourceBundle basicLanguageEN = ResourceBundle.getBundle("application", new Locale("en", "EN"));
    private static final ResourceBundle localeLanguageRU = ResourceBundle.getBundle("application", new Locale("ru", "RU"));
    private static final ResourceBundle localeLanguageJP = ResourceBundle.getBundle("application", new Locale("jp", "JP"));

    public static ResourceBundle checkLanguageSportNutrition(Message message){
        if ("Select the type of sports nutrition you want to know:".equals(message.getText())){
            return basicLanguageEN;
        } else if ("Выберите тип спортивного питания, которое вы хотите узнать:".equals(message.getText())){
            return localeLanguageRU;
        } else if ("知りたいスポーツ栄養の種類を選択してください:".equals(message.getText())){
            return localeLanguageJP;
        }
        return basicLanguageEN;
    }

    public static ResourceBundle checkReturnSportNutrition(Message message){
        switch (message.getText()){
            case "What do you want to do?": {
                return basicLanguageEN;
            }
            case "Что вы хотите сделать?": {
                return localeLanguageRU;
            }
            case  "何をしたいですか？": {
                return localeLanguageJP;
            }
        }
        return basicLanguageEN;
    }
}
