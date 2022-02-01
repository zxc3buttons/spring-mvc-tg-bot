package com.example.springtgbot;

import java.util.ArrayList;
import java.util.Map;

public class BotStateCache {

    private Map<Long, ArrayList<BotState>> map;

    public BotStateCache(Map<Long, ArrayList<BotState>> map) {
        this.map = map;

    }

    public BotState getPreviousState(Long chatId) {
        if(map.size() == 0){
            map.put(chatId, new ArrayList<>());
            map.get(chatId).add(BotState.START);
            map.get(chatId).add(BotState.START);
        }
        return map.get(chatId).get(0);
    }

    public BotState getLastWalletState(Long chatId) {
        for(int i = map.get(chatId).size() - 1; i > -1; i--)
            if(map.get(chatId).get(i) == BotState.DAILY_WALLET ||
                    map.get(chatId).get(i) == BotState.ACCUMULATIVE_WALLET)
                return map.get(chatId).get(i);
        return null;
    }

    public BotState getLastChangeState(Long chatId) {
        for(int i = map.get(chatId).size() - 1; i > -1; i--)
            if(map.get(chatId).get(i) == BotState.ADD_EXPENSE ||
                    map.get(chatId).get(i) == BotState.ADD_INCOME)
                return map.get(chatId).get(i);
        return null;
    }

    public BotState getCurrentState(Long chatId) {
        if(map.size() == 0 && !map.containsKey(chatId)) {
            map.put(chatId, new ArrayList<>());
            map.get(chatId).add(BotState.START);
        }
        return map.get(chatId).get(map.get(chatId).size() - 1);
    }

    public void setCurrentState(Long chatId, BotState botState) {
        if(map.size() == 0 && !map.containsKey(chatId)){
            map.put(chatId, new ArrayList<>());
            map.get(chatId).add(botState);
            map.get(chatId).add(botState);
        }
        else if(botState != map.get(chatId).get(map.get(chatId).size()-1)){
            map.get(chatId).set(0, map.get(chatId).get(map.get(chatId).size()-1));
            map.get(chatId).set(1, botState);
        }

    }

    public void addCurrentState(Long chatId, BotState botState) {
        if(map.size() == 0 && !map.containsKey(chatId))
            map.put(chatId, new ArrayList<>());
        map.get(chatId).add(botState);
    }

    public void print(Long chatId) {
        if(map.size() == 0)
            System.out.println("Словарь пуст");
        else
            for(BotState botState: map.get(chatId)) System.out.println(botState);
    }


}
