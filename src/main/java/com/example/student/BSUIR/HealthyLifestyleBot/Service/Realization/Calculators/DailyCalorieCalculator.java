package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.Activity;

import java.util.ResourceBundle;

public class DailyCalorieCalculator {
    public static float calculateDailyCalorie(float height, float weight, float age, Activity activity){
        return (float) ((10 * weight + 6.25 * height - 5 * age - 78) * activity.getValue());
    }

    public static String infoBurned(float value, ResourceBundle resourceBundle){
        return resourceBundle.getString("Daily.data") + " - " + value;
    }
}
