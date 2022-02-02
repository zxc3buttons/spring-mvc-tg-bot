package com.example.springtgbot;

import com.example.springtgbot.botapi.BotState;
import com.example.springtgbot.botapi.BotStateCache;
import com.example.springtgbot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;


@Component
@Slf4j
public class UpdateHandler {

    private final MainMenuService mainMenuService;
    private final StatisticsService statisticsService;
    private final WalletChangeService walletChangeService;
    private final ChangeTypeService changeTypeService;
    private final CategoryChooseService categoryChooseService;
    private final ResultOfAddingService resultOfAddingService;
    private final SetBalanceService setBalanceService;
    private final BotStateCache botStateCache = new BotStateCache(new HashMap<>());

    @Autowired
    public UpdateHandler(MainMenuService mainMenuService, StatisticsService statisticsService,
                         WalletChangeService walletChangeService, ChangeTypeService changeTypeService,
                         CategoryChooseService categoryChooseService, ResultOfAddingService resultOfAddingService,
                         SetBalanceService setBalanceService) {

        this.mainMenuService = mainMenuService;
        this.statisticsService = statisticsService;
        this.walletChangeService = walletChangeService;
        this.changeTypeService = changeTypeService;
        this.categoryChooseService = categoryChooseService;
        this.resultOfAddingService = resultOfAddingService;
        this.setBalanceService = setBalanceService;

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
        BotState botState = setBotCurrentState(message, botStateCache);

        switch (botState) {
            case STATISTICS:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return statisticsService.getStatisticsMessage(message);
            case DAILY_WALLET:
            case ACCUMULATIVE_WALLET:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return walletChangeService.getChangeMenuMessage(message, botStateCache.getLastWalletState(chatId));
            case TRY_SET_BALANCE:
                return new SendMessage(Long.toString(message.getChatId()), "Введите баланс кошелька");
            case SET_BALANCE:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return setBalanceService.getBalanceInstalledMessage(message, botStateCache.getLastWalletState(chatId));
            case ADD_EXPENSE:
            case ADD_INCOME:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return changeTypeService.getChangedBalanceMessage(chatId, inputMsg,
                        botStateCache.getLastWalletState(chatId));
            case BALANCE_CHANGED:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return categoryChooseService.getCategorySettingMessage(message,
                        botStateCache.getLastChangeTypeState(chatId),
                        botStateCache.getLastWalletState(chatId));
            case EXPENSE_CATEGORY:
            case INCOME_CATEGORY:
                botStateCache.addCurrentState(message.getChatId(), botState);
                return resultOfAddingService.getResultMessage(message, botState);
            default:
                botStateCache.addCurrentState(message.getChatId(), BotState.START);
                return mainMenuService.getMainMenuMessage(chatId, "Все процедуры начинаются с главного " +
                        "меню :)");
        }

    }

    private BotState setBotCurrentState (Message message, BotStateCache botStateCache) {

        String inputMessage = message.getText();
        BotState currentBotState = botStateCache.getCurrentState(message.getChatId());

        if((currentBotState == BotState.ACCUMULATIVE_WALLET || currentBotState == BotState.DAILY_WALLET ||
                currentBotState == BotState.SET_BALANCE)
                && inputMessage.matches("^\\d+$"))
            return BotState.SET_BALANCE;
        else if(inputMessage.matches("^\\d+$"))
            return BotState.BALANCE_CHANGED;

        switch (inputMessage) {
            case "Получить статистику по кошелькам":
                return BotState.STATISTICS;
            case "Ежедневный кошелек":
                return BotState.DAILY_WALLET;
            case "Накопительный кошелек":
                return BotState.ACCUMULATIVE_WALLET;
            case "Добавить расход":
                if(botStateCache.getLastWalletState(message.getChatId()) == null)
                    return BotState.START;
                return BotState.ADD_EXPENSE;
            case "Добавить доход":
                if(botStateCache.getLastWalletState(message.getChatId()) == null)
                    return BotState.START;
                return BotState.ADD_INCOME;
            case "Продукты":
            case "Кафе":
            case "Здоровье":
            case "Образование":
            case "Развлечения":
            case "Связь/интернет":
            case "Одежда":
            case "Транспорт":
            case "Подарки":
                if(botStateCache.getLastChangeTypeState(message.getChatId()) == null)
                    return BotState.START;
                return BotState.EXPENSE_CATEGORY;
            case "Зарплата":
            case "Деньги родственников":
            case "Подарочные":
                if(botStateCache.getLastChangeTypeState(message.getChatId()) == null)
                    return BotState.START;
                return BotState.INCOME_CATEGORY;
            case "Установить баланс кошелька":
                if(botStateCache.getLastWalletState(message.getChatId()) == null)
                    return BotState.START;
                return BotState.TRY_SET_BALANCE;
            case "Введите баланс кошелька":
                return BotState.SET_BALANCE;
            default:
                return BotState.START;
        }
    }
}
