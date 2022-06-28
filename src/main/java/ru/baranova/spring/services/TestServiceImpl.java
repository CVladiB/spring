package ru.baranova.spring.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImpl implements TestService {
    private final OutputDao outputDaoConsole;
    private final QuestionDao questionDaoCsv;
    private final UserService userServiceImpl;
    private final QuestionService questionServiceImpl;
    @Getter
    private int partRightAnswers;
    private String start;
    private String finish;

    private String win;
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
                int numberAnswer = questionServiceImpl.getAnswer(question);
                if (questionServiceImpl.checkCorrect(question, numberAnswer)) {
                    ++countCorrectAnswer;
                }
            }

            int numberOfQuestions = questions.size();

            outputDaoConsole.outputFormatLine(passTest(countCorrectAnswer, numberOfQuestions) ? win : fail, countCorrectAnswer, numberOfQuestions);

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
