package ru.baranova.spring.services.data.visitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.data.visitor.config.QuestionElementVisitorTestConfig;

import java.util.List;


@Slf4j
@DisplayName("Test class QuestionElementSetAndGetAnswerVisitor")
@ActiveProfiles("question-visitor")
@SpringBootTest(classes = {QuestionElementVisitorTestConfig.class, ComponentScanStopConfig.class})
class QuestionElementSetAndGetAnswerVisitorTest {
    @Autowired
    private QuestionElementSetAndGetAnswerVisitor questionElementSetAndGetAnswerVisitor;
    @Autowired
    private QuestionElementVisitorTestConfig config;
    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    private CheckService checkServiceImpl;
    private QuestionWithOptionAnswers questionWithOptionAnswers;
    private QuestionOneAnswer questionOneAnswer;
    private QuestionWithoutAnswer questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionElementSetAndGetAnswerVisitor.setMinAnswerSymbol(config.getMinAnswerSymbol());
        questionWithOptionAnswers = new QuestionWithOptionAnswers(
                config.getQuestion(),
                new Answer(config.getRightAnswer()),
                List.of(new Option(config.getOptionOne()), new Option(config.getOptionTwo())));
        questionOneAnswer = new QuestionOneAnswer(config.getQuestion(), new Answer(config.getRightAnswer()));
        questionWithoutAnswer = new QuestionWithoutAnswer(config.getQuestion());
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check get answer by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers() {
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());

        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(expected, min, max)).thenReturn(Integer.parseInt(expected));

        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithOptionAnswers);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check answer after big number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_NonexistentNumber_Big() {
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine())
                .thenReturn(String.valueOf(questionWithOptionAnswers.getOptionAnswers().size() + 10))
                .thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(String.valueOf(questionWithOptionAnswers.getOptionAnswers().size() + 10), min, max)).thenReturn(-1);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(expected, min, max)).thenReturn(Integer.parseInt(expected));
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithOptionAnswers);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check answer after small number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_NonexistentNumber_Small() {
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("0").thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber("0", min, max)).thenReturn(-1);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(expected, min, max)).thenReturn(Integer.parseInt(expected));
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithOptionAnswers);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check answer after small number by questionWithOptionAnswers")
    void shouldHaveAnswer_QuestionWithOptionAnswers_Symbol() {
        int min = 0;
        int max = questionWithOptionAnswers.getOptionAnswers().size();
        String expected = String.valueOf(questionWithOptionAnswers.getOptionAnswers().size());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("rightAnswer").thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber("rightAnswer", min, max)).thenReturn(-1);
        Mockito.when(checkServiceImpl.checkCorrectInputNumber(expected, min, max)).thenReturn(Integer.parseInt(expected));
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithOptionAnswers);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method getAnswer, check get answer by questionOneAnswer")
    void shouldHaveAnswer_QuestionOneAnswer() {
        int min = 1;
        String expected = questionOneAnswer.getRightAnswer().getAnswer();
        Mockito.when(inputDaoReader.inputLine()).thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputStr(expected, min)).thenReturn(true);
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionOneAnswer);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check answer after small answer by questionOneAnswer")
    void shouldHaveAnswer_QuestionOneAnswer_SmallAnswer() {
        int min = 1;
        String expected = String.valueOf(questionOneAnswer.getRightAnswer());
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("")
                .thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputStr("", min)).thenReturn(false);
        Mockito.when(checkServiceImpl.checkCorrectInputStr(expected, min)).thenReturn(true);
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionOneAnswer);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check get answer by questionWithoutAnswer")
    void shouldHaveAnswer_QuestionWithoutAnswer() {
        int min = 1;
        String expected = "rightAnswer";
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputStr(expected, min)).thenReturn(true);
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithoutAnswer);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionElementSetAndGetAnswerVisitor, method visit, check answer after small answer by questionWithoutAnswer")
    void shouldHaveAnswer_QuestionWithoutAnswer_SmallAnswer() {
        int min = 1;
        String expected = "rightAnswer";
        Mockito.when(inputDaoReader.inputLine()).thenReturn("").thenReturn(expected);
        Mockito.when(checkServiceImpl.checkCorrectInputStr("", min)).thenReturn(false);
        Mockito.when(checkServiceImpl.checkCorrectInputStr(expected, min)).thenReturn(true);
        String actual = questionElementSetAndGetAnswerVisitor.visit(questionWithoutAnswer);

        Assertions.assertEquals(expected, actual);
    }
}


