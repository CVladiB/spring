package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

@DisplayName("Test class QuestionOneAnswer")
@TestPropertySource("classpath:questionWithoutAnswerTest.properties")
class QuestionWithoutAnswerTest {
    @Value("${test.bean.questionWithoutAnswer.question}")
    private String question;

    @Test
    void shouldGetNullOptionAnswers() {
        Question questionOneAnswer = new QuestionWithoutAnswer(question);
        Assertions.assertEquals(null, questionOneAnswer.getOptionAnswers());
    }

    @Test
    void shouldGetNullRightAnswer() {
        Question questionOneAnswer = new QuestionWithoutAnswer(question);
        Assertions.assertEquals(null, questionOneAnswer.getRightAnswer());
    }

}
