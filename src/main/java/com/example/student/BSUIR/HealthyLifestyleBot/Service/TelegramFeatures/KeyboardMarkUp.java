package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class KeyboardMarkUp {

    public static ReplyKeyboardMarkup initButtons(ResourceBundle resourceBundle, String nameButton){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        setSettings(replyKeyboardMarkup);

        if (nameButton.equals(resourceBundle.getString("menu.desc")))
        {
            keyboardFirstRow.add(resourceBundle.getString("menu.all.info"));
            keyboardFirstRow.add(resourceBundle.getString("menu.show.calc"));

            keyboardSecondRow.add(resourceBundle.getString("menu.upd.info"));
            keyboardSecondRow.add(resourceBundle.getString("menu.show.sp"));

            keyboardRows.add(keyboardFirstRow);
            keyboardRows.add(keyboardSecondRow);

            replyKeyboardMarkup.setKeyboard(keyboardRows);

            return replyKeyboardMarkup;
        }

        return replyKeyboardMarkup;
    }

    public static void setSettings(ReplyKeyboardMarkup replyKeyboardMarkup){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
    }

}
