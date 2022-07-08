package ru.baranova.spring.services;

public interface CheckService {
    int checkCorrectInputNumber(String str, int min, int max);

    boolean checkCorrectInputStr(String str, int min);
}
