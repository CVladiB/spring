package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;

import java.util.List;

@DisplayName("Test class QuestionServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:questionServiceImplTest.properties")
@ContextConfiguration(classes = {QuestionServiceImpl.class})
class QuestionServiceImplTest {
    @Autowired
    private QuestionService questionServiceImpl;
    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private OutputDao outputDaoConsole;
    @Value("${test.bean.questionServiceImplTest.question}")
    private String question;
    @Value("${test.bean.questionServiceImplTest.rightAnswer}")
    private String rightAnswer;
    @Value("${test.bean.questionServiceImplTest.optionOne}")
    private String optionOne;
    @Value("${test.bean.questionServiceImplTest.optionTwo}")
    private String optionTwo;
    private Question questionWithOptionAnswers;
    private Question questionOneAnswer;
    private Question questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers(question, new Answer(rightAnswer), List.of(new Option(optionOne), new Option(optionTwo)));
        questionOneAnswer = new QuestionOneAnswer(question, new Answer(rightAnswer));
        questionWithoutAnswer = new QuestionWithoutAnswer(question);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer by questionWithOptionAnswers")
    void shouldHaveTrueAnswer_QuestionWithOptionAnswers() {
        Assertions.assertTrue(questionServiceImpl.checkCorrectAnswer(questionWithOptionAnswers, "2"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer by questionOneAnswer")
    void shouldHaveTrueAnswer_QuestionOneAnswer() {
        Assertions.assertTrue(questionServiceImpl.checkCorrectAnswer(questionOneAnswer, rightAnswer));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer by questionWithoutAnswer")
    void shouldHaveTrueAnswer_QuestionWithoutAnswer() {
        Assertions.assertTrue(questionServiceImpl.checkCorrectAnswer(questionWithoutAnswer, "some answer"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check wrong answer by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers() {
        Assertions.assertFalse(questionServiceImpl.checkCorrectAnswer(questionWithOptionAnswers, "1"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check wrong answer by questionOneAnswer")
    void shouldHaveFalseAnswer_QuestionOneAnswer() {
        Assertions.assertFalse(questionServiceImpl.checkCorrectAnswer(questionOneAnswer, rightAnswer + " "));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check false if  NumberFormatException by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers_NumberFormatException() {
        Assertions.assertFalse(questionServiceImpl.checkCorrectAnswer(questionWithOptionAnswers, "rightAnswer"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check false if IndexOutOfBoundsException by questionWithOptionAnswers")
    void shouldHaveFalseAnswer_QuestionWithOptionAnswers_IndexOutOfBoundsException() {
        Assertions.assertFalse(questionServiceImpl.checkCorrectAnswer(questionWithOptionAnswers, "-1"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check get answer by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers() {
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithOptionAnswers);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check answer after big number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_NonexistentNumber_Big() {
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine())
                .thenReturn(String.valueOf(questionWithOptionAnswers.getOptionAnswers().size() + 10))
                .thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithOptionAnswers);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check answer after small number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_NonexistentNumber_Small() {
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("0").thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithOptionAnswers);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check answer after small number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_Symbol() {
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("rightAnswer").thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithOptionAnswers);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check get answer by questionOneAnswer")
    void shouldHaveAnswer_QuestionOneAnswer() {
        String expected = String.valueOf(questionOneAnswer.getRightAnswer());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionOneAnswer);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check answer after small answer by questionOneAnswer")
    void shouldHaveAnswer_QuestionOneAnswer_SmallAnswer() {
        String expected = String.valueOf(questionOneAnswer.getRightAnswer());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("")
                .thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionOneAnswer);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check get answer by questionWithoutAnswer")
    void shouldHaveAnswer_QuestionWithoutAnswer() {
        String expected = "rightAnswer";
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithoutAnswer);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, check answer after small answer by questionWithoutAnswer")
    void shouldHaveAnswer_QuestionWithoutAnswer_SmallAnswer() {
        String expected = "rightAnswer";
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("")
                .thenReturn(expected);
        String actual = questionServiceImpl.getAnswer(questionWithoutAnswer);
        Assertions.assertEquals(expected, actual);
    }
}