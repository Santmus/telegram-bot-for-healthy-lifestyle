package com.example.student.BSUIR.HealthyLifestyleBot.Service.Realization;

import com.example.student.BSUIR.HealthyLifestyleBot.Data.SportNutrition;
import com.example.student.BSUIR.HealthyLifestyleBot.Data.State;
import com.example.student.BSUIR.HealthyLifestyleBot.Service.TelegramFeatures.KeyboardMarkUp;
import com.example.student.BSUIR.HealthyLifestyleBot.TelegramBot.TelegramBot;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class DowloadData {

    private static final Map<Integer, String> nameProduct = new HashMap<>();

    public static void findProduct(SportNutrition sportNutrition, int count, State state, TelegramBot telegramBot, Message message, ResourceBundle resourceBundle) throws TelegramApiException {
        log.info(sportNutrition.name());

        switch (sportNutrition) {
            case AMINO_ACIDS -> {
                log.info("User of telegram bot find list amino acids");
                voidInformation("https://sportivnoepitanie.ru/amino-acids/", count, state, telegramBot, message, resourceBundle);
            }
            case ANTICATABOLIC -> {
                log.info("User of telegram bot find list anticatabolic");
                voidInformation("https://sportivnoepitanie.ru/blokiratory-kortizola/", count, state, telegramBot, message, resourceBundle);
            }
            case CREATIN -> {
                log.info("User of telegram bot find list creatine");
                voidInformation("https://sportivnoepitanie.ru/carnitine/", count, state, telegramBot, message, resourceBundle);
            }
            case GROWTH_HORMONE -> {
                log.info("User of telegram bot find list hormone");
                voidInformation("https://sportivnoepitanie.ru/vyrabotka-gormona-rosta/", count, state, telegramBot, message, resourceBundle);
            }
            case ENERGY_DRINK -> {
                log.info("User of telegram bot find list energy drink");
                voidInformation("https://sportivnoepitanie.ru/energetiki/", count, state, telegramBot, message, resourceBundle);
            }
            case FAT_BURNERS -> {
                log.info("User of telegram bot find list fat burners");
                voidInformation("https://sportivnoepitanie.ru/fat-burners/", count, state, telegramBot, message, resourceBundle);
            }
            case COLLAGEN -> {
                log.info("User of telegram bot find list collagen");
                voidInformation("https://sportivnoepitanie.ru/kollagen/", count, state, telegramBot, message, resourceBundle);
            }
            case GLUCOSAMINE -> {
                log.info("User of telegram bot find list glucosamine");
                voidInformation("https://sportivnoepitanie.ru/glukozamin-hondroitin/", count, state, telegramBot, message, resourceBundle);
            }
            case ISOTONIC -> {
                log.info("User of telegram bot find list isotonic");
                voidInformation("https://sportivnoepitanie.ru/izotoniki/", count, state, telegramBot, message, resourceBundle);
            }
            case VITAMINE_COMPLEX -> {
                log.info("User of telegram bot find list vitamin complex");
                voidInformation("https://sportivnoepitanie.ru/vitaminy-i-mineraly/", count, state, telegramBot, message, resourceBundle);
            }
            case TESTOSTERONE -> {
                log.info("User of telegram bot find list testosterone");
                voidInformation("https://sportivnoepitanie.ru/povyshenie-testosterona/", count, state, telegramBot, message, resourceBundle);
            }
            case MEAL_REPLACE -> {
                log.info("User of telegram bot find list meal replace");
                voidInformation("https://sportivnoepitanie.ru/zameniteli-pitaniya/", count, state, telegramBot, message, resourceBundle);
            }
        }
    }

   private static void voidInformation(String url, int count, State state, TelegramBot telegramBot, Message message, ResourceBundle resourceBundle) throws TelegramApiException {
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
           voidInformation(url, count, state, telegramBot, message, resourceBundle);
       }
       assert document != null;
       Elements listInfo = document.select("div.items");
       int i = 0;
       int n = 0;
       for (Element element : listInfo.select("a")) {
           System.out.println(element.text());
           if (i%2 == 1) {
               nameProduct.put(n, element.select("a").text());
               n++;
           }
          i++;
       }
       nameProduct.forEach((k, v) -> System.out.println("Key: " + k + " Value: " + v));
       if (state.equals(State.DOWLOAD_TXT_LIST)){
           initDocument(telegramBot, message, count, resourceBundle);
       }
       else if (state.equals(State.DOWLOAD_SHOW_LIST)) {
           randElement(telegramBot, message, count, resourceBundle);
       }

   }

   private static SendDocument createDocument(Message message, String str, ResourceBundle resourceBundle) throws IOException {
       File file = new File("result.txt");
       SendDocument sendDocument = new SendDocument();
       try (FileWriter fileWriter = new FileWriter(file)) {
           fileWriter.write(str);
           InputFile inputFile = new InputFile(file);
           sendDocument.setChatId(message.getChatId().toString());
           sendDocument.setDocument(inputFile);
           sendDocument.setCaption(resourceBundle.getString("dowload.success"));
           return sendDocument;
       } catch (IOException e){
           log.error("File is doesn`t write");
           FileWriter fileWriter = new FileWriter("result.txt");
           createDocument(message, str, resourceBundle);
       }
       return sendDocument;
   }
    @SneakyThrows
    private static void initDocument(TelegramBot telegramBot, Message message, int count, ResourceBundle resourceBundle) throws TelegramApiException {
        String []nameProducts = listNameProducts(count);

        String str = String.join("\n", nameProducts);
        SendDocument sendDocument = createDocument(message, str, resourceBundle);
        telegramBot.execute(sendDocument);
        telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("menu.desc")).replyMarkup(KeyboardMarkUp.initButtons(resourceBundle, resourceBundle.getString("menu.desc"))).build());

    }

    private static String[] listNameProducts(int count){
        String []nameProducts = new String[count];
        boolean[] isavailable = new boolean[nameProduct.size()];

        for (int n = 0; n < count; n++) {
            int key = (int) Math.floor(Math.random() * nameProduct.size());
            while (isavailable[key]){
                key = (int) Math.floor(Math.random() * nameProduct.size());
            }
            log.info("We get id key: " + key + " . Value: " + nameProduct.get(key));
            nameProducts[n] = nameProduct.get(key);
            isavailable[key] = true;
        }
        return nameProducts;
    }
    private static void randElement(TelegramBot telegramBot, Message message, int count, ResourceBundle resourceBundle) throws TelegramApiException {
        String []nameProducts = listNameProducts(count);

        String str = String.join("\n", nameProducts);
        telegramBot.execute(SendMessage.builder().chatId(message.getChatId().toString()).text(resourceBundle.getString("dowload.success") + "\n" + str).replyMarkup(KeyboardMarkUp.initButtons(resourceBundle, resourceBundle.getString("menu.desc"))).build());
    }
}
