package com.example.springtgbot.service;

import com.example.springtgbot.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MainMenuService extends AbstractService {

    private final BalanceRepository balanceRepository;

    @Autowired
    public MainMenuService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyBoard();
        String finalMessage;
        if(!balanceRepository.existsByChatId(chatId))
            finalMessage = "Продолжая переписку с ботом, вы даете согласие на обработку ваших персональных данных.\n" +
                    "Для начала работы установите баланс одного из кошельков :)";
        else
            finalMessage = textMessage;
        return createMessageWithKeyboard(chatId, finalMessage, replyKeyboardMarkup);
    }

}
