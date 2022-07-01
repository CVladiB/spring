package ru.baranova.spring.services.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.QuestionService;
import ru.baranova.spring.services.QuestionServiceImpl;

import java.io.ByteArrayOutputStream;

@Slf4j
@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-service-impl")
public class QuestionServiceImplTestConfig {
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;

    private ByteArrayOutputStream outputStream;

    @Bean
    public QuestionService questionServiceImplPrint(InputDao inputDaoReader, OutputDao outputDaoConsolePrint) {
        return new QuestionServiceImpl(inputDaoReader, outputDaoConsolePrint);
    }

    @Bean
    public OutputDao outputDaoConsolePrint() {
        outputStream = new ByteArrayOutputStream();
        return new OutputDaoConsole(outputStream);
    }

}
