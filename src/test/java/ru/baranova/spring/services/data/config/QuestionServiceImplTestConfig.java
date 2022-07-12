package ru.baranova.spring.services.data.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.data.QuestionService;
import ru.baranova.spring.services.data.QuestionServiceImpl;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

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
    private ByteArrayOutputStream out;
    private PrintWriter writer;

    @Bean
    public OutputDao outputDaoConsoleString() {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new OutputDaoConsole(out);
    }

    @Bean
    public QuestionService questionServiceImplString(OutputDao outputDaoConsoleString) {
        return new QuestionServiceImpl(outputDaoConsoleString);
    }

    @Bean
    public QuestionElementPrintQuestionVisitor questionElementPrintQuestionVisitorMock() {
        return Mockito.mock(QuestionElementPrintQuestionVisitor.class);
    }

    @Bean
    public QuestionElementSetAndGetAnswerVisitor questionElementSetAndGetAnswerVisitorMock() {
        return Mockito.mock(QuestionElementSetAndGetAnswerVisitor.class);
    }

    @Bean
    public QuestionElementCheckCorrectAnswerVisitor questionElementCheckCorrectAnswerVisitorMock() {
        return Mockito.mock(QuestionElementCheckCorrectAnswerVisitor.class);
    }

}
