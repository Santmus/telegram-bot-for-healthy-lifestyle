package com.example.student.BSUIR.HealthyLifestyleBot.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Генерирует одно из сообщений во время запуска бота на разных языках
 */
public class StartMessage{

    private final Map<Integer, String> greetMessage = new HashMap<>();

    public String greetingMessage(ResourceBundle resourceBundle){
        initGreetMessage(resourceBundle);
        int key = (int) Math.floor(Math.random() * greetMessage.size());
        return greetMessage.get(key);
    }

    private void initGreetMessage(ResourceBundle resourceBundle) {
        greetMessage.put(1, resourceBundle.getString("event.hello.1"));
        greetMessage.put(2, resourceBundle.getString("event.hello.2"));
        greetMessage.put(3, resourceBundle.getString("event.hello.3"));
        greetMessage.put(4, resourceBundle.getString("event.hello.4"));
        greetMessage.put(5, resourceBundle.getString("event.hello.5"));
    }
}
