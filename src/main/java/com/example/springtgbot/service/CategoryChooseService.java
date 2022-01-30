package com.example.springtgbot.service;

import com.example.springtgbot.model.tgusers;
import com.example.springtgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryChooseService {

    private final UserRepository userRepository;

    @Autowired
    public CategoryChooseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage getCategorySettingMessage(final Message message) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getChangeMenuKeyBoard();

        tgusers tgusers = new tgusers();
        tgusers.setChatId(message.getChatId());
        tgusers.setMoney(Integer.parseInt(message.getText()));
        tgusers.setDate(new Date((long)message.getDate()*1000));
        tgusers.setChangeType("Расход");
        userRepository.save(tgusers);

        String textMessage = "Выберите категорию";

        return createMessageWithKeyboard(message.getChatId(), textMessage, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup getChangeMenuKeyBoard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton("Продукты"));
        row1.add(new KeyboardButton("Кафе"));
        row1.add(new KeyboardButton("Здоровье"));

        row2.add(new KeyboardButton("Образование"));
        row2.add(new KeyboardButton("Развлечения"));
        row2.add(new KeyboardButton("Связь/интернет"));

        row3.add(new KeyboardButton("Одежда"));
        row3.add(new KeyboardButton("Транспорт"));
        row3.add(new KeyboardButton("Подарки"));

        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        keyboardRowList.add(row3);

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
