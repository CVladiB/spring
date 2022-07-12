package ru.baranova.spring.services.data.visitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.data.visitor.config.QuestionElementVisitorTestConfig;

import java.util.List;

@Slf4j
@DisplayName("Test class QuestionElementPrintQuestionVisitor")
@ActiveProfiles("question-visitor")
@SpringBootTest(classes = {QuestionElementVisitorTestConfig.class, ComponentScanStopConfig.class})
class QuestionElementPrintQuestionVisitorTest {
    @Autowired
    private QuestionElementPrintQuestionVisitor questionElementPrintQuestionVisitor;
    @Autowired
    private QuestionElementVisitorTestConfig config;
    private QuestionWithOptionAnswers questionWithOptionAnswers;
    private QuestionOneAnswer questionOneAnswer;
    private QuestionWithoutAnswer questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers(
                config.getQuestion(),
                new Answer(config.getRightAnswer()),
                List.of(new Option(config.getOptionOne()), new Option(config.getOptionTwo())));
        questionOneAnswer = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
        questionWithoutAnswer = new QuestionWithoutAnswer(config.getQuestion());
    }

    @Test
    @DisplayName("Test class QuestionElementPrintQuestionVisitor, method visit, questionWithOptionAnswers")
    void shouldHave_QuestionWithOptionAnswers() {
        String actual = questionElementPrintQuestionVisitor.visit(questionWithOptionAnswers);
        String expected = "Question First\n" +
                "1) Answer First\n" +
                "2) Answer Second\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementPrintQuestionVisitor, method visit, questionOneAnswer")
    void shouldHave_QuestionOneAnswer() {
        String actual = questionElementPrintQuestionVisitor.visit(questionOneAnswer);
        String expected = "Question First\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementPrintQuestionVisitor, method visit, questionWithoutAnswer")
    void shouldHave_QuestionWithoutAnswer() {
        String actual = questionElementPrintQuestionVisitor.visit(questionWithoutAnswer);
        String expected = "Question First\n";
        Assertions.assertEquals(expected, actual);
    }
}

