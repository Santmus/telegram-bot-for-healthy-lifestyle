package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.SportNutrition;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.InlineKeyboard;
import com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot.TelegramBot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class HtmlSiteParser {

    public static void parseSportNutritionInformation(SportNutrition sportNutrition, TelegramBot telegramBot, Message message, ResourceBundle localeLanguage) {
        switch (sportNutrition) {
            case AMINO_ACIDS -> {
                log.info("User of telegram bot choose amino acids");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/amino-acids/", telegramBot, message, localeLanguage);
            }
            case ANTICATABOLIC -> {
                log.info("User of telegram bot choose anticatabolic");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/blokiratory-kortizola/", telegramBot, message, localeLanguage);
            }
            case CREATIN -> {
                log.info("User of telegram bot choose creatine");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/carnitine/", telegramBot, message, localeLanguage);
            }
            case GROWTH_HORMONE -> {
                log.info("User of telegram bot choose growth hormone");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/vyrabotka-gormona-rosta/", telegramBot, message, localeLanguage);
            }
            case ENERGY_DRINK -> {
                log.info("User of telegram bot choose energy drink");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/energetiki/", telegramBot, message, localeLanguage);
            }
            case FAT_BURNERS -> {
                log.info("User of telegram bot choose fat burners");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/fat-burners/", telegramBot, message, localeLanguage);
            }
            case COLLAGEN -> {
                log.info("User of telegram bot choose collagen");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/kollagen/", telegramBot, message, localeLanguage);
            }
            case GLUCOSAMINE -> {
                log.info("User of telegram bot choose glucosamine");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/glukozamin-hondroitin/", telegramBot, message, localeLanguage);
            }
            case ISOTONIC -> {
                log.info("User of telegram bot choose isotonic");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/izotoniki/", telegramBot, message, localeLanguage);
            }
            case VITAMINE_COMPLEX -> {
                log.info("User of telegram bot choose vitamin complex");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/vitaminy-i-mineraly/", telegramBot, message, localeLanguage);
            }
            case TESTOSTERONE -> {
                log.info("User of telegram bot choose testosterone");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/povyshenie-testosterona/", telegramBot, message, localeLanguage);
            }
            case MEAL_REPLACE -> {
                log.info("User of telegram bot choose meal replace");
                informationAboutSportNutrition("https://sportivnoepitanie.ru/zameniteli-pitaniya/", telegramBot, message, localeLanguage);
            }
        }
    }

    @SneakyThrows
    private static void informationAboutSportNutrition(String url, TelegramBot telegramBot, Message message, ResourceBundle resourceBundle)  {
        log.info("Data taken from the site: " + url + "\n");
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("An error has occurred. Trying to fix");
            try {
                Thread.currentThread().wait(25000);
            } catch (InterruptedException ex) {
                log.error("There was a problem with the thread, throwing an error");
                System.out.println(ex.getMessage());
            }
            informationAboutSportNutrition(url, telegramBot, message, resourceBundle);
        }
        assert document != null;
        Elements listInfo = document.select("div#main.cat");
        for (Element element : listInfo.select("p")) {

            try {
                if (ResourceBundle.getBundle("application", new Locale("en", "EN")).equals(resourceBundle)) {

                    telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(Translator.translate("ru", "en", element.text())).build());
                    Thread.sleep(2000);
                }
                else if (ResourceBundle.getBundle("application", new Locale("jp", "JP")).equals(resourceBundle)){
                    telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(Translator.translate("ru", "en", element.text())).build());
                    Thread.sleep(2000);
                }
                else if (ResourceBundle.getBundle("application", new Locale("ru", "RU")).equals(resourceBundle)){
                    telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(element.text()).build());
                    Thread.sleep(2000);
                }
            } catch (TelegramApiException e) {
                log.error("Telegram bot can`t send message");
                try {
                    telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Telegram bot can`t send message. Please try again later.").build());
                } catch (TelegramApiException ex) {
                    System.out.println(e.getMessage());
                }
            }
        }
        List<List<InlineKeyboardButton>> listOfHandler = InlineKeyboard.functionalOfSportNutritionList(resourceBundle);
        telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("sport.nutrition.menu.1")).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listOfHandler).build()).build());
    }

}
