package com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class KeyboardMarkUp {

    public static ReplyKeyboardMarkup initButtons(ResourceBundle resourceBundle, String nameButton) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        setSettings(replyKeyboardMarkup);

        if (nameButton.equals(resourceBundle.getString("menu.desc"))) {
            keyboardFirstRow.add(resourceBundle.getString("menu.all.info") + "\t\u2139");
            keyboardFirstRow.add(resourceBundle.getString("menu.show.calc") + "\t\uD83D\uDDA9");
            keyboardFirstRow.add(resourceBundle.getString("user.changer") + "\t\uD83D\uDDE3");

            keyboardSecondRow.add(resourceBundle.getString("menu.upd.info") + "\t\uD83D\uDCC0");
            keyboardSecondRow.add(resourceBundle.getString("menu.show.sp") + "\t\uD83C\uDFD0");
            keyboardSecondRow.add(resourceBundle.getString("menu.add.user") + "\t\u2705");

            keyboardRows.add(keyboardFirstRow);
            keyboardRows.add(keyboardSecondRow);

            replyKeyboardMarkup.setKeyboard(keyboardRows);

            return replyKeyboardMarkup;
        }
        else if (nameButton.equals(resourceBundle.getString("user.change"))) {

            keyboardFirstRow.add(resourceBundle.getString("user.name"));
            keyboardFirstRow.add(resourceBundle.getString("user.surname"));
            keyboardFirstRow.add(resourceBundle.getString("user.age"));

            keyboardSecondRow.add(resourceBundle.getString("user.height"));
            keyboardSecondRow.add(resourceBundle.getString("user.weight"));
            keyboardSecondRow.add(resourceBundle.getString("user.disease"));

            keyboardRows.add(keyboardFirstRow);
            keyboardRows.add(keyboardSecondRow);

            replyKeyboardMarkup.setKeyboard(keyboardRows);
            return replyKeyboardMarkup;
        }
        else if (nameButton.equals(resourceBundle.getString("Daily.menu"))) {
            keyboardFirstRow.add(resourceBundle.getString("Daily.btn1"));
            keyboardFirstRow.add(resourceBundle.getString("Daily.btn2"));
            keyboardFirstRow.add(resourceBundle.getString("Daily.btn3"));

            keyboardSecondRow.add(resourceBundle.getString("Daily.btn4"));
            keyboardSecondRow.add(resourceBundle.getString("Daily.btn5"));

            keyboardRows.add(keyboardFirstRow);
            keyboardRows.add(keyboardSecondRow);

            replyKeyboardMarkup.setKeyboard(keyboardRows);
            return replyKeyboardMarkup;
        }
        else if (nameButton.equals(resourceBundle.getString("dowload.number"))){
            keyboardFirstRow.add("1");
            keyboardFirstRow.add("2");
            keyboardFirstRow.add("5");

            keyboardSecondRow.add("10");
            keyboardSecondRow.add("20");

            keyboardRows.add(keyboardFirstRow);
            keyboardRows.add(keyboardSecondRow);

            replyKeyboardMarkup.setKeyboard(keyboardRows);
            return replyKeyboardMarkup;
        } else if (nameButton.equals(resourceBundle.getString("calculator.Ideal.enter_body_type"))) {
            keyboardFirstRow.add(resourceBundle.getString("calculator.Ideal.weight_smart"));
            keyboardFirstRow.add(resourceBundle.getString("calculator.Ideal.weight_normal"));
            keyboardFirstRow.add(resourceBundle.getString("calculator.Ideal.weight_fat"));

            keyboardRows.add(keyboardFirstRow);

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
