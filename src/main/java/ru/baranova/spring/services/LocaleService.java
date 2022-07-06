package ru.baranova.spring.services;

import ru.baranova.spring.annotation.MethodArg;

public interface LocaleService {

    String getMessage(String keyMessage);

    @MethodArg
    String getMessage(String keyMessage, Object... args);

    void chooseLanguage();

}
