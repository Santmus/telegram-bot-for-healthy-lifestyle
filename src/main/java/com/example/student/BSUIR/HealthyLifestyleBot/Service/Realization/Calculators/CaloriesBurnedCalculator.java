package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators;

import java.util.ResourceBundle;

public class CaloriesBurnedCalculator {

    public static float calculateBurnedCalories(float height, float weight, int meters, int minutes){
        float seconds = minutes * 60;
        float h = height / 100;
        return (float) ((0.035 * weight) + (Math.pow(meters / seconds, 2) / h) * (0.029 * weight));
    }

    public static float averSpeed(int meters, int minutes){
        float seconds = minutes * 60;
        return (float) ((float) (meters / seconds) * 3.6);
    }
    public static String infoBurned(float value, ResourceBundle resourceBundle, int meters, int minutes){
        float avrSpeed = averSpeed(meters, minutes);
        return resourceBundle.getString("Burned.info_1") + " " + value + resourceBundle.getString("Burned.info_2") + "\n"
                + resourceBundle.getString("Burned.info_3") + " " + value * minutes + resourceBundle.getString("Burned.info_4") + "\n"
                + resourceBundle.getString("Burned.info_5") + " " + avrSpeed + resourceBundle.getString("Burned.info_6") + "\n" ;
    }



}
