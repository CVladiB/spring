package ru.baranova.spring.dao;

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
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Test class QuestionDaoCsv")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:questionDaoCsvTest.properties")
@ContextConfiguration(classes = {QuestionDaoCsv.class})
public class QuestionDaoCsvTest {
    @Autowired
    private QuestionDaoCsv questionDaoCsv;
    @MockBean
    private ReaderDao readerDaoFile;
    @Value("${test.bean.questionDaoCsv.initialStringQuestionWithOptionAnswers}")
    private String initialStringQuestionWithOptionAnswers;
    @Value("${test.bean.questionDaoCsv.initialStringQuestionOneAnswer}")
    private String initialStringQuestionOneAnswer;
    @Value("${test.bean.questionDaoCsv.initialStringQuestionWithoutAnswer}")
    private String initialStringQuestionWithoutAnswer;
    @Value("${test.bean.questionDaoCsv.question}")
    private String question;
    @Value("${test.bean.questionDaoCsv.rightAnswer}")
    private String rightAnswer;
    @Value("${test.bean.questionDaoCsv.optionOne}")
    private String optionOne;
    @Value("${test.bean.questionDaoCsv.optionTwo}")
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
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionWithOptionAnswers")
    void shouldHaveCorrectLoadQuestion_QuestionWithOptionAnswers() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(initialStringQuestionWithOptionAnswers.getBytes(StandardCharsets.UTF_8)));
        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = List.of(questionWithOptionAnswers);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionOneAnswer")
    void shouldHaveCorrectLoadQuestion_QuestionOneAnswer() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(initialStringQuestionOneAnswer.getBytes()));
        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = List.of(questionOneAnswer);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionWithoutAnswer")
    void shouldHaveCorrectLoadQuestion_QuestionWithoutAnswer() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(initialStringQuestionWithoutAnswer.getBytes()));
        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = List.of(questionWithoutAnswer);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct empty List Question")
    void shouldHaveEmptyList_NullByPath() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(null);
        Assertions.assertEquals(questionDaoCsv.loadQuestion(), new ArrayList<>());
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct empty List Question")
    void shouldHaveEmptyList_IncorrectPath() {
        questionDaoCsv.setPath("IncorrectPath");
        Assertions.assertEquals(questionDaoCsv.loadQuestion(), new ArrayList<>());
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List with QuestionWithOptionAnswers")
    void shouldHaveCorrectParseStrings_QuestionWithOptionAnswers() {
        List<String> initialStringList = List.of(initialStringQuestionWithOptionAnswers);
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = List.of(questionWithOptionAnswers);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List QuestionOneAnswer")
    void shouldHaveCorrectParseStrings_QuestionOneAnswer() {
        List<String> initialStringList = List.of(initialStringQuestionOneAnswer);
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = List.of(questionOneAnswer);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List QuestionWithoutAnswer")
    void shouldHaveCorrectParseStrings_QuestionWithoutAnswer() {
        List<String> initialStringList = List.of(initialStringQuestionWithoutAnswer);
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = List.of(questionWithoutAnswer);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct empty List")
    void shouldHaveCorrectParseStrings_EmptyList() {
        List<String> initialStringList = new ArrayList<>();
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = new ArrayList<>();

        Assertions.assertEquals(expected, actual);
    }
}
