package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.User;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("Test class TestServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:testApplication.properties")
@ContextConfiguration(classes = {TestServiceImpl.class, TestServiceImplTestConfig.class})
class TestServiceImplTest {
    @Autowired
    private TestServiceImpl testServiceImpl;

    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private OutputDao outputDaoConsole;
    @MockBean
    private QuestionDao questionDaoCsv;
    @MockBean
    private UserService userServiceImpl;
    @MockBean
    private QuestionService questionServiceImpl;

    private PrintWriter output;
    private List<Question> listQuestion = Arrays.asList(new Question("Question First", new Answer("Answer Second"),
            Arrays.asList(new Option("Answer First"), new Option("Answer Second"))));

    @BeforeEach
    void setUp() {
        output = new PrintWriter(System.out);
    }

    // todo
    @Test
    @Disabled
    @DisplayName("Test class TestServiceImpl, method test, check start message")
    void shouldHaveMessageStartWithoutQuestions() {
        User user = new User("user", "surname");
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(Collections.emptyList());
        Mockito.when(userServiceImpl.createUser()).thenReturn(user);
        testServiceImpl.test();

        Assertions.assertTrue(output.toString().contains("Hi"));
        Assertions.assertTrue(output.toString().contains(String.format("Bye, %s %s!", user.getName(), user.getSurname())));
//        Assertions.assertEquals(String.format("Bye, %s %s!", user.getName(), user.getSurname()), "");
    }

    @Test
    @Disabled
    @DisplayName("Test class TestServiceImpl, method test, check ???")
    void shouldHave() {
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(listQuestion);
        Mockito.when(userServiceImpl.createUser()).thenReturn(new User("name", "surname"));
        Mockito.when(inputDaoReader.inputLine()).thenReturn("2");
        testServiceImpl.test();
    }

    @Test
    @DisplayName("Test class TestServiceImpl, method correctPartRightAnswers, return number of right answers to pass the test")
    void shouldHaveCorrectPartRightAnswers() {
        int result = (int) Math.round(4.0 / 5 * 100);
        boolean actual = result >= testServiceImpl.getPartRightAnswers();
        boolean expected = testServiceImpl.passTest(4, 5);
        Assertions.assertEquals(expected, actual);
    }
}