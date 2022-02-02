package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.repository.BalanceRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletChangeService extends AbstractService {
    private final BalanceRepository balanceRepository;

    public WalletChangeService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public SendMessage getChangeMenuMessage(Message message, BotState botState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup;
        String walletType;
        String textMessage;

        if(botState == BotState.DAILY_WALLET)
            walletType = "Ежедневный";
        else
            walletType = "Накопительный";

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), walletType)){
            replyKeyboardMarkup = getChangeMenuKeyBoard("default");
            textMessage = "Выберите нужное действие";
        }
        else{
            replyKeyboardMarkup = getChangeMenuKeyBoard("set_balance");
            textMessage = "Установите баланс";
        }

        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }

}
