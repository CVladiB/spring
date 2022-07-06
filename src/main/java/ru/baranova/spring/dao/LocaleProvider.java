package ru.baranova.spring.dao;

import ru.baranova.spring.domain.LanguageDescription;

import java.util.List;
import java.util.Locale;

public interface LocaleProvider {

    List<LanguageDescription> getLanguages();

    LanguageDescription getLanguageDescription();

    void setLanguageDescription(LanguageDescription languageDescription);

    Locale getLocale();
}
