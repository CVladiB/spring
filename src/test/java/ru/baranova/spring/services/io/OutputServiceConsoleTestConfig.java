package ru.baranova.spring.services.io;

import lombok.Getter;
import lombok.Setter;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.LocaleService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Setter
@TestConfiguration
class OutputServiceConsoleTestConfig {
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
    public LocaleService localeServiceImpl() {
        return Mockito.mock(LocaleService.class);
    }

    @Bean
    public OutputService outputServiceConsoleString(OutputDao outputDaoConsoleString, LocaleService localeServiceImpl) {
        return new OutputServiceConsole(outputDaoConsoleString, localeServiceImpl);
    }

}