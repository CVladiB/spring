package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.domain.config.QuestionWithoutAnswerTestConfig;

@DisplayName("Test class QuestionOneAnswer")
@ActiveProfiles("questionwithoutanswertest")
@SpringBootTest(classes = {QuestionWithoutAnswerTestConfig.class})
class QuestionWithoutAnswerTest {
    @Autowired
    private QuestionWithoutAnswerTestConfig config;


    @Test
    void shouldGetNullOptionAnswers() {
        Question actual = new QuestionWithoutAnswer(config.getQuestion());
        Assertions.assertNull(actual.getOptionAnswers());
    }

    @Test
    void shouldGetNullRightAnswer() {
        Question actual = new QuestionWithoutAnswer(config.getQuestion());
        Assertions.assertNull(actual.getRightAnswer());
    }

}

