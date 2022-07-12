package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.services.config.LocaleServiceImplTestConfig;
import ru.baranova.spring.services.message.LocaleService;

import java.util.Locale;

@DisplayName("Test class LocaleServiceImpl")
@SpringBootTest(classes = {LocaleServiceImplTestConfig.class, ComponentScanStopConfig.class})
class LocaleServiceImplTest {
    @Autowired
    private LocaleService testLocaleServiceImpl;
    @Autowired
    private LocaleProvider testLocaleProviderImpl;

    @Test
    @DisplayName("Test class LocaleServiceImpl, method getMessage, ru message properties")
    void shouldHaveMessage_ru() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String expected = "Тестовое сообщение";
        String actual = testLocaleServiceImpl.getMessage("message");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class LocaleServiceImpl, method getMessage, en message properties")
    void shouldHaveMessage_en() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("en-EN"));
        String expected = "Test message";
        String actual = testLocaleServiceImpl.getMessage("message");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class LocaleServiceImpl, method getMessage, default (ru) message properties")
    void shouldHaveMessage_defaultRuIfNothing() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("bel-BU"));
        String expected = "Тестовое сообщение";
        String actual = testLocaleServiceImpl.getMessage("message");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class LocaleServiceImpl, method getMessage, message with arguments")
    void shouldHaveMessageWithArguments() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String expected = "Тестовое второе сообщение";
        String actual = testLocaleServiceImpl.getMessage("message-two", "второе");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class LocaleServiceImpl, method getMessage, error load")
    void shouldHaveError() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String expected = "Ошибка загрузки сообщения, обратитесь к администратору";
        String actual = testLocaleServiceImpl.getMessage("smth-test-key");
        Assertions.assertEquals(expected, actual);
    }

}