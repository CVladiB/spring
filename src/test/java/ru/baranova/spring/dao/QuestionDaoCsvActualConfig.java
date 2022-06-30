package ru.baranova.spring.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;


@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "test")
public class QuestionDaoCsvActualConfig {

    private String initialStringQuestionWithOptionAnswers;
    private String initialStringQuestionOneAnswer;
    private String initialStringQuestionWithoutAnswer;
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;
}
