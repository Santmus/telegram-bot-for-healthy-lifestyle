package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InlineKeyboard {

    public static List<List<InlineKeyboardButton>> languageList(){
        List<List<InlineKeyboardButton>> languageList = new ArrayList<>();
        languageList.add(List.of(InlineKeyboardButton.builder().text("Russian \t\uD83C\uDDF7\uD83C\uDDFA").callbackData("ru").build()));
        languageList.add(List.of(InlineKeyboardButton.builder().text("English \t\uD83C\uDDEC\uD83C\uDDE7").callbackData("en").build()));
        languageList.add(List.of(InlineKeyboardButton.builder().text("Japanese \t\uD83C\uDDEF\uD83C\uDDF5").callbackData("jp").build()));
        return languageList;
    }

    public static List<List<InlineKeyboardButton>> calculatorList(ResourceBundle resourceBundle){
        List<List<InlineKeyboardButton>> calculatorList = new ArrayList<>();
        calculatorList.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.BMI")).callbackData("BMI Calculator").build()));
        calculatorList.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.Daily.Calories")).callbackData("Daily Calorie Calculator").build()));
        calculatorList.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.Burned.Calories")).callbackData("Calories Burned Calculator").build()));
        return calculatorList;
    }
}
