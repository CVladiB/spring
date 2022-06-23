package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.io.InputDaoReader;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test class QuestionServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
class QuestionServiceImplTest {
    @Mock
    private InputDaoReader inputDaoReader;
    @Mock
    private OutputDaoConsole outputDaoConsole;
    private QuestionServiceImpl questionServiceImpl;

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
    public void checkCorrect() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn(String.valueOf(inputAnswer));
        /*for (int i = 0; i <= questionBean.getOptionAnswers().size(); i++) {
            questionServiceImpl.checkCorrect(questionBean, i);
        }*/
        boolean expected = questionServiceImpl.checkCorrect(questionBean, inputAnswer);
        boolean actual = questionBean.getOptionAnswers().get(inputAnswer - 1).getOption()
                            .equals(questionBean.getRightAnswer().getAnswer());
        Assertions.assertEquals(expected, actual);
    }
}