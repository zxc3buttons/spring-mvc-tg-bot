package com.example.springtgbot;

import com.example.springtgbot.service.MainMenuService;
import com.example.springtgbot.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
public class UpdateHandler {

    private MainMenuService mainMenuService;
    private StatisticsService statisticsService;

    @Autowired
    public UpdateHandler(MainMenuService mainMenuService, StatisticsService statisticsService) {
        this.mainMenuService = mainMenuService;
        this.statisticsService = statisticsService;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if(message != null && message.hasText()) {
            log.info("New message from User: {}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getChatId();
        SendMessage replyMessage;


        switch (inputMsg) {
            case "/start":
                return mainMenuService.getMainMenuMessage(chatId, inputMsg);
            case "Получить статистику":
                return statisticsService.getStatisticsMessage(chatId);
            default:
                replyMessage = new SendMessage(Long.toString(message.getChatId()), "Hi" + inputMsg);
                return replyMessage;
        }

        /*replyMessage = new SendMessage(Long.toString(message.getChatId()), "Hi" + inputMsg);

        return replyMessage;*/
    }
}
