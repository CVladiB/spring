package ru.baranova.spring.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.dao.reader.ReaderDaoFile;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.services.QuestionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DisplayName("Test class QuestionDaoCsv")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@PropertySource("classpath:testApplication.properties")
public class QuestionDaoCsvTest {
    private QuestionDaoCsv questionDaoCsv;
    @Mock
    private ReaderDao readerDaoFile;
    private String initialStringUnansweredQuestion;
    private String initialStringOpenEndedQuestion;
    private String initialString;
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;
/*    @Value("${test.bean.questionDaoCsv.path}")
    private String path;
    @Value("${test.bean.questionDaoCsv.initialStringUnansweredQuestion}")
    private String initialStringUnansweredQuestion;
    @Value("${test.bean.questionDaoCsv.initialStringOpenEndedQuestion}")
    private String initialStringOpenEndedQuestion;
    @Value("${test.bean.questionDaoCsv.initialString}")
    private String initialString;
    @Value("${test.bean.questionDaoCsv.question}")
    private String question;
    @Value("${test.bean.questionDaoCsv.rightAnswer}")
    private String rightAnswer;
    @Value("${test.bean.questionDaoCsv.optionOne}")
    private String optionOne;
    @Value("${test.bean.questionDaoCsv.optionTwo}")
    private String optionTwo;*/

    @BeforeEach
    void setUp() {
        questionDaoCsv = new QuestionDaoCsv(readerDaoFile);
        questionDaoCsv.setQuestionPosition(1);
        questionDaoCsv.setRightAnswerPosition(2);
        questionDaoCsv.setDelimiter(";");
        questionDaoCsv.setPath("testQuestionnaire.csv");
        initialStringUnansweredQuestion = "Question First";
        initialStringOpenEndedQuestion = "Question First;Answer Second";
        initialString = "Question First;Answer Second;Answer First;Answer Second";
        question = "Question First";
        rightAnswer = "Answer Second";
        optionOne = "Answer First";
        optionTwo = "Answer Second";
    }

    // todo fix thenReturn in Mockito
    @Test
    @DisplayName("Test class QuestionDaoCsv, method loadQuestion, return correct List Question")
    void shouldHaveCorrectLoadQuestion() {
        //Mockito.when(readerDaoFile.getResource(path)).thenReturn(ApplicationContext.getResource(path).getInputStream());

        List<Question> expected = questionDaoCsv.loadQuestion();
        List<Question> actual = new ArrayList<>();
        actual.add(new Question(question, new Answer(rightAnswer), Arrays.asList(new Option(optionOne), new Option(optionTwo))));

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List with unanswered question")
    void shouldHaveCorrectParseStrings_1() {
        List<String> initialStringList = new ArrayList<>();
        initialStringList.add(initialStringUnansweredQuestion);

        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);

        List<Question> actual = new ArrayList<>();
        actual.add(new Question(question));

        Assertions.assertIterableEquals(expected, actual);
    }
    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List with open-ended question")
    void shouldHaveCorrectParseStrings_2() {
        List<String> initialStringList = Arrays.asList(initialStringOpenEndedQuestion);
        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);

        List<Question> actual = Arrays.asList(new Question(question, new Answer(rightAnswer)));

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionDaoCsv, method parseStrings, return correct List")
    void shouldHaveCorrectParseStrings_3() {
        List<String> initialStringList = Arrays.asList(initialString);

        List<Question> expected = questionDaoCsv.parseStrings(initialStringList);

        List<Option> optionList = Arrays.asList(new Option(optionOne), new Option(optionTwo));
        List<Question> actual = Arrays.asList(new Question(question, new Answer(rightAnswer), optionList));

        Assertions.assertEquals(expected, actual);
    }
}
