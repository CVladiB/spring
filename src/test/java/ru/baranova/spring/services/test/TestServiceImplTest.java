package ru.baranova.spring.services.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.domain.User;
import ru.baranova.spring.services.data.QuestionService;
import ru.baranova.spring.services.data.UserService;
import ru.baranova.spring.services.lang.AppSettingService;
import ru.baranova.spring.services.test.config.TestServiceImplTestConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Test class TestServiceImpl")
@SpringBootTest(classes = {TestServiceImplTestConfig.class})
@ActiveProfiles("test-service-impl")
class TestServiceImplTest {
    @Autowired
    private TestServiceImpl testServiceImpl;
    @MockBean
    private QuestionDao questionDaoCsv;
    @MockBean
    private UserService userServiceImpl;
    @MockBean
    private QuestionService questionServiceImpl;
    @MockBean
    private AppSettingService chooseAppSettingServiceImpl;
    @Autowired
    private TestServiceImplTestConfig config;
    @Autowired
    private TestServiceImpl testServiceImplString;
    private Question questionWithOptionAnswers;
    private Question questionOneAnswer;
    private Question questionWithoutAnswer;
    private List<Question> questionWithOptionAnswersList;
    private List<Question> questionOneAnswerList;
    private List<Question> questionWithoutAnswerList;

    @BeforeEach
    void setUp() {
        questionWithOptionAnswers = new QuestionWithOptionAnswers("Question First"
                , new Answer("Answer Second")
                , List.of(new Option("Answer First"), new Option("Answer Second")));
        questionOneAnswer = new QuestionOneAnswer("Question First", new Answer("Answer Second"));
        questionWithoutAnswer = new QuestionWithoutAnswer("Question First");

        questionWithOptionAnswersList = List.of(questionWithOptionAnswers);
        questionOneAnswerList = List.of(questionOneAnswer);
        questionWithoutAnswerList = List.of(questionWithoutAnswer);
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }


    @Test
    @DisplayName("Test class TestServiceImpl, method passTest, return true because enough correct answers")
    void shouldHaveTrue_EnoughCorrectAnswers() {
        testServiceImpl.setPartRightAnswers(50);
        boolean expected = testServiceImpl.passTest(5, 10);
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class TestServiceImpl, method passTest, return false because few correct answers")
    void shouldHaveFalse_FewCorrectAnswers() {
        testServiceImpl.setPartRightAnswers(50);
        boolean expected = testServiceImpl.passTest(4, 10);
        Assertions.assertFalse(expected);
    }

    @Test
    void shouldHaveStringTestFromConsole_FalseAnswer_QuestionWithOptionAnswers() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(questionWithOptionAnswersList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First\n"
                    + "1) Answer First\n"
                    + "2) Answer Second");
            return null;
        }).when(questionServiceImpl).printQuestion(questionWithOptionAnswers);
        Mockito.doReturn("1").when(questionServiceImpl).setAndGetAnswer(questionWithOptionAnswers);
        Mockito.doReturn(false).when(questionServiceImpl).checkCorrectAnswer(questionWithOptionAnswers, "1");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Question First\n"
                + "1) Answer First\n"
                + "2) Answer Second\r\n" +
                "You are extra stupid, you answered 0 / 1\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithOptionAnswers() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(questionWithOptionAnswersList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First\n"
                    + "1) Answer First\n"
                    + "2) Answer Second");
            return null;
        }).when(questionServiceImpl).printQuestion(questionWithOptionAnswers);
        Mockito.doReturn("2").when(questionServiceImpl).setAndGetAnswer(questionWithOptionAnswers);
        Mockito.doReturn(true).when(questionServiceImpl).checkCorrectAnswer(questionWithOptionAnswers, "2");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Question First\n"
                + "1) Answer First\n"
                + "2) Answer Second\r\n" +
                "You are extra smart, you answered 1 / 1\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_FalseAnswer_QuestionWithOneAnswer() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(questionOneAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImpl).printQuestion(questionOneAnswer);
        Mockito.doReturn("Answer First").when(questionServiceImpl).setAndGetAnswer(questionOneAnswer);
        Mockito.doReturn(false).when(questionServiceImpl).checkCorrectAnswer(questionOneAnswer, "Answer First");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Question First\r\n" +
                "You are extra stupid, you answered 0 / 1\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithOneAnswer() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(questionOneAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImpl).printQuestion(questionOneAnswer);
        Mockito.doReturn("Answer Second").when(questionServiceImpl).setAndGetAnswer(questionOneAnswer);
        Mockito.doReturn(true).when(questionServiceImpl).checkCorrectAnswer(questionOneAnswer, "Answer Second");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Question First\r\n" +
                "You are extra smart, you answered 1 / 1\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithoutAnswer() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(questionWithoutAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImpl).printQuestion(questionWithoutAnswer);
        Mockito.doReturn("Answer").when(questionServiceImpl).setAndGetAnswer(questionWithoutAnswer);
        Mockito.doReturn(true).when(questionServiceImpl).checkCorrectAnswer(questionWithoutAnswer, "Answer");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Question First\r\n" +
                "You are extra smart, you answered 1 / 1\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_EmptyQuestion() {
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(new ArrayList<>());

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Welcome to the test!\r\n" +
                "Упс! Чип И Дейл спешат на помощь\r\n" +
                "name surname, we hope you don't worry about it:)";
        Assertions.assertEquals(expected, actual);
    }
}