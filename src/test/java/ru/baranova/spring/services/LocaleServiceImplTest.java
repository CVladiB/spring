package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.services.config.LocaleServiceImplTestConfig;

import java.util.Locale;

@SpringBootTest(classes = {LocaleServiceImplTestConfig.class})
class LocaleServiceImplTest {
    @Autowired
    private LocaleService testLocaleServiceImpl;
    @Autowired
    private LocaleProvider testLocaleProviderImpl;

    @Test
    void getMessage() {
        Mockito.when(testLocaleProviderImpl.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String expected = "Тестовое сообщение";
        String actual = testLocaleServiceImpl.getMessage("message");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetMessage() {
    }
}