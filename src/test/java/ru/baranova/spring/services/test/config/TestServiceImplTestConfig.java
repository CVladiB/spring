package ru.baranova.spring.services.test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.LocaleService;
import ru.baranova.spring.services.data.QuestionService;
import ru.baranova.spring.services.data.QuestionServiceImpl;
import ru.baranova.spring.services.data.UserService;
import ru.baranova.spring.services.io.OutputService;
import ru.baranova.spring.services.io.OutputServiceConsole;
import ru.baranova.spring.services.lang.AppSettingService;
import ru.baranova.spring.services.test.TestService;
import ru.baranova.spring.services.test.TestServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImplTestConfig {
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
    public OutputService outputServiceConsoleString(OutputDao outputDaoConsoleString, LocaleService localeServiceImpl) {
        return new OutputServiceConsole(outputDaoConsoleString, localeServiceImpl);
    }

    @Bean
    public TestService testServiceImplString(OutputService outputServiceConsoleString
            , QuestionDao questionDaoCsv
            , UserService userServiceImpl
            , QuestionService questionServiceImpl
            , AppSettingService chooseAppSettingServiceImpl) {
        return new TestServiceImpl(outputServiceConsoleString, questionDaoCsv, userServiceImpl, questionServiceImpl, chooseAppSettingServiceImpl);
    }


//    @Bean
//    public QuestionService questionServiceImplString(InputDao inputDaoReader
//            , OutputDao outputDaoConsoleString
//            , OutputService outputServiceConsoleString
//            , CheckService checkServiceImpl) {
//        return new QuestionServiceImpl(inputDaoReader, outputDaoConsoleString, outputServiceConsoleString, checkServiceImpl);
//    }

}
