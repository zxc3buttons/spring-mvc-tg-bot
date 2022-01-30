package com.example.springtgbot.service;

import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChangeTypeService {
    private final UserRepository userRepository;

    @Autowired
    public ChangeTypeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage getChangedBalanceMessage(final long chatId, String inputMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChangeMenuKeyBoard();

        String textMessage;
        if(inputMessage.equals("Добавить расход"))
            textMessage = "Введите сумму расхода";
        else
            textMessage = "Введите сумму дохода";

        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup getChangeMenuKeyBoard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow addIncome = new KeyboardRow();
        KeyboardRow addExpense = new KeyboardRow();
        KeyboardRow backToMenu = new KeyboardRow();

        addIncome.add(new KeyboardButton("Добавить доход"));
        addExpense.add(new KeyboardButton("Добавить расход"));
        backToMenu.add(new KeyboardButton("Назад в главное меню"));

        keyboardRowList.add(addIncome);
        keyboardRowList.add(addExpense);
        keyboardRowList.add(backToMenu);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;

    }

    private SendMessage createMessageWithKeyboard(final long chatId,
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
