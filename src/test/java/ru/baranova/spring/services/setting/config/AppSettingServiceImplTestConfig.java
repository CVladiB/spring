package ru.baranova.spring.services.setting.config;

import lombok.Getter;
import lombok.Setter;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;
import ru.baranova.spring.services.io.OutputServiceConsole;
import ru.baranova.spring.services.setting.AppSettingService;
import ru.baranova.spring.services.setting.AppSettingServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Setter
@TestConfiguration
public class AppSettingServiceImplTestConfig {
    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;

    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private LocaleProvider localeProviderImpl;


    @Bean
    public OutputDao outputDaoConsoleString() {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new OutputDaoConsole(out);
    }

    @Bean
    public OutputService outputServiceConsoleString() {
        return Mockito.mock(OutputServiceConsole.class);
    }

    @Bean
    public AppSettingService appSettingServiceImpl(
            OutputDao outputDaoConsoleString
            , InputDao inputDaoReader
            , CheckService checkServiceImpl
            , LocaleProvider localeProviderImpl
            , OutputService outputServiceConsole) {
        return new AppSettingServiceImpl(outputDaoConsoleString
                , inputDaoReader
                , checkServiceImpl
                , localeProviderImpl
                , outputServiceConsole);
    }

}
