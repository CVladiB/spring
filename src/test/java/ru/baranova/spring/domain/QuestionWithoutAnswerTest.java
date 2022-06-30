package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DisplayName("Test class QuestionOneAnswer")
@TestPropertySource("classpath:questionWithoutAnswerTest.properties")
@ContextConfiguration(classes = {QuestionWithoutAnswer.class})
class QuestionWithoutAnswerTest {
    @Autowired
    QuestionWithoutAnswer questionWithoutAnswer;
    @Value("${test.bean.questionWithoutAnswer.question}")
    private String question;

    @Test
    void shouldGetNullOptionAnswers () {
        Question questionOneAnswer = new QuestionWithoutAnswer(question);
        Assertions.assertEquals(questionOneAnswer.getOptionAnswers(),questionOneAnswer.getOptionAnswers());
    }

    @Test
    void shouldGetNullRightAnswer () {
        Question questionOneAnswer = new QuestionWithoutAnswer(question);
        Assertions.assertEquals(questionOneAnswer.getRightAnswer(),questionOneAnswer.getRightAnswer());
    }

}
