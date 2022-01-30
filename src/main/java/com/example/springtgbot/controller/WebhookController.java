package com.example.springtgbot.controller;

import com.example.springtgbot.TelegramBot;
import com.example.springtgbot.model.tgusers;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Date;

@RestController
public class WebhookController {

    private final TelegramBot telegramBot;

    @Autowired
    public WebhookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){

        return telegramBot.onWebhookUpdateReceived(update);
    }

}
