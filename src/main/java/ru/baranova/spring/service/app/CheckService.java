package ru.baranova.spring.service.app;

import java.util.function.Supplier;

public interface CheckService {
    boolean isCorrectSymbolsInInputString(String str, int min, int max);

    boolean isCorrectInputString(String str, int min, int max);

    boolean isCorrectInputInt(Integer input);

    boolean isAllFieldsNotNull(Object obj);

    boolean checkExist(Supplier<Boolean> supplier);

    boolean checkIfNotExist(Supplier<Boolean> supplier);
}
