package ru.baranova.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("questiondaocsv")
@SpringBootTest(classes = {QuestionDaoCsvActualConfig.class})
public class QuestionDaoCsvTest {
    @Autowired
    private QuestionDaoCsv questionDaoCsv;
    @MockBean
    private ReaderDao readerDaoFile;

    @Autowired
    private QuestionDaoCsvActualConfig questionDaoCsvActualConfig;
    private Question questionWithOptionAnswers;
    private Question questionOneAnswer;
    private Question questionWithoutAnswer;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers(questionDaoCsvActualConfig.getQuestion()
                , new Answer(questionDaoCsvActualConfig.getRightAnswer())
                , List.of(new Option(questionDaoCsvActualConfig.getOptionOne())
                , new Option(questionDaoCsvActualConfig.getOptionTwo())));
        questionOneAnswer = new QuestionOneAnswer(questionDaoCsvActualConfig.getQuestion(), new Answer(questionDaoCsvActualConfig.getRightAnswer()));
        questionWithoutAnswer = new QuestionWithoutAnswer(questionDaoCsvActualConfig.getQuestion());
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionWithOptionAnswers")
    void shouldHaveCorrectLoadQuestion_QuestionWithOptionAnswers() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(questionDaoCsvActualConfig.getInitialStringQuestionWithOptionAnswers().getBytes(StandardCharsets.UTF_8)));
        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = List.of(questionWithOptionAnswers);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionOneAnswer")
    void shouldHaveCorrectLoadQuestion_QuestionOneAnswer() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(questionDaoCsvActualConfig.getInitialStringQuestionOneAnswer().getBytes()));
        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = List.of(questionOneAnswer);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List QuestionWithoutAnswer")
    void shouldHaveCorrectLoadQuestion_QuestionWithoutAnswer() {
        Mockito.when(readerDaoFile.getResource(questionDaoCsv.getPath()))
                .thenReturn(new ByteArrayInputStream(questionDaoCsvActualConfig.getInitialStringQuestionWithoutAnswer().getBytes()));
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
        List<String> initialStringList = List.of(questionDaoCsvActualConfig.getInitialStringQuestionWithOptionAnswers());
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = List.of(questionWithOptionAnswers);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List QuestionOneAnswer")
    void shouldHaveCorrectParseStrings_QuestionOneAnswer() {
        List<String> initialStringList = List.of(questionDaoCsvActualConfig.getInitialStringQuestionOneAnswer());
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);
        List<Question> actual = List.of(questionOneAnswer);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List QuestionWithoutAnswer")
    void shouldHaveCorrectParseStrings_QuestionWithoutAnswer() {
        List<String> initialStringList = List.of(questionDaoCsvActualConfig.getInitialStringQuestionWithoutAnswer());
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

