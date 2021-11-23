package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Генерирует одно из сообщений во время запуска бота на разных языках
 */
@Slf4j
public class StartMessage{

    private static final Map<Integer, String> greetMessage = new HashMap<>();

    public static String greetingMessage(ResourceBundle resourceBundle){
        initGreetMessage(resourceBundle);
        int key = (int) Math.floor(Math.random() * greetMessage.size());
        log.info("We get id key: " + key);
        return greetMessage.get(key);
    }

    private static void initGreetMessage(ResourceBundle resourceBundle) {
        greetMessage.put(0, resourceBundle.getString("event.hello.1"));
        greetMessage.put(1, resourceBundle.getString("event.hello.2"));
        greetMessage.put(2, resourceBundle.getString("event.hello.3"));
        greetMessage.put(3, resourceBundle.getString("event.hello.4"));
        greetMessage.put(4, resourceBundle.getString("event.hello.5"));
    }
}
