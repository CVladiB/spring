package ru.baranova.spring.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.domain.config.QuestionOneAnswerTestConfig;


@DisplayName("Test class QuestionOneAnswer")
@ActiveProfiles("questiononeanswertest")
@SpringBootTest(classes = {QuestionOneAnswerTestConfig.class})
class QuestionOneAnswerTest {
    @Autowired
    private QuestionOneAnswerTestConfig config;

    @Test
    void shouldGetNullOptionAnswers() {
        Question actual = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
        Assertions.assertNull(actual.getOptionAnswers());
    }

}
