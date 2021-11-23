package com.example.student.BSUIR.HealthyLifestyleBot.Data;

import lombok.Data;


import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Data

public class User {
    private String name;
    private String surname;

    private int age;

    private float height;
    private float weight;

    private ArrayList<String> listOfTypeBlood = new ArrayList<>(List.of("О(I)", "А(II)", "В(III)", "АВ(IV)", "I don`t know"));
    private ArrayList<String> disease = new ArrayList<>();

    public User(String name, String surname, int age, float height, float weight) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public User(){}

    public void showAllInformationAboutUser(ResourceBundle resourceBundle){
        System.out.printf("%s: %s\n", resourceBundle.getString("user.name"), getName());
        System.out.printf("%s: %s\n", resourceBundle.getString("user.surname"), getSurname());
        System.out.printf("%s: %d\n", resourceBundle.getString("user.age"), getAge());
        System.out.printf("%s: %f\n", resourceBundle.getString("user.height"), getHeight());
        System.out.printf("%s: %f\n", resourceBundle.getString("user.weight"), getWeight());
    }

}
