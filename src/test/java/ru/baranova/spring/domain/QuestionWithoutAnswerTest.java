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
@ActiveProfiles("questionwithoutanswertest")
@SpringBootTest(classes = {QuestionWithoutAnswerTestConfig.class})
class QuestionWithoutAnswerTest {
    @Autowired
    private QuestionWithoutAnswer questionWithoutAnswer;
    @Autowired
    private QuestionWithoutAnswerTestConfig config;


    @Test
    void shouldGetNullOptionAnswers() {
        Question expected = new QuestionWithoutAnswer(config.getQuestion());
        Assertions.assertEquals(expected.getOptionAnswers(), questionWithoutAnswer.getOptionAnswers());
    }

    @Test
    void shouldGetNullRightAnswer() {
        Question expected = new QuestionWithoutAnswer(config.getQuestion());
        Assertions.assertEquals(expected.getOptionAnswers(), questionWithoutAnswer.getRightAnswer());
    }

}

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-without-answer")
class QuestionWithoutAnswerTestConfig {
    private String question;
}
