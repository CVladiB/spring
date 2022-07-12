package ru.baranova.spring.services.setting;

public interface AppSettingService {
    void chooseLanguage();

    void printOptionsOfLanguage();

    int inputNumberOfLanguage();

    void setLanguage(int indexLanguage);
}
