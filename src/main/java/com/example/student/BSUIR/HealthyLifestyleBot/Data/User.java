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

    public void setAge(int age) {
        if (age < 100 && age >= 1) this.age = age;
        else throw new ArithmeticException();
    }

    public void setHeight(float height) {
        if (height <= 250.0 && height >= 50) this.height = height;
        else throw new ArithmeticException();
    }

    public void setWeight(float weight) {
        if (weight <= 250.0 && weight >= 3.5) this.weight = weight;
        else throw new ArithmeticException();
    }

}
