package ru.baranova.spring.services.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-service-impl")
public class QuestionServiceImplTestConfig {
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;
}
