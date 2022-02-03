package com.example.springtgbot.service;

import com.example.springtgbot.botapi.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService {

    public ReplyKeyboardMarkup getMainMenuKeyBoard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow rowStat = new KeyboardRow();
        KeyboardRow rowBalanceOfDaily = new KeyboardRow();
        KeyboardRow rowBalanceOfAccumulative = new KeyboardRow();

        rowStat.add(new KeyboardButton("Получить статистику по кошелькам"));
        rowBalanceOfDaily.add(new KeyboardButton("Ежедневный кошелек"));
        rowBalanceOfAccumulative.add(new KeyboardButton("Накопительный кошелек"));

        keyboardRowList.add(rowStat);
        keyboardRowList.add(rowBalanceOfDaily);
        keyboardRowList.add(rowBalanceOfAccumulative);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }

    public ReplyKeyboardMarkup getChooseStatisticsTypeKeyBoard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow rowToday = new KeyboardRow();


        rowToday.add(new KeyboardButton("Получить статистику за сегодня"));


        keyboardRowList.add(rowToday);


        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }

    public ReplyKeyboardMarkup getChangeMenuKeyBoard(String keyboardType) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow addIncome = new KeyboardRow();
        KeyboardRow addExpense = new KeyboardRow();
        KeyboardRow backToMenu = new KeyboardRow();
        KeyboardRow setBalance = new KeyboardRow();

        addIncome.add(new KeyboardButton("Добавить доход"));
        addExpense.add(new KeyboardButton("Добавить расход"));
        setBalance.add(new KeyboardButton("Установить баланс кошелька"));
        backToMenu.add(new KeyboardButton("Назад в главное меню"));

        switch (keyboardType) {
            case "default":
                keyboardRowList.add(addIncome);
                keyboardRowList.add(addExpense);
                keyboardRowList.add(setBalance);
                keyboardRowList.add(backToMenu);
                break;
            case "set_balance":
                keyboardRowList.add(setBalance);
                keyboardRowList.add(backToMenu);
                break;
            case "try_to_set_change":
                keyboardRowList.add(addIncome);
                keyboardRowList.add(addExpense);
                keyboardRowList.add(backToMenu);
                break;
            case "set_change":
                keyboardRowList.add(backToMenu);
                break;
        }

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }


    public ReplyKeyboardMarkup getCategoryChooseMenuKeyBoard(BotState botState) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();

        if(botState == BotState.ADD_EXPENSE) {
            row1.add(new KeyboardButton("Продукты"));
            row1.add(new KeyboardButton("Кафе"));
            row1.add(new KeyboardButton("Здоровье"));

            row2.add(new KeyboardButton("Образование"));
            row2.add(new KeyboardButton("Развлечения"));
            row2.add(new KeyboardButton("Связь/интернет"));

            row3.add(new KeyboardButton("Одежда"));
            row3.add(new KeyboardButton("Транспорт"));
            row3.add(new KeyboardButton("Другое"));

        }
        else {
            row1.add(new KeyboardButton("Стипендия"));
            row1.add(new KeyboardButton("Зарплата"));
            row2.add(new KeyboardButton("Деньги родственников"));
            row3.add(new KeyboardButton("Другое"));
        }

        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        keyboardRowList.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }

    public SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.setText(textMessage);

        if(replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        return sendMessage;

    }
}
