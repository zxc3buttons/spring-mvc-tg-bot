package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.model.wallet_changes;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


@Service
public class ChangeTypeService extends AbstractService {
    private final UserRepository userRepository;

    @Autowired
    public ChangeTypeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage getChangedBalanceMessage(final long chatId, String inputMessage, BotState botState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChangeMenuKeyBoard("set_change");

        wallet_changes wallet_changes = new wallet_changes();

        String textMessage;
        if(inputMessage.equals("Добавить расход"))
            textMessage = "Введите сумму расхода";
        else
            textMessage = "Введите сумму дохода";

        if(botState == BotState.DAILY_WALLET)
            wallet_changes.setWalletType("Ежедневный");
        else
            wallet_changes.setWalletType("Накопительный");

        userRepository.save(wallet_changes);

        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

}
