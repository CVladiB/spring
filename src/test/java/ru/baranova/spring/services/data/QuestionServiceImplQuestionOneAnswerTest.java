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
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.services.data.config.QuestionServiceImplTestConfig;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

@Slf4j
@DisplayName("Test class QuestionServiceImpl")
@ActiveProfiles("question-service-impl")
@SpringBootTest(classes = {QuestionServiceImplTestConfig.class, ComponentScanStopConfig.class})
class QuestionServiceImplQuestionOneAnswerTest {
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

    private QuestionOneAnswer questionOneAnswer;

    @BeforeEach
    void setUp() {
        questionOneAnswer = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void QuestionOneAnswer_printQuestion_IsCorrect() {
        Mockito.when(questionElementPrintQuestionVisitorMock.visit(questionOneAnswer))
                .thenReturn("Question First\n");
        String expected = "Question First\n\r\n";
        questionServiceImplString.printQuestion(questionOneAnswer);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void QuestionOneAnswer_setAndGetAnswer_IsCorrect() {
        String expected = questionOneAnswer.getRightAnswer().getAnswer();
        Mockito.when(questionElementSetAndGetAnswerVisitorMock.visit(questionOneAnswer))
                .thenReturn(expected);
        String actual = questionServiceImplString.setAndGetAnswer(questionOneAnswer);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void QuestionOneAnswer_checkCorrectAnswer_IsCorrect() {
        String inputAnswer = questionOneAnswer.getRightAnswer().getAnswer();
        Mockito.when(questionElementCheckCorrectAnswerVisitorMock.visit(questionOneAnswer, inputAnswer))
                .thenReturn(true);
        boolean actual = questionServiceImplString.checkCorrectAnswer(questionOneAnswer, inputAnswer);
        Assertions.assertTrue(actual);
    }

}

