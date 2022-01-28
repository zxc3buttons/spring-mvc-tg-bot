package com.example.springtgbot.controller;

import com.example.springtgbot.TelegramBot;
import com.example.springtgbot.model.User;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final TelegramBot telegramBot;

    public WebhookController(TelegramBot telegramBot, UserRepository userRepository) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){

        User user = new User();

        if(update.getMessage().getText().matches("^\\d+$"))
        {
            user.setChat_id(update.getMessage().getChatId());
            user.setMoney(Integer.parseInt(update.getMessage().getText()));
            userRepository.save(user);
        }

        return telegramBot.onWebhookUpdateReceived(update);
    }

}
