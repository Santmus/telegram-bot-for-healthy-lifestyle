package com.example.student.BSUIR.HealthyLifestyleBot.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ResourceBundle;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private String surname;

    private int age;

    private float height;
    private float weight;

    private String disease;


    public String showAllInformationAboutUser(ResourceBundle resourceBundle){
        return resourceBundle.getString("user.name") + ":\t" + getName() + "\n" +
                resourceBundle.getString("user.surname")+ ":\t" + getSurname() + "\n" +
                resourceBundle.getString("user.age") + ":\t" + getAge() + "\n" +
                resourceBundle.getString("user.height" ) + ":\t" + getHeight() + "\n" +
                resourceBundle.getString("user.weight") + ":\t" + getWeight() + "\n" +
                resourceBundle.getString("user.disease") + ":\t" + getDisease() + "\n";
    }

}
