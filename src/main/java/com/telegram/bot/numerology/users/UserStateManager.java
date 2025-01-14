package com.telegram.bot.numerology.users;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStateManager {

    private final Map<Long, int[]> userInputMap;
    private final Map<Long, String> userStateMap;

    public UserStateManager() {
        this.userInputMap = new HashMap<>();
        this.userStateMap = new HashMap<>();
    }

    public void setUserInput(long chatId, int[] input) {
        userInputMap.put(chatId, input);
    }

    public int[] getUserInput(long chatId) {
        return userInputMap.get(chatId);
    }

    public void setUserState(long chatId, String state) {
        userStateMap.put(chatId, state);
    }

    public String getUserState(long chatId) {
        return userStateMap.get(chatId);
    }

    public void clearUserState(long chatId) {
        userStateMap.remove(chatId);
        userInputMap.remove(chatId);
    }
}
