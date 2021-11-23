package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class BMICalculator {

    public static float calculateBMI(User user){
        return (float) (user.getWeight() / Math.pow(user.getHeight(),2));
    }

    public static String info(float value, ResourceBundle resourceBundle){
        if (value < 16) return value + resourceBundle.getString("event.koff.1") ;
        else if (value > 16 && value < 18.5) return value + resourceBundle.getString("event.koff.2");
        else if (value > 18.5 && value < 25) return value + resourceBundle.getString("event.koff.3");
        else if (value > 25 && value < 30) return value + resourceBundle.getString("event.koff.4");
        else if (value > 30 && value < 35) return value + resourceBundle.getString("event.koff.5");
        else if (value > 35 && value < 40) return value + resourceBundle.getString("event.koff.6");
        else if (value > 40) return value + resourceBundle.getString("event.koff.7");
    return value + resourceBundle.getString("event.koff.8");
    }
}
