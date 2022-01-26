package com.example.springtgbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramWebhookBot {

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
        if(update.getMessage() != null && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();

            try{
                execute(new SendMessage(Long.toString(chat_id), "Hi" + update.getMessage().getText()));
            }
            catch (TelegramApiException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
