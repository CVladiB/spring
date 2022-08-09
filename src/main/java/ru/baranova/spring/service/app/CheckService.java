package ru.baranova.spring.service.app;

public interface CheckService {
    boolean isCorrectSymbolsInInputString(String str, int min, int max);

    boolean isCorrectInputString(String str, int min, int max);

    boolean isCorrectInputInt(Integer input);

    boolean isAllFieldsNotNull(Object obj);
}
