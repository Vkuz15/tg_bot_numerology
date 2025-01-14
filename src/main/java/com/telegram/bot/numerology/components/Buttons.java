package com.telegram.bot.numerology.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {

    private static final InlineKeyboardButton BACK_TO_YEAR_BUTTON = new InlineKeyboardButton("Денежная девятка \uD83D\uDCB5");
    private static final InlineKeyboardButton CYCLE_BUTTON = new InlineKeyboardButton("Девятилетний цикл \uD83D\uDD04");
    private static final InlineKeyboardButton PROFESSION_TEXT_BUTTON = new InlineKeyboardButton("Узнать свою профессию \uD83D\uDCBC");
    private static final InlineKeyboardButton LOVE_ON_MONTH_BUTTON = new InlineKeyboardButton("Как найти свою любовь ❤\uFE0F");
    private static final InlineKeyboardButton GUARDIAN_ANGEL_BUTTON = new InlineKeyboardButton("Ваш Ангел-Хранитель \uD83D\uDC7C");
    private static final InlineKeyboardButton LUCKY_NUMBER_BUTTON = new InlineKeyboardButton("Ваш код удачи \uD83D\uDE4F");
    private static final InlineKeyboardButton BACK_TO_MENU_BUTTON = new InlineKeyboardButton("Главное меню");

    private static final InlineKeyboardButton BACK_TO_MONTH_BUTTON = new InlineKeyboardButton("Список месяцев");
    private static final InlineKeyboardButton JANUARY_BUTTON = new InlineKeyboardButton("Январь");
    private static final InlineKeyboardButton FEBRUARY_BUTTON = new InlineKeyboardButton("Февраль");
    private static final InlineKeyboardButton MARCH_BUTTON = new InlineKeyboardButton("Март");
    private static final InlineKeyboardButton APRIL_BUTTON = new InlineKeyboardButton("Апрель");
    private static final InlineKeyboardButton MAY_BUTTON = new InlineKeyboardButton("Май");
    private static final InlineKeyboardButton JUNE_BUTTON = new InlineKeyboardButton("Июнь");
    private static final InlineKeyboardButton JULY_BUTTON = new InlineKeyboardButton("Июль");
    private static final InlineKeyboardButton AUGUST_BUTTON = new InlineKeyboardButton("Август");
    private static final InlineKeyboardButton SEPTEMBER_BUTTON = new InlineKeyboardButton("Сентябрь");
    private static final InlineKeyboardButton OCTOBER_BUTTON = new InlineKeyboardButton("Октябрь");
    private static final InlineKeyboardButton NOVEMBER_BUTTON = new InlineKeyboardButton("Ноябрь");
    private static final InlineKeyboardButton DECEMBER_BUTTON = new InlineKeyboardButton("Декабрь");

    private static final InlineKeyboardButton TWENTY_FIVE_BUTTON = new InlineKeyboardButton("2025");
    private static final InlineKeyboardButton TWENTY_SIX_BUTTON = new InlineKeyboardButton("2026");
    private static final InlineKeyboardButton TWENTY_SEVEN_BUTTON = new InlineKeyboardButton("2027");

    private static final InlineKeyboardButton CALCULATE_TO_CYCLE_BUTTON = new InlineKeyboardButton("Узнать свой цикл");
    private static final InlineKeyboardButton CALCULATE_PROFESSION_TEXT_BUTTON = new InlineKeyboardButton("Узнать свое число судьбы");

    private static final InlineKeyboardButton BACK_TO_MONTH_LOVE_BUTTON = new InlineKeyboardButton("Хочу узнать!");
    private static final InlineKeyboardButton CALCULATE_GUARDIAN_ANGEL_BUTTON = new InlineKeyboardButton("Давай узнаем!");
    private static final InlineKeyboardButton CALCULATE_LUCKY_NUMBER_BUTTON = new InlineKeyboardButton("Хочу знать!");

    private static final InlineKeyboardButton NEXT_LUCKY_NUMBER_BUTTON = new InlineKeyboardButton("Продолжение");

    public static InlineKeyboardMarkup inlineMarkup() {
        BACK_TO_YEAR_BUTTON.setCallbackData("/nine_year");
        CYCLE_BUTTON.setCallbackData("/nine_cycle");
        PROFESSION_TEXT_BUTTON.setCallbackData("/professional_number_answer");
        LOVE_ON_MONTH_BUTTON.setCallbackData("/love_month");
        GUARDIAN_ANGEL_BUTTON.setCallbackData("/guardian_angel");
        LUCKY_NUMBER_BUTTON.setCallbackData("/lucky_number");

        List<InlineKeyboardButton> row2 = List.of(BACK_TO_YEAR_BUTTON);
        List<InlineKeyboardButton> row3 = List.of(CYCLE_BUTTON);
        List<InlineKeyboardButton> row4 = List.of(PROFESSION_TEXT_BUTTON);
        List<InlineKeyboardButton> row5 = List.of(LOVE_ON_MONTH_BUTTON);
        List<InlineKeyboardButton> row6 = List.of(GUARDIAN_ANGEL_BUTTON);
        List<InlineKeyboardButton> row7 = List.of(LUCKY_NUMBER_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row2, row3, row4, row5, row6, row7);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup monthMarkup() {
        JANUARY_BUTTON.setCallbackData("/january_nine");
        FEBRUARY_BUTTON.setCallbackData("/february_nine");
        MARCH_BUTTON.setCallbackData("/march_nine");
        APRIL_BUTTON.setCallbackData("/april_nine");
        MAY_BUTTON.setCallbackData("/may_nine");
        JUNE_BUTTON.setCallbackData("/june_nine");
        JULY_BUTTON.setCallbackData("/july_nine");
        AUGUST_BUTTON.setCallbackData("/august_nine");
        SEPTEMBER_BUTTON.setCallbackData("/september_nine");
        OCTOBER_BUTTON.setCallbackData("/october_nine");
        NOVEMBER_BUTTON.setCallbackData("/november_nine");
        DECEMBER_BUTTON.setCallbackData("/december_nine");

        List<InlineKeyboardButton> row1 = List.of(JANUARY_BUTTON, FEBRUARY_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(MARCH_BUTTON, APRIL_BUTTON);
        List<InlineKeyboardButton> row3 = List.of(MAY_BUTTON, JUNE_BUTTON);
        List<InlineKeyboardButton> row4 = List.of(JULY_BUTTON, AUGUST_BUTTON);
        List<InlineKeyboardButton> row5 = List.of(SEPTEMBER_BUTTON, OCTOBER_BUTTON);
        List<InlineKeyboardButton> row6 = List.of(NOVEMBER_BUTTON, DECEMBER_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4, row5, row6);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup backMonthNine() {
        BACK_TO_MONTH_BUTTON.setCallbackData("/back_to_months");
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton("Главное меню");
        mainMenuButton.setCallbackData("/main_menu");

        List<InlineKeyboardButton> row1 = List.of(BACK_TO_MONTH_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(mainMenuButton); // Новая строка с кнопкой "Главное меню"

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup backMonthLove() {
        BACK_TO_MONTH_LOVE_BUTTON.setCallbackData("/back_to_months_love");

        List<InlineKeyboardButton> row1 = List.of(BACK_TO_MONTH_LOVE_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup monthMarkupLove() {
        JANUARY_BUTTON.setCallbackData("/january_love");
        FEBRUARY_BUTTON.setCallbackData("/february_love");
        MARCH_BUTTON.setCallbackData("/march_love");
        APRIL_BUTTON.setCallbackData("/april_love");
        MAY_BUTTON.setCallbackData("/may_love");
        JUNE_BUTTON.setCallbackData("/june_love");
        JULY_BUTTON.setCallbackData("/july_love");
        AUGUST_BUTTON.setCallbackData("/august_love");
        SEPTEMBER_BUTTON.setCallbackData("/september_love");
        OCTOBER_BUTTON.setCallbackData("/october_love");
        NOVEMBER_BUTTON.setCallbackData("/november_love");
        DECEMBER_BUTTON.setCallbackData("/december_love");

        List<InlineKeyboardButton> row1 = List.of(JANUARY_BUTTON, FEBRUARY_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(MARCH_BUTTON, APRIL_BUTTON);
        List<InlineKeyboardButton> row3 = List.of(MAY_BUTTON, JUNE_BUTTON);
        List<InlineKeyboardButton> row4 = List.of(JULY_BUTTON, AUGUST_BUTTON);
        List<InlineKeyboardButton> row5 = List.of(SEPTEMBER_BUTTON, OCTOBER_BUTTON);
        List<InlineKeyboardButton> row6 = List.of(NOVEMBER_BUTTON, DECEMBER_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4, row5, row6);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup yearMarkup() {
        TWENTY_FIVE_BUTTON.setCallbackData("/2025");
        TWENTY_SIX_BUTTON.setCallbackData("/2026");
        TWENTY_SEVEN_BUTTON.setCallbackData("/2027");

        List<InlineKeyboardButton> row1 = List.of(TWENTY_FIVE_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(TWENTY_SIX_BUTTON);
        List<InlineKeyboardButton> row3 = List.of(TWENTY_SEVEN_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup backYear() {
        BACK_TO_YEAR_BUTTON.setCallbackData("/back_to_year");
        BACK_TO_MENU_BUTTON.setCallbackData("/main_menu");

        List<InlineKeyboardButton> row1 = List.of(BACK_TO_YEAR_BUTTON);
        List<InlineKeyboardButton> row2 = List.of(BACK_TO_MENU_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup cycleMarkup() {
        CALCULATE_TO_CYCLE_BUTTON.setCallbackData("/calculate_cycle");

        List<InlineKeyboardButton> row1 = List.of(CALCULATE_TO_CYCLE_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup backToMainMenu() {
        BACK_TO_MENU_BUTTON.setCallbackData("/main_menu");

        List<InlineKeyboardButton> row1 = List.of(BACK_TO_MENU_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup calculateProfessionMarkup() {
        CALCULATE_PROFESSION_TEXT_BUTTON.setCallbackData("/calculate_profession");

        List<InlineKeyboardButton> row1 = List.of(CALCULATE_PROFESSION_TEXT_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup angelMarkup() {
        CALCULATE_GUARDIAN_ANGEL_BUTTON.setCallbackData("/calculate_guardian_angel");

        List<InlineKeyboardButton> row1 = List.of(CALCULATE_GUARDIAN_ANGEL_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup luckyMarkup() {
        CALCULATE_LUCKY_NUMBER_BUTTON.setCallbackData("/calculate_lucky_number");

        List<InlineKeyboardButton> row1 = List.of(CALCULATE_LUCKY_NUMBER_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public static InlineKeyboardMarkup luckyNumberNextMarkup() {
        NEXT_LUCKY_NUMBER_BUTTON.setCallbackData("/calculate_lucky_number_next");

        List<InlineKeyboardButton> row1 = List.of(NEXT_LUCKY_NUMBER_BUTTON);

        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }
}
