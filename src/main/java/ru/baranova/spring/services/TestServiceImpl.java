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
@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImpl implements TestService {
    private final OutputDao outputDaoConsole;
    private final QuestionDao questionDaoCsv;
    private final UserService userServiceImpl;
    private final QuestionService questionServiceImpl;
    private final LocaleService localeServiceImpl;
    @Getter
    @Setter
    private int partRightAnswers;

    @Override
    public void test() {
        localeServiceImpl.chooseLanguage();

        outputDaoConsole.outputLine(localeServiceImpl.getMessage("message.test-service-message.start"));
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
            String testResult = passTest(countCorrectAnswer, numberOfQuestions) ? "message.test-service-message.win" : "message.test-service-message.fail";
            outputDaoConsole.outputFormatLine(localeServiceImpl.getMessage(testResult, countCorrectAnswer, numberOfQuestions));
        } else {
            outputDaoConsole.outputLine(localeServiceImpl.getMessage("log.smth-wrong"));
        }
        outputDaoConsole.outputFormatLine(localeServiceImpl.getMessage(
                "message.test-service-message.finish",
                user.getName(), user.getSurname()));
    }

    @Override
    public boolean passTest(double countCorrectAnswer, int numberOfQuestions) {
        int result = (int) Math.round(countCorrectAnswer / numberOfQuestions * 100);
        return result >= partRightAnswers;
    }
}
