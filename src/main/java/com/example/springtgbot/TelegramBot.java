package com.example.springtgbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramWebhookBot {


    private UpdateHandler updateHandler;

    @Autowired
    public TelegramBot(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.webhook}")
    private String webhook;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return webhook;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return updateHandler.handleUpdate(update);

        }
        //todo: цепь методов (из webhookcontroller -> tg onwebhookupdaterecieved -> messagehandler(здесь будут все
        //todo: возможные варианты, в них будут записываться данные в бд и там же создаваться клавиатуры
        //todo: https://www.youtube.com/watch?v=sqyvy6kVgwM&list=PLHkGizioHWF2a5mesJC7w3_9EYpaRBJJq&index=3 11 min

}
