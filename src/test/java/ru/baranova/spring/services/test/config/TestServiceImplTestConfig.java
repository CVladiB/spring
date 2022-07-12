package ru.baranova.spring.services.test.config;

import lombok.Getter;
import lombok.Setter;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.LocaleProviderImpl;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.QuestionDaoCsv;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.data.QuestionService;
import ru.baranova.spring.services.data.QuestionServiceImpl;
import ru.baranova.spring.services.data.UserService;
import ru.baranova.spring.services.data.UserServiceImpl;
import ru.baranova.spring.services.io.OutputService;
import ru.baranova.spring.services.io.OutputServiceConsole;
import ru.baranova.spring.services.message.LocaleService;
import ru.baranova.spring.services.message.LocaleServiceImpl;
import ru.baranova.spring.services.test.TestServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImplTestConfig {

    private int partRightAnswers;
    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;

    @Bean
    public OutputDao outputDaoConsoleString() {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new OutputDaoConsole(out);
    }

    @Bean
    public OutputService outputServiceConsoleString(OutputDao outputDaoConsoleString, LocaleService localeServiceImplString) {
        return new OutputServiceConsole(outputDaoConsoleString, localeServiceImplString);
    }

    @Bean
    public LocaleService localeServiceImplString(LocaleProvider localeProviderImplString, ReloadableResourceBundleMessageSource messageSource) {
        return new LocaleServiceImpl(localeProviderImplString, messageSource);
    }

    @Bean
    public LocaleProvider localeProviderImplString() {
        return Mockito.mock(LocaleProviderImpl.class);
    }

    @Bean
    public TestServiceImpl testServiceImplString(OutputService outputServiceConsoleString
            , QuestionDao questionDaoCsvMock
            , UserService userServiceImplMock
            , QuestionService questionServiceImplMock
    ) {
        TestServiceImpl testService = new TestServiceImpl(outputServiceConsoleString
                , questionDaoCsvMock
                , userServiceImplMock
                , questionServiceImplMock);
        testService.setPartRightAnswers(partRightAnswers);
        return testService;
    }

    @Bean
    public QuestionDao questionDaoCsvMock() {
        return Mockito.mock(QuestionDaoCsv.class);
    }

    @Bean
    public UserService userServiceImplMock() {
        return Mockito.mock(UserServiceImpl.class);
    }

    @Bean
    public QuestionService questionServiceImplMock() {
        return Mockito.mock(QuestionServiceImpl.class);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
    }
}
