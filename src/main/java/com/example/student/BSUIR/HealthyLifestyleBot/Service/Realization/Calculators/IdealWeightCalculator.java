package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.Activity;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.State;

import java.util.ResourceBundle;

public class IdealWeightCalculator {

    public static float calculateIdealWeight(float height, float age, State state){
        float normalWeigthY = height - 110;
        float normalWeightO = height - 100;

        if (age <= 40 && state.equals(State.WEIGHT_NORMAL)) {
            return  normalWeigthY;
        } else if (age > 40 && state.equals(State.WEIGHT_NORMAL)) {
            return normalWeightO;
        }
        else if (age <= 40 && state.equals(State.WEIGHT_LEAN)) {
            return (float) ((height - 110) - (normalWeigthY * 0.10));
        }
        else if (age > 40 && state.equals(State.WEIGHT_LEAN)) {
            return (float) ((height - 100) - (normalWeightO * 0.10));
        }
        else if (age <= 40 && state.equals(State.WEIGHT_FAT)) {
            return (float) ((height - 110) + (normalWeigthY * 0.10));
        }
        else if (age > 40 && state.equals(State.WEIGHT_FAT)) {
            return (float) ((height - 100) + (normalWeightO * 0.10));
        }
        return height - 100;
    }

    public static String infoBurned(float value, ResourceBundle resourceBundle){
        return resourceBundle.getString("calculator.Ideal.weight") + " " + value + " " + resourceBundle.getString("calculator.Ideal.weight_kilo");
    }

}
