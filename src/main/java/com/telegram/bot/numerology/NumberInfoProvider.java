package com.telegram.bot.numerology;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NumberInfoProvider {

    private Map<Integer, String> numberInfoMap;
    private Map<Integer, String> professionalInfoMap;
    private Map<Integer, String> loveAdviceMap;
    private Map<Integer, String> angelInfoMap;
    private Map<Integer, String> luckyInfoMap;

    public NumberInfoProvider() {
        numberInfoMap = new HashMap<>(); // Инициализация map
        loadNumberInfo(); // Загрузка информации о персональных числах года
        professionalInfoMap = new HashMap<>(); // Инициализация map
        loadProfessionalInfo(); // Загрузка информации о профессиях
        loveAdviceMap = new HashMap<>(); // Инициализация map для советов о любви
        loadLoveAdvice(); // Загрузка информации о советах о любви
        angelInfoMap = new HashMap<>(); // Инициализация map для Ангелов-Хранителей
        loadGuardianAngelAdvice(); // Загрузка информации об Ангелах
        luckyInfoMap = new HashMap<>(); //
        loadLuckyNumberInfo();
    }

    private void loadNumberInfo() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("number_info.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder currentInfo = new StringBuilder();
            String line;
            int currentNumber = -1;

            while ((line = br.readLine()) != null) {
                // Убираем только пробелы, но не пустые строки
                if (line.trim().isEmpty()) {
                    // Добавляем пустую строку в текущую информацию
                    currentInfo.append("\n");
                    continue;
                }

                // Проверяем, является ли строка номером
                if (line.matches("\\d+:.*")) {
                    // Если это не первая запись, сохраняем предыдущую
                    if (currentNumber != -1) {
                        numberInfoMap.put(currentNumber, currentInfo.toString().trim());
                    }

                    // Обрабатываем новую строку
                    String[] parts = line.split(":", 2);
                    try {
                        currentNumber = Integer.parseInt(parts[0].trim());
                        currentInfo = new StringBuilder(parts[1].trim() + "\n"); // Начинаем новую запись
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа: " + parts[0] + ". Строка пропущена.");
                    }
                } else {
                    // Если это не номер, добавляем строку к текущему описанию
                    currentInfo.append(line).append("\n");
                }
            }

            // Сохраняем последнюю запись
            if (currentNumber != -1) {
                numberInfoMap.put(currentNumber, currentInfo.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInfoAboutNumber(int number) {
        return numberInfoMap.getOrDefault(number, "Нет информации, повторите попытку");
    }

    private void loadProfessionalInfo() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("professional_info.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder currentInfo = new StringBuilder();
            String line;
            int currentNumber = -1;

            while ((line = br.readLine()) != null) {
                // Убираем только пробелы, но не пустые строки
                if (line.trim().isEmpty()) {
                    // Добавляем пустую строку в текущую информацию
                    currentInfo.append("\n");
                    continue;
                }

                // Проверяем, является ли строка номером
                if (line.matches("\\d+:.*")) {
                    // Если это не первая запись, сохраняем предыдущую
                    if (currentNumber != -1) {
                        professionalInfoMap.put(currentNumber, currentInfo.toString().trim());
                    }

                    // Обрабатываем новую строку
                    String[] parts = line.split(":", 2);
                    try {
                        currentNumber = Integer.parseInt(parts[0].trim());
                        currentInfo = new StringBuilder(parts[1].trim() + "\n"); // Начинаем новую запись
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа: " + parts[0] + ". Строка пропущена.");
                    }
                } else {
                    // Если это не номер, добавляем строку к текущему описанию
                    currentInfo.append(line).append("\n");
                }
            }

            // Сохраняем последнюю запись
            if (currentNumber != -1) {
                professionalInfoMap.put(currentNumber, currentInfo.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProfessionalInfoAboutNumber(int number) {
        return professionalInfoMap.getOrDefault(number, "Нет информации о профессии, повторите попытку");
    }

    // Метод для загрузки советов о любви
    private void loadLoveAdvice() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("love_month.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder currentAdvice = new StringBuilder();
            String line;
            int currentMonth = -1;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    currentAdvice.append("\n");
                    continue;
                }

                if (line.matches("\\d+:.*")) {
                    if (currentMonth != -1) {
                        loveAdviceMap.put(currentMonth, currentAdvice.toString().trim());
                    }

                    String[] parts = line.split(":", 2);
                    try {
                        currentMonth = Integer.parseInt(parts[0].trim());
                        currentAdvice = new StringBuilder(parts[1].trim() + "\n");
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа: " + parts[0] + ". Строка пропущена.");
                    }
                } else {
                    currentAdvice.append(line).append("\n");
                }
            }

            if (currentMonth != -1) {
                loveAdviceMap.put(currentMonth, currentAdvice.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения совета о любви по номеру месяца
    public String getLoveAdvice(int month) {
        return loveAdviceMap.getOrDefault(month, "Нет совета о любви для этого месяца, повторите попытку");
    }

    // Метод для загрузки информации об Ангелах-Хранителях
    private void loadGuardianAngelAdvice() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("guardian_angel.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder currentAdvice = new StringBuilder();
            String line;
            int currentMonth = -1;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    currentAdvice.append("\n");
                    continue;
                }

                if (line.matches("\\d+:.*")) {
                    if (currentMonth != -1) {
                        angelInfoMap.put(currentMonth, currentAdvice.toString().trim());
                    }

                    String[] parts = line.split(":", 2);
                    try {
                        currentMonth = Integer.parseInt(parts[0].trim());
                        currentAdvice = new StringBuilder(parts[1].trim() + "\n");
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа: " + parts[0] + ". Строка пропущена.");
                    }
                } else {
                    currentAdvice.append(line).append("\n");
                }
            }

            if (currentMonth != -1) {
                angelInfoMap.put(currentMonth, currentAdvice.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения числа Ангела-Хранителя
    public String getGuardianAngelAdvice(int month) {
        return angelInfoMap.getOrDefault(month, "Нет такого числа, повторите попытку");
    }

    // Метод для загрузки информации об Ангелах-Хранителях
    private void loadLuckyNumberInfo() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("lucky_number.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder currentAdvice = new StringBuilder();
            String line;
            int currentMonth = -1;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    currentAdvice.append("\n");
                    continue;
                }

                if (line.matches("\\d+:.*")) {
                    if (currentMonth != -1) {
                        luckyInfoMap.put(currentMonth, currentAdvice.toString().trim());
                    }

                    String[] parts = line.split(":", 2);
                    try {
                        currentMonth = Integer.parseInt(parts[0].trim());
                        currentAdvice = new StringBuilder(parts[1].trim() + "\n");
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга числа: " + parts[0] + ". Строка пропущена.");
                    }
                } else {
                    currentAdvice.append(line).append("\n");
                }
            }

            if (currentMonth != -1) {
                luckyInfoMap.put(currentMonth, currentAdvice.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения числа Ангела-Хранителя
    public String getInfoLuckyNumber(int month) {
        return luckyInfoMap.getOrDefault(month, "Нет такого числа, повторите попытку");
    }
}
