package ru.baranova.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.dao.reader.ReaderDaoFile;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest
@ContextConfiguration(locations = "/test-question-spring-context.xml")
public class QuestionDaoCsvTest {

    @Autowired
    @Qualifier("testQuestionDao")
    private QuestionDaoCsv questionDaoCsv;


    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List Question")
    void shouldHaveCorrectLoadQuestion() {
        String initialString = "Question First;Answer Fourth;Answer First;Answer Second;Answer Third;Answer Fourth";
        ReaderDao mockReaderDao = mock(ReaderDaoFile.class);
        Mockito.when(mockReaderDao.getResource("testQuestionnaire.csv"))
                        .thenReturn(new ByteArrayInputStream(initialString.getBytes()));

        List<Question> expected = questionDaoCsv.loadQuestion();

        List<Question> actual = new ArrayList<>();
        List<Option> optionList = Arrays.asList(new Option("Answer First"), new Option("Answer Second"), new Option("Answer Third"), new Option("Answer Fourth"));
        Question question = new Question("Question First", new Answer("Answer Fourth"), optionList);
        actual.add(question);

        Assertions.assertIterableEquals(expected, actual);
    }


    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List Question by 1 constructor")
    void shouldHaveCorrectParseStrings_1() {
        List<String> initialString = new ArrayList<>();
        initialString.add("Question First");

        List<Question> expected = questionDaoCsv.parseStrings(initialString);

        List<Question> actual = new ArrayList<>();
        Question question = new Question("Question First");
        actual.add(question);

        Assertions.assertIterableEquals(expected, actual);
    }
    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List Question by 2 constructor")
    void shouldHaveCorrectParseStrings_2() {
        List<String> initialString = new ArrayList<>();
        initialString.add("Question First;Answer");
        List<Question> expected = questionDaoCsv.parseStrings(initialString);

        List<Question> actual = new ArrayList<>();
        Question question = new Question("Question First", new Answer("Answer"));
        actual.add(question);

        Assertions.assertIterableEquals(expected, actual);
    }
    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List Question by 3 constructor")
    void shouldHaveCorrectParseStrings_3() {
        List<String> initialString = new ArrayList<>();
        initialString.add("Question First;Answer Fourth;Answer First;Answer Second;Answer Third;Answer Fourth");

        List<Question> expected = questionDaoCsv.parseStrings(initialString);

        List<Question> actual = new ArrayList<>();
        List<Option> optionList = Arrays.asList(new Option("Answer First"), new Option("Answer Second"), new Option("Answer Third"), new Option("Answer Fourth"));
        Question question = new Question("Question First", new Answer("Answer Fourth"), optionList);
        actual.add(question);

        Assertions.assertIterableEquals(expected, actual);
    }

}
