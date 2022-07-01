package ru.baranova.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.config.QuestionServiceImplTestConfig;

import java.util.List;

@Slf4j
@DisplayName("Test class QuestionServiceImpl")
@ActiveProfiles("question-service-impl")
@SpringBootTest(classes = {QuestionServiceImplTestConfig.class})
class QuestionServiceImplTest {
    @Autowired
    private QuestionServiceImplTestConfig config;
    @Autowired
    private QuestionService questionServiceImpl;
    @Autowired
    private QuestionService questionServiceImplPrint;
    @MockBean
    private InputDao inputDaoReader;
    private Question questionWithOptionAnswers;
    private Question questionOneAnswer;
    private Question questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers(
                config.getQuestion(),
                new Answer(config.getRightAnswer()),
                List.of(new Option(config.getOptionOne()), new Option(config.getOptionTwo())));
        questionOneAnswer = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
        questionWithoutAnswer = new QuestionWithoutAnswer(config.getQuestion());
    }

    @AfterEach
    void tearDown() {
        config.getOutputStream().reset();
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, questionWithOptionAnswers")
    void shouldHave_QuestionWithOptionAnswers() {
        questionServiceImplPrint.printQuestion(questionWithOptionAnswers);
        String actual = config.getOutputStream().toString();
        String expected = "Question First\r\n" +
                "1) Answer First\n" +
                "2) Answer Second\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, questionOneAnswer")
    void shouldHave_QuestionOneAnswer() {
        questionServiceImplPrint.printQuestion(questionOneAnswer);
        String actual = config.getOutputStream().toString();
        String expected = "Question First\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, questionWithoutAnswer")
    void shouldHave_QuestionWithoutAnswer() {
        questionServiceImplPrint.printQuestion(questionWithoutAnswer);
        String actual = config.getOutputStream().toString();
        String expected = "Question First\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer by questionWithOptionAnswers")
    void shouldHaveTrueAnswer_QuestionWithOptionAnswers() {
        Assertions.assertTrue(questionServiceImpl.checkCorrectAnswer(questionWithOptionAnswers, "2"));
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer by questionOneAnswer")
    void shouldHaveTrueAnswer_QuestionOneAnswer() {
        Assertions.assertTrue(questionServiceImpl.checkCorrectAnswer(questionOneAnswer, config.getRightAnswer()));
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
        Assertions.assertFalse(questionServiceImpl.checkCorrectAnswer(questionOneAnswer, config.getRightAnswer() + " "));
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

