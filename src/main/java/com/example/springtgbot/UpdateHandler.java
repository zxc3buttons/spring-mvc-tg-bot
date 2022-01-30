package com.example.springtgbot;

import com.example.springtgbot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
public class UpdateHandler {

    private final MainMenuService mainMenuService;
    private final StatisticsService statisticsService;
    private final WalletChangeService walletChangeService;
    private final ChangeTypeService changeTypeService;
    private final CategoryChooseService categoryChooseService;
    private final ResultOfAddingService resultOfAddingService;

    @Autowired
    public UpdateHandler(MainMenuService mainMenuService, StatisticsService statisticsService,
                         WalletChangeService walletChangeService, ChangeTypeService changeTypeService,
                         CategoryChooseService categoryChooseService, ResultOfAddingService resultOfAddingService) {

        this.mainMenuService = mainMenuService;
        this.statisticsService = statisticsService;
        this.walletChangeService = walletChangeService;
        this.changeTypeService = changeTypeService;
        this.categoryChooseService = categoryChooseService;
        this.resultOfAddingService = resultOfAddingService;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if(message != null && message.hasText()) {
            log.info("New message from User: {}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getChatId();
        SendMessage replyMessage;
        BotState botState = setBotCurrentState(inputMsg);

        switch (botState) {
            case START:
                return mainMenuService.getMainMenuMessage(chatId, inputMsg);
            case STATISTICS:
                return statisticsService.getStatisticsMessage(message);
            case DAILY_WALLET:
            case ACCUMULATIVE_WALLET:
                return walletChangeService.getChangeMenuMessage(chatId, inputMsg);
            case ADD_EXPENSE:
            case ADD_INCOME:
                return changeTypeService.getChangedBalanceMessage(chatId, inputMsg);
            case BALANCE_CHANGED:
                return categoryChooseService.getCategorySettingMessage(message);
            case PRODUCTS:
                return resultOfAddingService.getResultMessage(message);
            default:
                replyMessage = new SendMessage(Long.toString(message.getChatId()), "Hi" + inputMsg);
                return replyMessage;
        }

    }

    private BotState setBotCurrentState (String inputMessage) {

        if(inputMessage.matches("^\\d+$"))
            return BotState.BALANCE_CHANGED;

        switch (inputMessage) {
            case "Получить статистику по кошелькам":
                return BotState.STATISTICS;
            case "Ежедневный кошелек":
                return BotState.DAILY_WALLET;
            case "Накопительный кошелек":
                return BotState.ACCUMULATIVE_WALLET;
            case "Добавить расход":
                return BotState.ADD_EXPENSE;
            case "Добавить доход":
                return BotState.ADD_INCOME;
            case "Продукты":
                return BotState.PRODUCTS;
            default:
                return BotState.START;
        }
    }
}
