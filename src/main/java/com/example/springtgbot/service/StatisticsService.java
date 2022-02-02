package com.example.springtgbot.service;

import com.example.springtgbot.Statistics;
import com.example.springtgbot.repository.BalanceRepository;
import com.example.springtgbot.repository.UserRepository;
import com.example.springtgbot.utils.Emojis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.Date;
import java.util.List;

@Service
public class StatisticsService extends AbstractService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public StatisticsService(UserRepository userRepository, BalanceRepository balanceRepository) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
    }

    public SendMessage getStatisticsMessage(Message message) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyBoard();

        StringBuilder textMessage = new StringBuilder("");
        String currentDailyBalance;
        String currentAccumulativeBalance;

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), "Ежедневный"))
            currentDailyBalance = "Текущий баланс ежедневного кошелька: " +
                    balanceRepository.getMoneyAmountByChatIdAndWalletType(message.getChatId(), "Ежедневный")
                    + "\n";
        else
            currentDailyBalance ="Баланс ежедневного кошелька не установлен\n";

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), "Накопительный"))
            currentAccumulativeBalance = "Текущий баланс накопительного кошелька: " +
                    balanceRepository.getMoneyAmountByChatIdAndWalletType
                            (message.getChatId(), "Накопительный") + "\n";
        else
            currentAccumulativeBalance = "Баланс накопительного кошелька не установлен\n";


        textMessage.append(currentDailyBalance).append(currentAccumulativeBalance);

        if(balanceRepository.existsByChatId(message.getChatId())){
            String expensesDaily = "\nВаши расходы за сегодня"
                    + Emojis.EXPENSES + " по всем категориям ежедневного кошелька:\n"
                    + buildMessage(userRepository, "Ежедневный", "Расход", message);
            String incomesDaily = "\nВаши доходы за сегодня"
                    + Emojis.INCOMES + " по всем категориям ежедневного кошелька:\n"
                    + buildMessage(userRepository, "Ежедневный", "Доход", message);
            String expensesAccumulative = "\nВаши расходы за сегодня"
                    + Emojis.EXPENSES + " по всем категориям накопительного кошелька:\n"
                    + buildMessage(userRepository, "Накопительный", "Расход", message);
            String incomesAccumulative =  "\nВаши доходы за сегодня"
                    +  Emojis.INCOMES + " по всем категориям накопительного кошелька:\n"
                    + buildMessage(userRepository, "Накопительный", "Доход", message);

            textMessage.append(expensesDaily).append(incomesDaily).append(expensesAccumulative)
                    .append(incomesAccumulative);
        }
        else{
            textMessage = new StringBuilder("Для получения статистики следует установить баланс кошелька.\n" +
                    "Выберите ежедневный или накопительный кошелёк");
        }
        return createMessageWithKeyboard(message.getChatId(), textMessage.toString(), replyKeyboardMarkup);
    }

    private String buildMessage(UserRepository userRepository,
                                       String walletType, String changeType, Message message){
        StringBuilder stringBuilder = new StringBuilder();

        List<Statistics> statisticsList = userRepository.getChangesByCategory(message.getChatId(),
                new Date((long)message.getDate()*1000), walletType, changeType);
        if(statisticsList.size() == 0) stringBuilder.append("- Пока пусто\n");
        for(Statistics statistics: statisticsList){
            stringBuilder.append("- ")
                    .append(statistics.getCategory())
                    .append(": ")
                    .append(statistics.getMoneyAmount())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
