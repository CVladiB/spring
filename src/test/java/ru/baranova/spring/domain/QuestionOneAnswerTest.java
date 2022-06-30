package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DisplayName("Test class QuestionOneAnswer")
@TestPropertySource("classpath:questionOneAnswerTest.properties")
@ContextConfiguration(classes = {QuestionOneAnswer.class})
class QuestionOneAnswerTest {
    @Autowired
    QuestionOneAnswer questionOneAnswer;
    @Value("${test.bean.questionOneAnswer.question}")
    private String question;
    @Value("${test.bean.questionOneAnswer.rightAnswer}")
    private String rightAnswer;

    @Test
    void shouldGetNullOptionAnswers() {
        Question questionOneAnswer = new QuestionOneAnswer(question, new Answer(rightAnswer));
        Assertions.assertEquals(questionOneAnswer.getOptionAnswers(), questionOneAnswer.getOptionAnswers());
    }

}
