package ru.baranova.spring.services.test;

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
import ru.baranova.spring.dao.LocaleProvider;
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
import ru.baranova.spring.services.test.config.TestServiceImplTestConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@DisplayName("Test class TestServiceImpl")
@SpringBootTest(classes = {TestServiceImplTestConfig.class, ComponentScanStopConfig.class})
@ActiveProfiles("test-service-impl")
class TestServiceImplTest {
    @Autowired
    private TestServiceImplTestConfig config;
    @Autowired
    private TestServiceImpl testServiceImplString;
    @Autowired
    private QuestionDao questionDaoCsvMock;
    @Autowired
    private UserService userServiceImplMock;
    @Autowired
    private QuestionService questionServiceImplMock;
    @Autowired
    private LocaleProvider localeProviderImplString;
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
        testServiceImplString.setPartRightAnswers(50);
        boolean expected = testServiceImplString.passTest(5, 10);
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class TestServiceImpl, method passTest, return false because few correct answers")
    void shouldHaveFalse_FewCorrectAnswers() {
        testServiceImplString.setPartRightAnswers(50);
        boolean expected = testServiceImplString.passTest(4, 10);
        Assertions.assertFalse(expected);
    }

    @Test
    void shouldHaveStringTestFromConsole_FalseAnswer_QuestionWithOptionAnswers() {

        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(questionWithOptionAnswersList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First\n"
                    + "1) Answer First\n"
                    + "2) Answer Second");
            return null;
        }).when(questionServiceImplMock).printQuestion(questionWithOptionAnswers);
        Mockito.doReturn("1").when(questionServiceImplMock).setAndGetAnswer(questionWithOptionAnswers);
        Mockito.doReturn(false).when(questionServiceImplMock).checkCorrectAnswer(questionWithOptionAnswers, "1");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Question First\n"
                + "1) Answer First\n"
                + "2) Answer Second\r\n" +
                "Ты очень глуп, ты ответил только на 0 вопросов из 1\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithOptionAnswers() {
        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(questionWithOptionAnswersList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First\n"
                    + "1) Answer First\n"
                    + "2) Answer Second");
            return null;
        }).when(questionServiceImplMock).printQuestion(questionWithOptionAnswers);
        Mockito.doReturn("2").when(questionServiceImplMock).setAndGetAnswer(questionWithOptionAnswers);
        Mockito.doReturn(true).when(questionServiceImplMock).checkCorrectAnswer(questionWithOptionAnswers, "2");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Question First\n"
                + "1) Answer First\n"
                + "2) Answer Second\r\n" +
                "Ты очень умен, ты ответил на 1 вопросов из 1\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_FalseAnswer_QuestionWithOneAnswer() {
        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(questionOneAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImplMock).printQuestion(questionOneAnswer);
        Mockito.doReturn("Answer First").when(questionServiceImplMock).setAndGetAnswer(questionOneAnswer);
        Mockito.doReturn(false).when(questionServiceImplMock).checkCorrectAnswer(questionOneAnswer, "Answer First");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Question First\r\n" +
                "Ты очень глуп, ты ответил только на 0 вопросов из 1\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithOneAnswer() {
        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(questionOneAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImplMock).printQuestion(questionOneAnswer);
        Mockito.doReturn("Answer Second").when(questionServiceImplMock).setAndGetAnswer(questionOneAnswer);
        Mockito.doReturn(true).when(questionServiceImplMock).checkCorrectAnswer(questionOneAnswer, "Answer Second");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Question First\r\n" +
                "Ты очень умен, ты ответил на 1 вопросов из 1\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_TrueAnswer_QuestionWithoutAnswer() {
        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(questionWithoutAnswerList);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("Question First");
            return null;
        }).when(questionServiceImplMock).printQuestion(questionWithoutAnswer);
        Mockito.doReturn("Answer").when(questionServiceImplMock).setAndGetAnswer(questionWithoutAnswer);
        Mockito.doReturn(true).when(questionServiceImplMock).checkCorrectAnswer(questionWithoutAnswer, "Answer");

        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Question First\r\n" +
                "Ты очень умен, ты ответил на 1 вопросов из 1\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveStringTestFromConsole_EmptyQuestion() {
        Mockito.when(localeProviderImplString.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));

        Mockito.when(userServiceImplMock.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(questionDaoCsvMock.loadQuestion()).thenReturn(new ArrayList<>());


        testServiceImplString.test();

        String actual = config.getOut().toString();
        String expected = "Добро пожаловать на тестирвоание интеллекта!\r\n" +
                "Упс! Чип И Дейл спешат на помощь\r\n\n" +
                "name surname, надеюсь, ты не обижаешься на нас:)\r\n";
        Assertions.assertEquals(expected, actual);
    }
}