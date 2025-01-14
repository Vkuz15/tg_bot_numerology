package com.telegram.bot.numerology.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class TelegramBotConfiguration {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.chatId}")
    private String chatId;

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }

    public String getChatId() {
        return chatId;
    }
}
