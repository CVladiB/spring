package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test class QuestionServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:testApplication.properties")
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
        questionServiceImpl = new QuestionServiceImpl(inputDaoReader, outputDaoConsole);
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check right answer")
       public void shouldHaveTrueAnswer() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        boolean expected = questionServiceImpl.checkCorrect(questionBean, inputAnswer);
        boolean actual = questionBean.getOptionAnswers().get(inputAnswer - 1).getOption()
                            .equals(questionBean.getRightAnswer().getAnswer());
        Assertions.assertEquals(expected, actual);
    }

    // todo
    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check answer with big number")
    public void shouldHaveFalseAnswer_NonexistentNumber() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer + 5))
                .thenReturn(String.valueOf(inputAnswer))
        ;
        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            questionServiceImpl.checkCorrect(questionBean, inputAnswer + 5);
            Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        });
        Assertions.assertTrue(exception.getMessage().contains("Введен несуществующий номер, попробуйте снова"));
    }
    // todo
    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check answer with big number")
    public void shouldHaveFalseAnswer_NonexistentNumber2 () {
        Mockito.when(inputDaoReader.inputLine())
                .thenReturn(String.valueOf(5))
                .thenThrow(IndexOutOfBoundsException.class)
                .thenReturn(String.valueOf(2));
        Throwable exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            questionServiceImpl.checkCorrect(questionBean, 5);
        });
        Assertions.assertEquals("Введен несуществующий номер, попробуйте снова", exception.getMessage());
    }

    @Test
    @DisplayName("Test class QuestionServiceImpl, method checkCorrect, check wrong answer")
    public void shouldHaveFalseAnswer_WrongNumber() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(1));
        boolean expected = questionServiceImpl.checkCorrect(questionBean, 1);
        boolean actual = questionBean.getOptionAnswers().get(0).getOption()
                .equals(questionBean.getRightAnswer().getAnswer());
        Assertions.assertEquals(expected, actual);
    }

    // todo
    @Disabled
    @Test
    @DisplayName("Test class QuestionServiceImpl, method printQuestion, check print")
    public void printQuestion() {
        questionServiceImpl.printQuestion(questionBean);
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor.capture());
       /* Mockito.when(outputDaoConsole.outputLine(1 + ") " +  questionBean.getOptionAnswers().get(0).getOption()))
                .thenCallRealMethod(System.out.println("Answer First"));
        Mockito.when(outputDaoConsole.outputLine(1 + ") " +  questionBean.getOptionAnswers().get(0).getOption()))
                .thenCallRealMethod(System.out.println("Answer Second"));*/
        //assertEquals("Answer Second", consoleArgumentCaptor.getValue());
        System.out.println();

        assertAll("",
                () -> assertEquals("Question First", consoleArgumentCaptor.getValue()),
                () -> assertEquals("Answer First", consoleArgumentCaptor.getValue()),
                () -> assertEquals("Answer Second", consoleArgumentCaptor.getValue())
        );
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