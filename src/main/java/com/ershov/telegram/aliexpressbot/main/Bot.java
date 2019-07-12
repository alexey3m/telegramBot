package com.ershov.telegram.aliexpressbot.main;

import com.ershov.telegram.aliexpressbot.http.Client;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Date;

public class Bot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String text = message.getText();
            String[] parts = text.split(" ");
            if (text.contains("/info") || text.contains("/follow") || text.contains("/changeSecretKey") || text.contains("/followTv")) {
                if (parts.length > 1) {
                    execute(message, parts);
                } else {
                    sendResponseToUser(message, "Ошибка! Вы не ввели id товара!");
                }
            } else {
                sendResponseToUser(message, "Не распознана команда!");
            }
        }
    }

    private void execute(Message message, String[] parts) {
        switch (parts[0]) {
            case "/info":
                try {
                    info(message, parts[1]);
                } catch (IOException e) {
                    sendResponseToUser(message, e.getMessage());
                }
                break;
            case "/follow":
                try {
                    follow(message, parts[1]);
                } catch (IOException | InterruptedException e) {
                    sendResponseToUser(message, e.getMessage());
                }
                break;
            case "/followTv":
                try {
                    followTv(message);
                } catch (IOException | InterruptedException e) {
                    sendResponseToUser(message, e.getMessage());
                }
                break;
            case "/changeSecretKey":
                changeSecretKey(message, parts[1]);
                break;
            default:
                sendResponseToUser(message, "хз че случилось(");
                break;
        }
    }

    private void followTv(Message message) throws IOException, InterruptedException {
        Date date = new Date();
        int count = 0;
        sendResponseToUser(message, "Начинаем следить за телеком.");
        Client client = Client.getInstance();
        boolean successFail = true;
        int stock;
        do {
            count++;
            if (count % 100 == 0) {
                sendResponseToUser(message, "Прошло 100 запросов. Всего запросов = " + count);
            }
            stock = client.getStockTv();
            if (stock > 0) {
                successFail = false;
            } else {
                Thread.sleep(120_000);
            }
        } while (successFail);
        String result = "Телек ПОЯВИЛСЯ В НАЛИЧИИ!!! ПОКУПАЙ!!! Сейчас есть " + stock + " штук!";
        for (int i = 0; i < 10; i++) {
            sendResponseToUser(message, result);
            Thread.sleep(3_000);
        }
        String endText = "Поиск телека завершен.\n" +
                "Начат " + date + ".\n" +
                "Количество запросов - " + count + ".";
        sendResponseToUser(message, endText);
    }

    private void changeSecretKey(Message message, String newSecretKey) {
        Client client = Client.getInstance();
        client.setSecretKey(newSecretKey);
        sendResponseToUser(message, "Новый ключ установлен");
    }

    private void follow(Message message, String productKey) throws IOException, InterruptedException {
        Client client = Client.getInstance();
        Date date = new Date();
        JSONObject product = new JSONObject(client.sendPostProductRq(productKey));
        String title = (String) product.get("title");
        int count = 0;
        sendResponseToUser(message, "Начинаем следить за товаром: " + title + ". Запросов = " + count);
        boolean successFail = true;
        int stock;
        do {
            count++;
            if (count % 100 == 0) {
                sendResponseToUser(message, "Прошло еще 100 запросов. Всего запросов = " + count);
            }
            if (count == 800) {
                sendResponseToUser(message, "Количество запросов больше 800! Через 10 часов менять аккаунт!");
            }
            product = new JSONObject(client.sendPostProductRq(productKey));
            JSONObject promotions = product.getJSONArray("promotions").getJSONObject(0);
            stock = (int) promotions.get("stock");
            if (stock > 0) {
                successFail = false;
            } else {
                Thread.sleep(180_000);
            }
        } while (successFail);
        String result = "Товар: " + title + " ПОЯВИЛСЯ В НАЛИЧИИ!!! ПОКУПАЙ!!! Сейчас есть " + stock + " штук!";
        for (int i = 0; i < 10; i++) {
            sendResponseToUser(message, result);
            Thread.sleep(3_000);
        }
        String endText = "Поиск товара: " + title + " завершен.\n" +
                "Начат " + date + ".\n" +
                "Количество запросов - " + count + ".";
        sendResponseToUser(message, endText);
    }

    private void info(Message message, String productKey) throws IOException {
        Client client = Client.getInstance();
        JSONObject product = new JSONObject(client.sendPostProductRq(productKey));
        JSONObject promotions = product.getJSONArray("promotions").getJSONObject(0);
        int stock = (int) promotions.get("stock");
        String title = (String) product.get("title");
        String result = "Количество товара: " + title + " = " + stock;
        sendResponseToUser(message, result);
    }

    private void sendResponseToUser(Message message, String result) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(result);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "AliStockNotificationBot";
    }

    @Override
    public String getBotToken() {
        return "694945341:AAHFDwUxDmIP-gocaxp3QjV7qHcClNh2u_c";
    }
}
