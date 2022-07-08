package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test class QuestionOneAnswer")
class QuestionOneAnswerTest {
    private String question = "Question First";
    private String rightAnswer = "Answer Second";

    @BeforeEach
    void setUp() {
        question = "Question First";
        rightAnswer = "Answer Second";
    }

    @Test
    void shouldGetNullOptionAnswers() {
        Question actual = new QuestionOneAnswer(question, new Answer(rightAnswer));
        Assertions.assertNull(actual.getOptionAnswers());
    }

}
