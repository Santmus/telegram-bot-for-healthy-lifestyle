package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InlineKeyboard {

    private static List<List<InlineKeyboardButton>> lists;

    public static List<List<InlineKeyboardButton>> languageList(){
        lists = new ArrayList<>(3);
        lists.add(List.of(InlineKeyboardButton.builder().text("Russian \t\uD83C\uDDF7\uD83C\uDDFA").callbackData("ru").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text("English \t\uD83C\uDDEC\uD83C\uDDE7").callbackData("en").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text("Japanese \t\uD83C\uDDEF\uD83C\uDDF5").callbackData("jp").build()));
        return lists;
    }

    public static List<List<InlineKeyboardButton>> calculatorList(ResourceBundle resourceBundle){
        lists = new ArrayList<>(3);
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.BMI")).callbackData("BMI Calculator").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.Daily.Calories")).callbackData("Daily Calorie Calculator").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("calculator.Burned.Calories")).callbackData("Calories Burned Calculator").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.return")).callbackData("return_main_menu").build()));
        return lists;
    }

    public static List<List<InlineKeyboardButton>> sportNutritionList(ResourceBundle resourceBundle){
        lists = new ArrayList<>(10);
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.amino_acids")).callbackData("sp_amino_acids").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.anticatabolic")).callbackData("sp_anticatabolic").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.energy.drink")).callbackData("sp_en_drink").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.creatin")).callbackData("sp_creatin").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.growth_hormone")).callbackData("sp_gr_hormone").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.meal_replacemen")).callbackData("sp_meal_replace").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.fat_burners")).callbackData("sp_fat_burners").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.collagen")).callbackData("sp_collagen").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.glucosamine")).callbackData("sp_glucosamine").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.isotonic")).callbackData("sp_isotonic").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.vitamin_complexes")).callbackData("sp_vitamine_comp").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.testosterone")).callbackData("sp_testosterone").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.return")).callbackData("return_main_menu").build()));
        return lists;
    }

    public static List<List<InlineKeyboardButton>> functionalOfSportNutritionList(ResourceBundle resourceBundle) {
        lists = new ArrayList<>(3);
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.dowload_list")).callbackData("dowload_list").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.random_product")).callbackData("random_product").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.return")).callbackData("return").build()));
        return lists;
    }

    public static List<List<InlineKeyboardButton>> dowloadSportNutritionList(ResourceBundle resourceBundle) {
        lists = new ArrayList<>(3);
        lists.add(List.of(InlineKeyboardButton.builder().text(".TXT").callbackData("txt_file").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("dowload.message")).callbackData("message_list").build()));
        lists.add(List.of(InlineKeyboardButton.builder().text(resourceBundle.getString("sport.nutrition.return")).callbackData("return_sp").build()));

        return lists;
    }

}
