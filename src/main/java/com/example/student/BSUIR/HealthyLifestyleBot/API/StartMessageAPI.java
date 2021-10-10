package com.example.student.BSUIR.HealthyLifestyleBot.API;

import java.util.HashMap;
import java.util.Map;

/** Генерирует одно из сообщений во время запуска бота
 * */
public class StartMessageAPI {

    private final Map<Integer, String> greetMessage = new HashMap<>();

    public String greetingMessage(){
        initGreetMessage();
        int key = (int) Math.floor(Math.random() * greetMessage.size());
        return greetMessage.get(key);
    }

    private void initGreetMessage() {
        greetMessage.put(1, "Добро пожаловать!!! Я бот, который поможет тебе с организацией Здорового образа жизни!!!");
        greetMessage.put(2, "Привет. Я рад, что ты решил воспользоваться мной. Я бот который всегда готов помочь правильно организовать твой Здоровый образ жизни.");
        greetMessage.put(3, "Приветствую. Я бот, который был создан, чтобы помочь людям правильно организовать свой Здоровый образ жизни!!!");
        greetMessage.put(4, "Доброго тебе дня. Я бот, который предназначен помогать людям правильно организовать свой Здоровый образ жизни и не только.");
        greetMessage.put(5, "Привет. Я бот, предназначенный чтобы помочь людям правильно организовать свой Здоровый образ жизни и не только.");
    }
}
