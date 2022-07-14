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
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.services.data.config.QuestionServiceImplTestConfig;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

import java.util.List;

@Slf4j
@DisplayName("Test class QuestionServiceImpl")
@ActiveProfiles("question-service-impl")
@SpringBootTest(classes = {QuestionServiceImplTestConfig.class, ComponentScanStopConfig.class})
class QuestionServiceImplQuestionWithOptionAnswersTest {
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

    private QuestionWithOptionAnswers questionWithOptionAnswers;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers(
                config.getQuestion(),
                new Answer(config.getRightAnswer()),
                List.of(new Option(config.getOptionOne()), new Option(config.getOptionTwo())));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void QuestionWithOptionAnswers_printQuestion_IsCorrect() {
        Mockito.when(questionElementPrintQuestionVisitorMock.visit(questionWithOptionAnswers))
                .thenReturn("Question First\n" +
                        "1) Answer First\n" +
                        "2) Answer Second\n");
        String expected = "Question First\n" +
                "1) Answer First\n" +
                "2) Answer Second\n\r\n";
        questionServiceImplString.printQuestion(questionWithOptionAnswers);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void QuestionWithOptionAnswers_setAndGetAnswer_IsCorrect() {
        String expected = questionWithOptionAnswers.getRightAnswer().getAnswer();
        Mockito.when(questionElementSetAndGetAnswerVisitorMock.visit(questionWithOptionAnswers))
                .thenReturn(expected);
        String actual = questionServiceImplString.setAndGetAnswer(questionWithOptionAnswers);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void QuestionWithOptionAnswers_checkCorrectAnswer_IsCorrect() {
        String inputAnswer = questionWithOptionAnswers.getRightAnswer().getAnswer();
        Mockito.when(questionElementCheckCorrectAnswerVisitorMock.visit(questionWithOptionAnswers, inputAnswer))
                .thenReturn(true);
        boolean actual = questionServiceImplString.checkCorrectAnswer(questionWithOptionAnswers, inputAnswer);
        Assertions.assertTrue(actual);
    }
}

