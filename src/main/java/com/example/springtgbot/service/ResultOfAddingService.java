package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


@Service
public class ResultOfAddingService extends AbstractService {
    private final UserRepository userRepository;

    @Autowired
    public ResultOfAddingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage getResultMessage(final Message message, BotState botState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChangeMenuKeyBoard("try_to_set_change");

        userRepository.setCategoryForLastInsert(message.getText());
        String textMessage;

        if(botState == BotState.EXPENSE_CATEGORY)
            textMessage = "Добавлен расход по категории: " + message.getText();
        else
            textMessage = "Добавлен доход по категории: " + message.getText();

        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }
}
