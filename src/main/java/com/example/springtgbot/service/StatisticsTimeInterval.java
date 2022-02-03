package com.example.springtgbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class StatisticsTimeInterval extends AbstractService {

    public SendMessage getChooseStatisticsIntervalMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChooseStatisticsTypeKeyBoard();
        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }
}
