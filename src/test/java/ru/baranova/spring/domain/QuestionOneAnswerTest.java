package ru.baranova.spring.domain;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;


@DisplayName("Test class QuestionOneAnswer")
@ActiveProfiles("questiononeanswertest")
@SpringBootTest(classes = {QuestionOneAnswerTestConfig.class})
class QuestionOneAnswerTest {
    @Autowired
    private QuestionOneAnswer questionOneAnswer;
    @Autowired
    private QuestionOneAnswerTestConfig config;

    @Test
    void shouldGetNullOptionAnswers() {
        Question excepted = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
        Assertions.assertEquals(excepted.getOptionAnswers(), questionOneAnswer.getOptionAnswers());
    }

}

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-one-answer")
class QuestionOneAnswerTestConfig {
    private String question;
    private String rightAnswer;
}