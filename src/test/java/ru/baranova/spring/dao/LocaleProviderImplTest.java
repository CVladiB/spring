package ru.baranova.spring.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.dao.config.LocaleProviderImplTestConfig;
import ru.baranova.spring.domain.LanguageDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ActiveProfiles("locale-provider")
@SpringBootTest(classes = {LocaleProviderImplTestConfig.class, ComponentScanStopConfig.class})
class LocaleProviderImplTest {

    @Autowired
    private LocaleProvider testLocaleProviderImpl;

    private LanguageDescription lRU;
    private LanguageDescription lBY;
    private List<LanguageDescription> languages;

    @BeforeEach
    void setUp() {
        LanguageDescription l1 = new LanguageDescription("ru-RU", "Ру", "Русский", "classpath:questionnaire_ru.csv");
        LanguageDescription l2 = new LanguageDescription("en-EN", "En", "English", "classpath:questionnaire_en.csv");
        languages = List.of(l1, l2);
        lRU = new LanguageDescription("ru-RU", "Ру", "Русский", "classpath:questionnaire_ru.csv");
        lBY = new LanguageDescription("ru-BY", "BY", "BY", "classpath:questionnaire_ru_BY.csv");
    }

    @AfterEach
    void tearDown() {
        testLocaleProviderImpl.setLanguageDescription(lRU);
    }

    @Test
    void getLocale() {
        testLocaleProviderImpl.setLanguageDescription(lRU);
        Locale actual = testLocaleProviderImpl.getLocale();
        Locale expected = Locale.forLanguageTag(lRU.getLanguage());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getLanguages() {
        ArrayList<LanguageDescription> actual = new ArrayList<>(languages);
        List<LanguageDescription> expected = testLocaleProviderImpl.getLanguages();
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void getLanguageDescription() {
        LanguageDescription actual = testLocaleProviderImpl.getLanguageDescription();
        LanguageDescription expected = lRU;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void setLanguageDescription() {
        testLocaleProviderImpl.setLanguageDescription(lBY);
        LanguageDescription actual = testLocaleProviderImpl.getLanguageDescription();
        LanguageDescription expected = lBY;
        Assertions.assertEquals(expected, actual);
    }
}