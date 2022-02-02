package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.model.balance;
import com.example.springtgbot.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class SetBalanceService extends AbstractService {

    private final BalanceRepository balanceRepository;

    @Autowired
    public SetBalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public SendMessage getBalanceInstalledMessage(Message message, BotState previousState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChangeMenuKeyBoard("default");
        final String textMessage;
        final String walletType;

        if(previousState == BotState.DAILY_WALLET){
            walletType = "Ежедневный";
            textMessage = "Баланс ежедневного кошелька: " + message.getText();
        }
        else{
            walletType = "Накопительный";
            textMessage = "Баланс накопительного кошелька: " + message.getText();
        }

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), walletType))
            balanceRepository.setNewBalance(message.getChatId(), Long.parseLong(message.getText()), walletType);
        else{
            balance balance = new balance();
            balance.setChatId(message.getChatId());
            balance.setMoneyAmount(Long.parseLong(message.getText()));
            balance.setWalletType(walletType);
            balanceRepository.save(balance);
        }
        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }

}
