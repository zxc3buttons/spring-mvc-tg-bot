package com.example.springtgbot.service;

import com.example.springtgbot.Statistics;
import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.repository.BalanceRepository;
import com.example.springtgbot.repository.UserRepository;
import com.example.springtgbot.utils.Emojis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.Date;
import java.util.Calendar;
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

    public String getCurrentBalance(BalanceRepository balanceRepository, Message message) {

        String currentBalance;
        String currentDailyBalance;
        String currentAccumulativeBalance;

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), "Ежедневный"))
            currentDailyBalance = Emojis.DAILY_WALLET + " Текущий баланс ежедневного кошелька: " +
                    balanceRepository.getMoneyAmountByChatIdAndWalletType(message.getChatId(), "Ежедневный")
                    + "\n";
        else
            currentDailyBalance = Emojis.DAILY_WALLET + " Баланс ежедневного кошелька не установлен\n";

        if(balanceRepository.existsByChatIdAndWalletType(message.getChatId(), "Накопительный"))
            currentAccumulativeBalance = Emojis.ACCUMULATIVE_WALLET + " Текущий баланс накопительного кошелька: " +
                    balanceRepository.getMoneyAmountByChatIdAndWalletType
                            (message.getChatId(), "Накопительный") + "\n";
        else
            currentAccumulativeBalance = Emojis.ACCUMULATIVE_WALLET + " Баланс накопительного кошелька не установлен\n";

        currentBalance = currentDailyBalance + currentAccumulativeBalance;
        return currentBalance;
    }

    public SendMessage getStatisticsMessage(Message message, BotState botState) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyBoard();

        StringBuilder textMessage = new StringBuilder("");

        textMessage.append(getCurrentBalance(balanceRepository, message));

        if(balanceRepository.existsByChatId(message.getChatId())){
            String expensesDaily = "\nВаши расходы" + Emojis.EXPENSES + " за сегодня" +
                    " по всем категориям ежедневного кошелька: "
                    + buildMessage(userRepository, "Ежедневный", "Расход", message);
            String incomesDaily = "\nВаши доходы" + Emojis.INCOMES + " за сегодня" +
                    " по всем категориям ежедневного кошелька: "
                    + buildMessage(userRepository, "Ежедневный", "Доход", message);
            String expensesAccumulative = "\nВаши расходы" + Emojis.EXPENSES + " за сегодня"  +
                    " по всем категориям накопительного кошелька: "
                    + buildMessage(userRepository, "Накопительный", "Расход", message);
            String incomesAccumulative =  "\nВаши доходы" + Emojis.INCOMES + " за сегодня" +
                    " по всем категориям накопительного кошелька: "
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

        java.util.Date currentDate = Calendar.getInstance().getTime();
        java.util.Date firstDate = currentDate;
        Calendar calendar = Calendar.getInstance();
        String inputMessage = message.getText();

        if(inputMessage.equals("Получить статистику за сегодня"))
        {
            List<Statistics> statisticsList = userRepository.getChangesByCategoryForToday(message.getChatId(),
                    currentDate, walletType, changeType);
            if(statisticsList.size() == 0)
                stringBuilder.append("- Пока пусто\n");
            else
                stringBuilder.
                        append(userRepository.getChanges(message.getChatId(), changeType, walletType,
                                currentDate)).append("\n");
            for(Statistics statistics: statisticsList){
                stringBuilder.append("- ")
                        .append(statistics.getCategory())
                        .append(": ")
                        .append(statistics.getMoneyAmount())
                        .append("\n");
            }
            return stringBuilder.toString();
        }
        else if(inputMessage.equals("Получить статистику за неделю")) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            firstDate = calendar.getTime();
        }
        else if(inputMessage.equals("Получить статистику за месяц")) {
            calendar.add(Calendar.MONTH, -1);
            firstDate = calendar.getTime();
        }

        List<Statistics> statisticsList = userRepository.getChangesByCategoryForDateInterval(message.getChatId(),
                firstDate, currentDate, walletType, changeType);
        if(statisticsList.size() == 0)
            stringBuilder.append("- Пока пусто\n");
        else
            stringBuilder.
                    append(userRepository.getChangesByWeek(message.getChatId(), changeType, walletType,
                            firstDate, currentDate)).append("\n");
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
