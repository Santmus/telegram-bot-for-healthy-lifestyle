package com.example.student.BSUIR.HealthyLifestyleBot.Data;

import lombok.Data;

import java.util.ArrayList;
import java.util.ResourceBundle;

@Data

public class User {
    private String name;
    private String surname;

    private int age;

    private float height;
    private float weight;

    private ArrayList<String> disease;

    public User(String name, String surname, int age, float height, float weight, ArrayList<String> disease) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.disease = disease;
    }

    public String showAllInformationAboutUser(ResourceBundle resourceBundle){
        return resourceBundle.getString("user.name") + ":\t" + getName() + "\n" +
                resourceBundle.getString("user.surname")+ ":\t" + getSurname() + "\n" +
                resourceBundle.getString("user.age") + ":\t" + getAge() + "\n" +
                resourceBundle.getString("user.height" ) + ":\t" + getHeight() + "\n" +
                resourceBundle.getString("user.weight") + ":\t" + getWeight() + "\n" +
                resourceBundle.getString("user.disease") + ":\t" + getDisease() + "\n";
    }

}
