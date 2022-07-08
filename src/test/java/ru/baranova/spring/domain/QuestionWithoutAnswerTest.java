package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test class QuestionOneAnswer")
class QuestionWithoutAnswerTest {

    private String question;

    @BeforeEach
    void setUp() {
        question = "Question First";
    }

    @Test
    void shouldGetNullOptionAnswers() {
        Question actual = new QuestionWithoutAnswer(question);
        Assertions.assertNull(actual.getOptionAnswers());
    }

    @Test
    void shouldGetNullRightAnswer() {
        Question actual = new QuestionWithoutAnswer(question);
        Assertions.assertNull(actual.getRightAnswer());
    }

}

