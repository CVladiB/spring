package ru.baranova.spring.services.data.visitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.data.visitor.config.QuestionElementVisitorTestConfig;

import java.util.List;


@Slf4j
@DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor")
@ActiveProfiles("question-visitor")
@SpringBootTest(classes = {QuestionElementVisitorTestConfig.class, ComponentScanStopConfig.class})
class QuestionElementCheckCorrectAnswerVisitorTest {
    @Autowired
    private QuestionElementVisitorTestConfig config;
    @Autowired
    private QuestionElementCheckCorrectAnswerVisitor questionElementCheckCorrectAnswerVisitor;
    @Autowired
    private CheckService checkServiceImpl;
    private QuestionWithOptionAnswers questionWithOptionAnswers;
    private QuestionOneAnswer questionOneAnswer;
    private QuestionWithoutAnswer questionWithoutAnswer;

    @Autowired
    private ApplicationContext context;

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
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check right answer by questionWithOptionAnswers")
    void shouldHaveTrueAnswer_QuestionWithOptionAnswers() {
        String inputAnswer = "2";
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(inputAnswer, min, max)).thenReturn(Integer.parseInt(inputAnswer));
        Assertions.assertTrue(questionElementCheckCorrectAnswerVisitor.visit(questionWithOptionAnswers, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check right answer by questionOneAnswer")
    void shouldHaveTrueAnswer_QuestionOneAnswer() {
        String inputAnswer = config.getRightAnswer();
        Assertions.assertTrue(questionElementCheckCorrectAnswerVisitor.visit(questionOneAnswer, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check right answer by questionWithoutAnswer")
    void shouldHaveTrueAnswer_QuestionWithoutAnswer() {
        String inputAnswer = "some answer";
        Assertions.assertTrue(questionElementCheckCorrectAnswerVisitor.visit(questionWithoutAnswer, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check wrong answer by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers() {
        String inputAnswer = "1";
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(inputAnswer, min, max)).thenReturn(Integer.parseInt(inputAnswer));
        Assertions.assertFalse(questionElementCheckCorrectAnswerVisitor.visit(questionWithOptionAnswers, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check wrong answer by questionOneAnswer")
    void shouldHaveFalseAnswer_QuestionOneAnswer() {
        String inputAnswer = config.getRightAnswer() + " ";
        Assertions.assertFalse(questionElementCheckCorrectAnswerVisitor.visit(questionOneAnswer, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check false if  NumberFormatException by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers_NumberFormatException() {
        String inputAnswer = "rightAnswer";
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(inputAnswer, min, max)).thenReturn(-1);
        Assertions.assertFalse(questionElementCheckCorrectAnswerVisitor.visit(questionWithOptionAnswers, inputAnswer));
    }

    @Test
    @DisplayName("Test class QuestionElementCheckCorrectAnswerVisitor, method visit, check false if IndexOutOfBoundsException by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers_IndexOutOfBoundsException() {
        String inputAnswer = "-1";
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(inputAnswer, min, max)).thenReturn(-1);

        Assertions.assertFalse(questionElementCheckCorrectAnswerVisitor.visit(questionWithOptionAnswers, inputAnswer));
    }
}

