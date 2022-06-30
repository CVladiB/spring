package ru.baranova.spring.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.User;

import java.util.List;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {
    private final OutputDao outputDaoConsole;
    private final QuestionDao questionDaoCsv;
    private final UserService userServiceImpl;
    private final QuestionService questionServiceImpl;
    @Getter
    @Value("${app.bean.testServiceImpl.partRightAnswers}")
    private int partRightAnswers;
    @Value("${app.bean.testServiceImpl.start:Hi}")
    private String start;
    @Value("${app.bean.testServiceImpl.finish}")
    private String finish;
    @Value("${app.bean.testServiceImpl.win}")
    private String win;
    @Value("${app.bean.testServiceImpl.fail}")
    private String fail;

    @Override
    public void test() {
        outputDaoConsole.outputLine(start);
        User user = userServiceImpl.createUser();
        List<Question> questions = questionDaoCsv.loadQuestion();
        if (!questions.isEmpty()) {
            int countCorrectAnswer = 0;

            for (Question question : questions) {
                questionServiceImpl.printQuestion(question);
                String numberAnswer = questionServiceImpl.getAnswer(question);
                if (questionServiceImpl.checkCorrectAnswer(question, numberAnswer)) {
                    ++countCorrectAnswer;
                }
            }

            int numberOfQuestions = questions.size();
            String testResult = passTest(countCorrectAnswer, numberOfQuestions) ? win : fail;
            outputDaoConsole.outputFormatLine(testResult, countCorrectAnswer, numberOfQuestions);

        } else {
            outputDaoConsole.outputLine("Упс! Чип И Дейл спешат на помощь");
        }
        outputDaoConsole.outputFormatLine(finish, user.getName(), user.getSurname());
    }

    @Override
    public boolean passTest(double countCorrectAnswer, int numberOfQuestions) {
        int result = (int) Math.round(countCorrectAnswer / numberOfQuestions * 100);
        return result >= partRightAnswers;
    }
}
