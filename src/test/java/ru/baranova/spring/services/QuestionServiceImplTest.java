package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Test class QuestionServiceImpl")
@ContextConfiguration(classes = {QuestionServiceImpl.class, QuestionServiceImplTestConfig.class})
class QuestionServiceImplTest {
    @Autowired
    private QuestionService questionServiceImpl;
    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    private OutputDao outputDaoConsole;
    @Captor
    ArgumentCaptor<String> consoleArgumentCaptor;


    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;
    private Question questionBean;
    private int inputAnswer = 2;

    @BeforeEach
    void setUp() {
        question = "Question First";
        rightAnswer = "Answer Second";
        optionOne = "Answer First";
        optionTwo = "Answer Second";
        questionBean = new Question(question, new Answer(rightAnswer), Arrays.asList(new Option(optionOne), new Option(optionTwo)));
    }

    // todo
    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, check print")
    public void printQuestion() {
        questionServiceImpl.printQuestion(questionBean);
        Mockito.verify(outputDaoConsole, Mockito.times(3)).outputLine(consoleArgumentCaptor.capture());
        assertAll("",
                () -> assertEquals("Question First", consoleArgumentCaptor.getValue()),
                () -> assertEquals("1) Answer First", consoleArgumentCaptor.getValue()),
                () -> assertEquals("2) Answer Second", consoleArgumentCaptor.getValue())
        );
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer")
       public void shouldHaveTrueAnswer() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        boolean expected = questionServiceImpl.checkCorrect(questionBean, inputAnswer);
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check wrong answer")
    public void shouldHaveFalseAnswer_WrongNumber() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(1));
        boolean expected = questionServiceImpl.checkCorrect(questionBean, 1);
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, input right answer")
    public void shouldHaveRightAnswer() {
        int inputAnswer = 2;
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        int actual = questionServiceImpl.getAnswer(questionBean);
        int expected = inputAnswer;
        for (int i = 0; i < questionBean.getOptionAnswers().size(); i++) {
            if (questionBean.getRightAnswer().getAnswer().equals(questionBean.getOptionAnswers().get(i).getOption())) {
                actual = i + 1;
            }
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, input wrong answer")
    public void shouldHaveWrongAnswer() {
        int inputAnswer = 1;
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        int actual = questionServiceImpl.getAnswer(questionBean);
        Assertions.assertEquals(inputAnswer, actual);
    }

    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, input answer with big number")
    public void shouldHaveFalseAnswer_NonexistentNumber() {
        int inputAnswer = 17;
        Mockito.lenient().when(inputDaoReader.inputLine())
                .thenReturn(String.valueOf(inputAnswer))
                .thenReturn(String.valueOf(2));
        // Проверка повторного вывода сообщения о вводе номера ответа
    }

    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method getAnswer, input answer with big number")
    public void shouldHaveFalseAnswer_Symbol () {
        int inputAnswer = 2;
        Mockito.when(inputDaoReader.inputLine()).thenReturn("Second").thenReturn(String.valueOf(2));
        Throwable exception = assertThrows(NumberFormatException.class, () -> {
            questionServiceImpl.getAnswer(questionBean);
            Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        });
        Assertions.assertTrue(exception.getMessage().contains("Введите номер ответа без лишних символов"));
        // Проверка лога о необходимости ввести цифру
    }
}

@TestConfiguration
class QuestionServiceImplTestConfig {
    @Bean
    public InputDao getInputDaoReader() {
        return Mockito.mock(InputDao.class);
    }
    @Bean
    public OutputDao getOutputDaoConsole() {
        return Mockito.mock(OutputDao.class);
    }
}