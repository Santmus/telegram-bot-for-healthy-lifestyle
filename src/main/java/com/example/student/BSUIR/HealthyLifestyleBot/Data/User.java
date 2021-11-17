package com.example.student.BSUIR.HealthyLifestyleBot.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data

public class User {
    private String name;
    private String surname;

    private int age;

    private float height;
    private float weight;

    private ArrayList listOfTypeBlood = new ArrayList(List.of("О(I)", "А(II)", "В(III)", "АВ(IV)", "I don`t know"));
    private ArrayList<String> disease = new ArrayList<>();

    public User(String name, String surname, int age, float height, float weight) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }


}
