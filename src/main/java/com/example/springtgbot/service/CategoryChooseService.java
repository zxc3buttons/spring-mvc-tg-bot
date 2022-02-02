package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.repository.BalanceRepository;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import java.sql.Date;

@Service
public class CategoryChooseService extends AbstractService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public CategoryChooseService(UserRepository userRepository, BalanceRepository balanceRepository) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
    }

    public SendMessage getCategorySettingMessage(final Message message, BotState changeTypeBotState,
                                                 BotState walletTypeBotState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getCategoryChooseMenuKeyBoard(changeTypeBotState);
        String walletType;
        String changeType;

        if(changeTypeBotState == BotState.ADD_EXPENSE)
            changeType = "Расход";
        else
            changeType = "Доход";

        if(walletTypeBotState == BotState.DAILY_WALLET)
            walletType = "Ежедневный";
        else
            walletType = "Накопительный";

        if(changeType.equals("Расход"))
            balanceRepository.AddExpenseToBalance(message.getChatId(), Long.parseLong(message.getText()), walletType);
        else
            balanceRepository.AddIncomeToBalance(message.getChatId(), Long.parseLong(message.getText()), walletType);

        if(!message.getText().equals("0"))
            userRepository.setValuesForLastInsert(Integer.parseInt(message.getText()), message.getChatId(),
                    new Date((long)message.getDate()*1000), changeType);

        String textMessage = "Выберите категорию";
        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }

}
