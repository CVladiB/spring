package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.User;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private final QuestionDao questionDaoCsv;
    private final UserService userServiceImpl;
    private final QuestionService questionServiceImpl;
    @Value("${app.bean.testServiceImpl.start}")
    private String start;
    @Value("${app.bean.testServiceImpl.inputAnswer}")
    private String inputAnswer;
    @Value("${app.bean.testServiceImpl.finish}")
    private String finish;
    @Value("${app.bean.testServiceImpl.win}")
    private String win;
    @Value("${app.bean.testServiceImpl.fail}")
    private String fail;

    @Override
    public void test() {
        List<Question> questions = questionDaoCsv.loadQuestion();
        outputDaoConsole.outputLine(start);
        User user = userServiceImpl.createUser();

        int countCorrectAnswer = 0;
        for (Question question : questions) {
            questionServiceImpl.printQuestion(question);
            outputDaoConsole.outputLine(inputAnswer);
            int numberAnswer = Integer.parseInt(inputDaoReader.inputLine());
            if (questionServiceImpl.checkCorrect(question, numberAnswer)) {
                ++countCorrectAnswer;
            }
        }

    }
}
