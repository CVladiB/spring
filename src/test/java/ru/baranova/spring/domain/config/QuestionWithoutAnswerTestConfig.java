package ru.baranova.spring.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-without-answer")
public class QuestionWithoutAnswerTestConfig {
    private String question;
}
