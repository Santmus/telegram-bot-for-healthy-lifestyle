package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization.Calculators;

import java.util.ResourceBundle;

public class BasicMetabolismCalculator {

    public static float calculateBasicMetabolismMaffin (float height, float weight, float age){
        return (float) ((10 * weight) + (6.25 * height) - (5 * age) + 5);
    }

    public static float calculateBasicMetabolismXarris (float height, float weight, float age){
        return (float) (66.5 + (13.75 * weight) + (5.003 * height) - (6.775 * age));
    }

    public static float calculateBasicMetabolismVenutto (float height, float weight, float age){
        return (float) (66 + (13.7 * weight) + (5 * height) - (6.8 * age));
    }

    public static String infoBurned(float value, float value1, float value2, ResourceBundle resourceBundle){

        return resourceBundle.getString("calculator.Metabolism.desc")+ "\n" + resourceBundle.getString("calculator.Metabolism.alg1") + " " + value + " (218)* " +resourceBundle.getString("calculator.Metabolism.kkal") + "\n"
                + resourceBundle.getString("calculator.Metabolism.alg2") + " " + value1 + " (166)* " + resourceBundle.getString("calculator.Metabolism.kkal") + "\n"
                + resourceBundle.getString("calculator.Metabolism.alg3") + " " + value2 + " (209)* " + resourceBundle.getString("calculator.Metabolism.kkal") + "\n" ;
    }
}
