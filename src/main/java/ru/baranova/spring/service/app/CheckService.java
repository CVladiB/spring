package ru.baranova.spring.service.app;

import java.util.stream.Stream;

public interface CheckService {
    boolean isCorrectSymbolsInInputString(String str, int min, int max);

    boolean isCorrectInputString(String str, int min, int max);

    boolean isCorrectInputInt(Integer input);

    <T> boolean isInputExist(T inputStr, Stream<T> existStr, Boolean shouldExist);

    String returnNullField(String str);
}
