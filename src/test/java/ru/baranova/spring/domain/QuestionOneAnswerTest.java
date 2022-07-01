package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

@DisplayName("Test class QuestionOneAnswer")
@TestPropertySource("classpath:questionOneAnswerTest.properties")
class QuestionOneAnswerTest {
    @Value("${test.bean.questionOneAnswer.question}")
    private String question;
    @Value("${test.bean.questionOneAnswer.rightAnswer}")
    private String rightAnswer;

    @Test
    void shouldGetNullOptionAnswers() {
        Question questionOneAnswer = new QuestionOneAnswer(question, new Answer(rightAnswer));
        Assertions.assertEquals(null, questionOneAnswer.getOptionAnswers());
    }

}
