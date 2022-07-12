package ru.baranova.spring.services.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.data.config.QuestionServiceImplTestConfig;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

@Slf4j
@DisplayName("Test class QuestionServiceImpl")
@ActiveProfiles("question-service-impl")
@SpringBootTest(classes = {QuestionServiceImplTestConfig.class, ComponentScanStopConfig.class})
class QuestionServiceImplQuestionWithoutAnswerTest {
    @Autowired
    private QuestionServiceImplTestConfig config;
    @Autowired
    private QuestionService questionServiceImplString;
    @Autowired
    private QuestionElementPrintQuestionVisitor questionElementPrintQuestionVisitorMock;
    @Autowired
    private QuestionElementSetAndGetAnswerVisitor questionElementSetAndGetAnswerVisitorMock;
    @Autowired
    private QuestionElementCheckCorrectAnswerVisitor questionElementCheckCorrectAnswerVisitorMock;

    private QuestionWithoutAnswer questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionWithoutAnswer = new QuestionWithoutAnswer(config.getQuestion());
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, questionWithoutAnswer")
    void questionWithoutAnswer_printQuestion_IsCorrect() {
        Mockito.when(questionElementPrintQuestionVisitorMock.visit(questionWithoutAnswer)).thenReturn("Question First\n");
        String expected = "Question First\n\r\n";
        questionServiceImplString.printQuestion(questionWithoutAnswer);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method setAndGetAnswer, questionWithoutAnswer")
    void questionWithoutAnswer_setAndGetAnswer_IsCorrect() {
        Mockito.when(questionElementSetAndGetAnswerVisitorMock.visit(questionWithoutAnswer)).thenReturn("Some answer");
        String expected = "Some answer";
        String actual = questionServiceImplString.setAndGetAnswer(questionWithoutAnswer);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrectAnswer, questionWithoutAnswer")
    void questionWithoutAnswer_checkCorrectAnswer_IsCorrect() {
        String answer = "1";
        Mockito.when(questionElementCheckCorrectAnswerVisitorMock.visit(questionWithoutAnswer, answer)).thenReturn(Boolean.TRUE);
        boolean actual = questionServiceImplString.checkCorrectAnswer(questionWithoutAnswer, answer);
        Assertions.assertTrue(actual);
    }

}

