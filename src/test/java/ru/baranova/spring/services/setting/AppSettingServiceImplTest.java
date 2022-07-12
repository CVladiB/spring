package ru.baranova.spring.services.setting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.LanguageDescription;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.setting.config.AppSettingServiceImplTestConfig;

import java.util.List;

@DisplayName("Test class AppSettingServiceImpl")
@SpringBootTest(classes = {AppSettingServiceImplTestConfig.class, ComponentScanStopConfig.class})
class AppSettingServiceImplTest {
    @Autowired
    private AppSettingService appSettingServiceImpl;

    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private LocaleProvider localeProviderImpl;
    @Autowired
    private AppSettingServiceImplTestConfig config;
    private List<LanguageDescription> listLD;
    private LanguageDescription ldRu;
    private LanguageDescription ldEn;

    @BeforeEach
    void setUp() {
        ldRu = new LanguageDescription("RU", "Рус", "Русский язык", "classpath:questionnaire_ru.csv");
        ldEn = new LanguageDescription("EN", "Engl", "English language", "classpath:questionnaire_en.csv");
        listLD = List.of(ldRu, ldEn);
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    @DisplayName("Test class AppSettingServiceImpl, method printOptionsOfLanguage")
    void printOptionsOfLanguage() {

        Assertions.assertTrue(false);
    }

    @Test
    @DisplayName("Test class AppSettingServiceImpl, method inputNumberOfLanguage, correct input")
    void inputCorrectNumberOfLanguage() {
        int expected = 1;
        int min = 0;
        int max = listLD.size();
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(Integer.toString(expected));
        Mockito.when(localeProviderImpl.getLanguages()).thenReturn(listLD);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(Integer.toString(expected), min, max)).thenReturn(expected);
        int actual = appSettingServiceImpl.inputNumberOfLanguage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class AppSettingServiceImpl, method inputNumberOfLanguage, incorrect input - big number")
    void inputIncorrectNumberOfLanguage_big() {
        int expected = listLD.size();
        int min = 0;
        int max = listLD.size();
        Mockito.lenient().when(inputDaoReader.inputLine())
                .thenReturn(Integer.toString(expected + 1))
                .thenReturn(Integer.toString(expected));
        Mockito.when(localeProviderImpl.getLanguages()).thenReturn(listLD);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(Integer.toString(expected + 1), min, max)).thenReturn(-1);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(Integer.toString(expected), min, max)).thenReturn(expected);
        int actual = appSettingServiceImpl.inputNumberOfLanguage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class AppSettingServiceImpl, method inputNumberOfLanguage, incorrect input - small number")
    void inputIncorrectNumberOfLanguage_small() {
        int expected = listLD.size();
        int min = 0;
        int max = listLD.size();
        Mockito.lenient().when(inputDaoReader.inputLine())
                .thenReturn(Integer.toString(0))
                .thenReturn(Integer.toString(expected));
        Mockito.when(localeProviderImpl.getLanguages()).thenReturn(listLD);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(Integer.toString(0), min, max)).thenReturn(-1);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(Integer.toString(expected), min, max)).thenReturn(expected);
        int actual = appSettingServiceImpl.inputNumberOfLanguage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class AppSettingServiceImpl, method setLanguage, print choose language")
    void setLanguage() {
        int index = 1;
        Mockito.when(localeProviderImpl.getLanguages()).thenReturn(listLD);
        Mockito.doAnswer(invocation -> {
            config.getWriter().println(listLD.get(index - 1).toString());
            return null;
        }).when(localeProviderImpl).setLanguageDescription(listLD.get(index - 1));

        appSettingServiceImpl.setLanguage(index);

        String expected = listLD.get(index - 1).toString();
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected + "\r\n", actual);
    }
}