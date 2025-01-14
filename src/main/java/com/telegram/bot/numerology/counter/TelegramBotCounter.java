package com.telegram.bot.numerology.counter;

import com.telegram.bot.numerology.NumberInfoProvider;
import com.telegram.bot.numerology.components.Buttons;
import com.telegram.bot.numerology.configuration.TelegramBotConfiguration;
import com.telegram.bot.numerology.users.UserStateManager;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.*;

import static com.telegram.bot.numerology.components.BotCommands.*;
import static com.telegram.bot.numerology.components.Buttons.*;
import static com.telegram.bot.numerology.components.Buttons.monthMarkup;

@Slf4j
@Component
public class TelegramBotCounter extends TelegramLongPollingBot {

    private final TelegramBotConfiguration configuration;
    private final UserStateManager userStateManager;
    private Map<Long, Integer> userYearMap = new HashMap<>();
    private NumberInfoProvider numberInfoProvider = new NumberInfoProvider();
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotCounter.class);

    @Autowired
    public TelegramBotCounter(TelegramBotConfiguration configuration, UserStateManager userStateManager) {
        this.configuration = configuration;
        this.userStateManager = userStateManager;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return configuration.getBotName();
    }

    @Override
    public String getBotToken() {
        return configuration.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);

            //Обработка нажатий на кнопки
            //Обработка кнопок денежной девятки
            switch (receivedMessage) {
                case "/2025":
                case "/2026":
                case "/2027":
                    sendMonthButtonsNine(chatId, receivedMessage);
                    break;
                case "/january_nine":
                case "/february_nine":
                case "/march_nine":
                case "/april_nine":
                case "/may_nine":
                case "/june_nine":
                case "/july_nine":
                case "/august_nine":
                case "/september_nine":
                case "/october_nine":
                case "/november_nine":
                case "/december_nine":
                    Integer year = userYearMap.get(chatId); // Получаем год из userYearMap
                    if (year == null) {
                        sendMessage(chatId, "Пожалуйста, сначала выберите год.", backYear(), ""); // Сообщение пользователю
                        return; // Выход из метода
                    }
                    int month = getMonthNumberNine(receivedMessage);
                    calculateNineDaysForMonth(chatId, year, month);
                    break;

                //Обработка кнопок девятилетнего цикла
                case "/nine_cycle_year":
                    sendNineYearCycleButtons(chatId, receivedMessage);
                    break;
                case "/calculate_cycle_year":
                    calculateNineCycleYear(chatId, receivedMessage);
                    break;

                //Обработка кнопок числа судьбы (профессия)
                case "/calculate_profession_answer":
                    sendProfessionNumberOfDestiny(chatId, receivedMessage);
                    break;
                case "/calculate_profession":
                    calculateProfessionNumberOfDestiny(chatId, receivedMessage);
                    break;

                //Обработка кнопок "Как найти свою любовь?"
                case "/love_month_answer":
                    sendLoveOnMonthsButtons(chatId, receivedMessage);
                    break;
                case "/back_to_months_love":
                    sendMonthButtonsLove(chatId, receivedMessage);
                    break;
                case "/january_love":
                case "/february_love":
                case "/march_love":
                case "/april_love":
                case "/may_love":
                case "/june_love":
                case "/july_love":
                case "/august_love":
                case "/september_love":
                case "/october_love":
                case "/november_love":
                case "/december_love":
                    int monthLove = getMonthNumberLove(receivedMessage);
                    calculateLoveMonth(chatId, monthLove);
                    break;

                //Обработка кнопок Ангела-Хранителя
                case "/guardian_angel_answer":
                    sendGuardianAngelButton(chatId, receivedMessage);
                    break;
                case "/calculate_guardian_angel":
                    calculateGuardianAngelNumber(chatId, receivedMessage);
                    break;
                default:
                    break;
            }

            // Подтверждаем, что callback был обработан
            try {
                execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            } catch (TelegramApiException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void sendMonthButtonsNine(long chatId, String yearMessage) {
        // Извлекаем год из сообщения
        int year = Integer.parseInt(yearMessage.substring(1));
        userYearMap.put(chatId, year); // Сохраняем год в userYearMap

        // Получаем разметку кнопок месяцев
        InlineKeyboardMarkup markup = monthMarkup();

        // Отправляем сообщение с кнопками
        sendMessage(chatId, "Выберите месяц в котором вы хотите узнать денежные даты: \uD83E\uDD11", markup, "");
    }

    private int getMonthNumberNine(String monthText) {
        switch (monthText) {
            case "/january_nine": return 1;
            case "/february_nine": return 2;
            case "/march_nine": return 3;
            case "/april_nine": return 4;
            case "/may_nine": return 5;
            case "/june_nine": return 6;
            case "/july_nine": return 7;
            case "/august_nine": return 8;
            case "/september_nine": return 9;
            case "/october_nine": return 10;
            case "/november_nine": return 11;
            case "/december_nine": return 12;
            default: return -1; // Неверный месяц
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/help":
                sendHelpText(chatId, HELP_TEXT);
                break;
            case "/nine_year":
                nineYear(chatId, YEAR_TEXT, backYear());
                break;
            case "/back_to_year":
                sendMessage(chatId, "Выберите год:", yearMarkup(), ""); // Возврат к списку лет
                break;
            case "/nine":
                nineBot(chatId, NINE_TEXT);
                break;
            case "/back_to_months":
                sendMessage(chatId, "Выберите месяц:", monthMarkup(), ""); // Возврат к списку месяцев
                break;
            case "/main_menu":
                sendMessage(chatId, "Добро пожаловать в главное меню!", inlineMarkup(), "https://drive.google.com/uc?id=1Q1lyYtvgG9-164wa8d91pBVvYX3DTo8H");
                break;
            case "/nine_cycle":
                sendNineYearCycleButtons(chatId, CYCLE_TEXT);
                break;
            case "/calculate_cycle":
                calculateNineCycleYear(chatId, CALCULATE_CYCLE_TEXT);
                break;
            case "/professional_number_answer":
                sendProfessionNumberOfDestiny(chatId, PROFESSION_TEXT);
                break;
            case "/professional_number_calculate_answer":
                calculateProfessionNumberOfDestiny(chatId, CALCULATE_PROFESSIONAL_TEXT);
                break;
            case "/love_month":
                sendLoveOnMonthsButtons(chatId, LOVE_TEXT);
                break;
            case "/back_to_months_love_answer":
                loveMonth(chatId,"Выберите месяц в котором Вы были рождены: ", monthMarkupLove());
                break;
            case "/guardian_angel":
                sendGuardianAngelButton(chatId, GUARDIAN_ANGEL_TEXT);
                break;
            case "/calculate_guardian_angel_answer":
                calculateGuardianAngelNumber(chatId, CALCULATE_GUARDIAN_ANGEL_TEXT);
                break;
            case "/lucky_number":
                sendLuckyNumberButton(chatId, LUCKY_TEXT);
                break;
            case "/calculate_lucky_number":
                calculateLuckyNumberOfDestiny(chatId, CALCULATE_LUCKY_NUMBER_TEXT);
                break;
            case "/calculate_lucky_number_next":
                sendLuckyNumberNext(chatId, NEXT_LUCKY_NUMBER_TEXT);
                break;
            default:
                // Проверяем состояние пользователя и обрабатываем ввод
                String userState = userStateManager.getUserState(chatId);
                if ("WAITING_FOR_BIRTH_DATE".equals(userState)) {
                    processUserInput(chatId, receivedMessage);
                } else if ("WAITING_FOR_YEAR".equals(userState)) {
                    processUserInput(chatId, receivedMessage);
                } else if ("WAITING_FOR_BIRTH_DATE_FROM_PROFESSIONAL".equals(userState)) {
                    processCalculateProfessionNumber(chatId, receivedMessage);
                } else if ("WAITING_FOR_BIRTH_DATE_FROM_ANGEL".equals(userState)) {
                    processCalculateAngelNumber(chatId, receivedMessage);
                } else if ("WAITING_FOR_BIRTH_DATE_FROM_LUCKY".equals(userState)) {
                    processCalculateLuckyNumber(chatId, receivedMessage);
                } else {
                    // Обработка общего ввода, если состояние не ожидает даты или года
                    //sendMessage(chatId, "Команда не распознана. Пожалуйста, введите корректную команду.", null);
                }
                break;
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! Я телеграм-бот Нумеролога Лидии!");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, старт");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void sendHelpText(long chatId, String helpText){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(helpText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, помощь");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    public void sendMessage(long chatId, String text, InlineKeyboardMarkup replyMarkup, String photoUrl) {
        // Если передан URL изображения, отправляем фото
        if (photoUrl != null && !photoUrl.isEmpty()) {
            sendPhoto(chatId, photoUrl, text, replyMarkup);
        } else {
            SendMessage message = new SendMessage(); // Создаем объект SendMessage
            message.setChatId(String.valueOf(chatId)); // Устанавливаем ID чата
            message.setText(text); // Устанавливаем текст сообщения
            message.setParseMode("Markdown");
            message.setReplyMarkup(replyMarkup); // Устанавливаем разметку клавиатуры, если она есть

            try {
                execute(message);
            } catch (TelegramApiException e) {
                logger.error("Ошибка при отправке сообщения: " + e.getMessage());
            }
        }
    }

    private void nineBot(long chatId, String nineText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(nineText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(monthMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, девятка");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void nineYear(long chatId, String yearText, InlineKeyboardMarkup inlineKeyboardMarkup) {
        // Сначала отправим изображение
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        photoMessage.setPhoto(new InputFile
                ("https://i06.fotocdn.net/s219/1cebd38d272fd948/public_pin_l/2992153356.jpg")); // Фактический URL изображения

        try {
            execute(photoMessage);
            logger.info("Изображение отправлено в методе nineYear");

            // Теперь отправим текстовое сообщение с кнопками
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(yearText);
            message.setParseMode("Markdown");
            message.setReplyMarkup(yearMarkup()); // Используем переданный inlineKeyboardMarkup

            execute(message);
            logger.info("Ответ отправлен, год девятка");

        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean isNine(int sum) {
        // Проверяем, сводится ли сумма к 9
        return sum % 9 == 0;
    }

    private int sumDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum > 9 ? sumDigits(sum) : sum; // Суммируем до однозначного числа
    }

    // Метод для получения названия месяца
    private String getMonthNameNine(int month) {
        switch (month) {
            case 1: return "*ЯНВАРЬ* ☃\uFE0F";
            case 2: return "*ФЕВРАЛЬ* \uD83C\uDF28\uFE0F";
            case 3: return "*МАРТ* \uD83C\uDF38";
            case 4: return "*АПРЕЛЬ* \uD83C\uDF3C";
            case 5: return "*МАЙ* \uD83C\uDF31";
            case 6: return "*ИЮНЬ* ☀\uFE0F";
            case 7: return "*ИЮЛЬ* \uD83C\uDFD6\uFE0F";
            case 8: return "*АВГУСТ* \uD83C\uDF3B";
            case 9: return "*СЕНТЯБРЬ* \uD83C\uDF42";
            case 10: return "*ОКТЯБРЬ* \uD83C\uDF41";
            case 11: return "*НОЯБРЬ* \uD83C\uDF3E";
            case 12: return "*ДЕКАБРЬ* ❄\uFE0F";
            default: return "";
        }
    }

    // Метод для получения количества дней в месяце
    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            default:
                return 0; // Неверный месяц
        }
    }

    // Метод для проверки, является ли год високосным
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void calculateNineDaysForMonth(long chatId, int year, int month) {
        StringBuilder result = new StringBuilder("Выбранный год " + year + "\n\n");
        result.append(getMonthNameNine(month)).append("\n\n");
        result.append("*Дни «денежной девятки»:* \uD83D\uDCB0\n");

        // По сумме чисел дня
        result.append("\n*По сумме чисел дня:* \n");
        for (int day = 1; day <= getDaysInMonth(month, year); day++) {
            if (isNine(day)) {
                result.append(day).append(", ");
            }
        }
        result.setLength(result.length() - 2); // Удаляем последнюю запятую и пробел
        result.append("\n");

        // По сумме чисел дня и месяца
        result.append("\n*По сумме чисел дня и месяца:* \n");
        for (int day = 1; day <= getDaysInMonth(month, year); day++) {
            if (isNine(day + month)) {
                result.append(String.format("%02d.%02d", day, month)).append(", ");
            }
        }
        result.setLength(result.length() - 2); // Удаляем последнюю запятую и пробел
        result.append("\n");

        // По сумме чисел дня, месяца и года
        result.append("\n*По сумме чисел дня, месяца и года:*\n");
        for (int day = 1; day <= getDaysInMonth(month, year); day++) {
            if (isNine(day + month + sumDigits(year))) {
                result.append(String.format("%02d.%02d.%d", day, month, year)).append(", ");
            }
        }
        result.setLength(result.length() - 2); // Удаляем последнюю запятую и пробел
        result.append("\n");

        // По сумме чисел дня и года
        result.append("\n*По сумме чисел дня и года:*\n");
        for (int day = 1; day <= getDaysInMonth(month, year); day++) {
            if (isNine(day + sumDigits(year))) {
                result.append(String.format("%02d.%02d.%d", day, month, year)).append(", ");
            }
        }
        result.setLength(result.length() - 2); // Удаляем последнюю запятую и пробел

        sendMessage(chatId, result.toString(), backMonthNine(), "");
    }

    //ДЕВЯТИЛЕТНИЙ ЦИКЛ

    private void sendNineYearCycleButtons(long chatId, String cycleText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(cycleText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(cycleMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, цикл");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void calculateNineCycleYear(long chatId, String cycleTextBack) {
        // Запрашиваем у пользователя дату рождения
        sendMessage(chatId, "Введите вашу дату рождения в формате дд.мм (например, 01.01):", null, "");

        // Сохраняем состояние ожидания даты рождения
        userStateManager.setUserState(chatId, "WAITING_FOR_BIRTH_DATE");
    }

    private void processUserInput(long chatId, String input) {
        String userState = userStateManager.getUserState(chatId);

        if ("WAITING_FOR_BIRTH_DATE".equals(userState)) {
            // Проверяем формат даты
            if (input.matches("\\d{2}\\.\\d{2}")) {
                String[] parts = input.split("\\.");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);

                // Запрашиваем год
                sendMessage(chatId, "Введите год, который хотите узнать (например, 2025):", null, "");
                userStateManager.setUserInput(chatId, new int[]{day, month}); // Сохраняем день и месяц
                userStateManager.setUserState(chatId, "WAITING_FOR_YEAR");
            } else {
                sendMessage(chatId, "Неверный формат. Пожалуйста, введите дату в формате дд.мм:", null, "");
            }
        } else if ("WAITING_FOR_YEAR".equals(userState)) {
            // Проверяем, что введен год
            try {
                int year = Integer.parseInt(input);
                int[] birthDate = userStateManager.getUserInput(chatId);
                int day = birthDate[0];
                int month = birthDate[1];

                // Проверяем корректность полной даты
                String fullDate = String.format("%02d.%02d.%04d", day, month, year);
                if (isValidDate(fullDate)) {
                    // Выполняем расчет
                    int sum = calculateSum(day, month, year);
                    int finalResult = calculateFinalNumber(sum);
                    sendMessage(chatId, " " + numberInfoProvider.getInfoAboutNumber(finalResult), null, "");
                    sendMessage(chatId, "Выйти в главное меню?", backToMainMenu(), "");

                    // Сбрасываем состояние
                    userStateManager.clearUserState(chatId);
                } else {
                    sendMessage(chatId, "Некорректная дата. Пожалуйста, введите существующую дату:", null, "");
                    // Возвращаем пользователя к вводу даты рождения
                    userStateManager.setUserState(chatId, "WAITING_FOR_BIRTH_DATE");
                    sendMessage(chatId, "Введите вашу дату рождения в формате дд.мм (например, 01.01):", null, "");
                }
            } catch (NumberFormatException e) {
                sendMessage(chatId, "Пожалуйста, введите корректный год:", null, "");
            }
        }
    }

    private void sendPhoto(long chatId, String photoUrl, String caption, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        photoMessage.setPhoto(new InputFile(photoUrl)); // Используем InputFile для URL изображения
        photoMessage.setCaption(caption); // Укажите текстовое сообщение, если нужно
        photoMessage.setReplyMarkup(inlineKeyboardMarkup); // Устанавливаем разметку клавиатуры

        try {
            execute(photoMessage);
            logger.info("Изображение отправлено с кнопками");
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке изображения: " + e.getMessage());
        }
    }

    private void sendProfessionNumberOfDestiny(long chatId, String professionText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(professionText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(calculateProfessionMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, профессия");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void calculateProfessionNumberOfDestiny(long chatId, String professionText) {
        // Запрашиваем у пользователя дату рождения
        sendMessage(chatId, "Введите вашу дату рождения в формате дд.мм.гггг (например, 01.01.2000):", null, "");

        // Сохраняем состояние ожидания даты рождения
        userStateManager.setUserState(chatId, "WAITING_FOR_BIRTH_DATE_FROM_PROFESSIONAL");
    }

    private void processCalculateProfessionNumber(long chatId, String input) {
        String userState = userStateManager.getUserState(chatId);

        if ("WAITING_FOR_BIRTH_DATE_FROM_PROFESSIONAL".equals(userState)) {
            if (input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                if (isValidDate(input)) {
                    String[] parts = input.split("\\.");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);

                    // Выполняем расчет
                    int sum = calculateSum(day, month, year);
                    int finalResult = calculateFinalNumber(sum);
                    sendMessage(chatId, " " + numberInfoProvider.getProfessionalInfoAboutNumber(finalResult), null, "");
                    sendMessage(chatId, "Выйти в главное меню?", backToMainMenu(), "");

                    userStateManager.clearUserState(chatId);
                } else {
                    sendMessage(chatId, "Некорректная дата. Пожалуйста, введите существующую дату в формате дд.мм.гггг:", null, "");
                }
            } else {
                sendMessage(chatId, "Неверный формат. Пожалуйста, введите дату в формате дд.мм.гггг:", null, "");
            }
        }
    }

    private int calculateSum(int day, int month, int year) {
        // Суммируем все цифры дня, месяца и года
        int sum = 0;

        // Суммируем цифры дня
        sum += day / 10; // десятки дня
        sum += day % 10; // единицы дня

        // Суммируем цифры месяца
        sum += month / 10; // десятки месяца
        sum += month % 10; // единицы месяца

        // Суммируем цифры года
        sum += year / 1000; // тысячи года
        sum += (year / 100) % 10; // сотни года
        sum += (year / 10) % 10; // десятки года
        sum += year % 10; // единицы года

        return sum; // Возвращаем общую сумму
    }

    private int calculateFinalNumber(int sum) {
        // Повторяем сложение цифр, пока результат не станет меньше 10
        while (sum >= 10) {
            sum = String.valueOf(sum).chars().map(c -> c - '0').sum();
        }
        return sum;
    }

    //КАК НАЙТИ СВОЮ ЛЮБОВЬ

    private void sendMonthButtonsLove(long chatId, String loveMonthText) {
        // Получаем разметку кнопок месяцев
        InlineKeyboardMarkup markup = monthMarkupLove();

        // Отправляем сообщение с кнопками
        sendMessage(chatId, "Вам подскажет месяц Вашего рождения: ", markup, "");
    }

    private int getMonthNumberLove(String monthText) {
        switch (monthText) {
            case "/january_love": return 1;
            case "/february_love": return 2;
            case "/march_love": return 3;
            case "/april_love": return 4;
            case "/may_love": return 5;
            case "/june_love": return 6;
            case "/july_love": return 7;
            case "/august_love": return 8;
            case "/september_love": return 9;
            case "/october_love": return 10;
            case "/november_love": return 11;
            case "/december_love": return 12;
            default: return -1; // Неверный месяц
        }
    }

    private void sendLoveOnMonthsButtons(long chatId, String loveText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(loveText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(backMonthLove());

        try {
            execute(message);
            logger.info("Ответ отправлен, любовь текст");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void loveMonth(long chatId, String loveText, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(loveText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(monthMarkupLove());

        try {
            execute(message);
            logger.info("Ответ отправлен, любовные месяцы");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    // Метод для получения названия месяца
    private String getMonthNameLove(int monthLove) {
        switch (monthLove) {
            case 1: return "*ЯНВАРЬ* ☃\uFE0F";
            case 2: return "*ФЕВРАЛЬ* \uD83C\uDF28\uFE0F";
            case 3: return "*МАРТ* \uD83C\uDF38";
            case 4: return "*АПРЕЛЬ* \uD83C\uDF3C";
            case 5: return "*МАЙ* \uD83C\uDF31";
            case 6: return "*ИЮНЬ* ☀\uFE0F";
            case 7: return "*ИЮЛЬ* \uD83C\uDFD6\uFE0F";
            case 8: return "*АВГУСТ* \uD83C\uDF3B";
            case 9: return "*СЕНТЯБРЬ* \uD83C\uDF42";
            case 10: return "*ОКТЯБРЬ* \uD83C\uDF41";
            case 11: return "*НОЯБРЬ* \uD83C\uDF3E";
            case 12: return "*ДЕКАБРЬ* ❄\uFE0F";
            default: return "";
        }
    }

    private void calculateLoveMonth(long chatId, int monthLove) {
        // Получаем название месяца через numberInfoProvider
        String monthName = getMonthNameLove(monthLove);

        // Получаем совет о любви для выбранного месяца через numberInfoProvider
        String loveAdvice = numberInfoProvider.getLoveAdvice(monthLove);

        // Формируем сообщение для отправки
        String messageToSend = String.format("%s:\n\n%s", monthName, loveAdvice);

        // Отправляем сообщение пользователю
        sendMessage(chatId, messageToSend, null, "");
        sendMessage(chatId, "Выйти в главное меню?", backToMainMenu(), "");
    }

    //ОБРАБОТКА КНОПОК АНГЕЛА-ХРАНИТЕЛЯ

    private void sendGuardianAngelButton(long chatId, String angelText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(angelText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(angelMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, ангел вступительный текст");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void calculateGuardianAngelNumber(long chatId, String angelText) {
        // Запрашиваем у пользователя дату рождения
        sendMessage(chatId, "Введите вашу дату рождения в формате дд.мм.гггг (например, 01.01.2000):", null, "");

        // Сохраняем состояние ожидания даты рождения
        userStateManager.setUserState(chatId, "WAITING_FOR_BIRTH_DATE_FROM_ANGEL");
    }

    private void processCalculateAngelNumber(long chatId, String input) {
        String userState = userStateManager.getUserState(chatId);

        if ("WAITING_FOR_BIRTH_DATE_FROM_ANGEL".equals(userState)) {
            if (input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                if (isValidDate(input)) {
                    String[] parts = input.split("\\.");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);

                    // Выполняем расчет
                    int sum = calculateSum(day, month, year);
                    int finalResult = calculateFinalNumber(sum);
                    sendMessage(chatId, " " + numberInfoProvider.getGuardianAngelAdvice(finalResult), null, "");
                    sendMessage(chatId, "Выйти в главное меню?", backToMainMenu(), "");

                    userStateManager.clearUserState(chatId);
                } else {
                    sendMessage(chatId, "Некорректная дата. Пожалуйста, введите существующую дату в формате дд.мм.гггг:", null, "");
                }
            } else {
                sendMessage(chatId, "Неверный формат. Пожалуйста, введите дату в формате дд.мм.гггг:", null, "");
            }
        }
    }

    //КОД-УДАЧИ

    private void sendLuckyNumberButton(long chatId, String luckyText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(luckyText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(luckyMarkup());

        try {
            execute(message);
            logger.info("Ответ отправлен, код-удачи");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    private void calculateLuckyNumberOfDestiny(long chatId, String luckyCalculateText) {
        // Запрашиваем у пользователя дату рождения
        sendMessage(chatId, "Введите вашу дату рождения в формате дд.мм.гггг (например, 01.01.2000):", null, "");

        // Сохраняем состояние ожидания даты рождения
        userStateManager.setUserState(chatId, "WAITING_FOR_BIRTH_DATE_FROM_LUCKY");
    }

    private void processCalculateLuckyNumber(long chatId, String input) {
        String userState = userStateManager.getUserState(chatId);

        if ("WAITING_FOR_BIRTH_DATE_FROM_LUCKY".equals(userState)) {
            if (input.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                if (isValidDate(input)) {
                    String[] parts = input.split("\\.");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);

                    // Выполняем расчет
                    int firstIndicator = sumDigits(day);
                    int secondIndicator = sumDigits(month);
                    int thirdIndicator = sumDigits(year);
                    int finalSum = firstIndicator + secondIndicator + thirdIndicator;
                    int fourthIndicator = sumDigits(finalSum);

                    String luckyNumber = String.format("*%d%d%d%d*", firstIndicator, secondIndicator, thirdIndicator, fourthIndicator);
                    sendMessage(chatId, "*Ваш код удачи:* " + luckyNumber, null, "");
                    sendMessage(chatId, " \n" + numberInfoProvider.getInfoLuckyNumber(sumDigits(day)), null, "");
                    sendMessage(chatId, " \n" + numberInfoProvider.getInfoLuckyNumber(sumDigits(month)), null, "");
                    sendMessage(chatId, " \n" + numberInfoProvider.getInfoLuckyNumber(sumDigits(year)), null, "");
                    sendMessage(chatId, " \n" + numberInfoProvider.getInfoLuckyNumber(sumDigits(finalSum)), null, "");

                    sendMessage(chatId, "Как активизировать код удачи?", luckyNumberNextMarkup(), "");
                    userStateManager.clearUserState(chatId);
                } else {
                    sendMessage(chatId, "Некорректная дата. Пожалуйста, введите существующую дату в формате дд.мм.гггг:", null, "");
                }
            } else {
                sendMessage(chatId, "Неверный формат. Пожалуйста, введите дату в формате дд.мм.гггг:", null, "");
            }
        }
    }

    private void sendLuckyNumberNext(long chatId, String luckyTextNext) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(luckyTextNext);
        message.setParseMode("Markdown");
        message.setReplyMarkup(backToMainMenu());

        try {
            execute(message);
            logger.info("Ответ отправлен, код-удачи объяснение");
        } catch (TelegramApiException e){
            logger.error(e.getMessage());
        }
    }

    // Метод для проверки корректности даты
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        dateFormat.setLenient(false); // Устанавливаем строгий режим

        try {
            Date date = dateFormat.parse(dateStr);
            return date != null; // Если парсинг прошел успешно, дата корректна
        } catch (ParseException e) {
            return false; // Если произошла ошибка парсинга, дата некорректна
        }
    }
}