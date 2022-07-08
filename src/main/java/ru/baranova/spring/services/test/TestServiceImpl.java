package ru.baranova.spring.services.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.baranova.spring.annotation.MethodArg;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.User;
import ru.baranova.spring.services.data.QuestionService;
import ru.baranova.spring.services.data.UserService;
import ru.baranova.spring.services.io.OutputService;
import ru.baranova.spring.services.lang.AppSettingService;

import java.util.List;

@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImpl implements TestService {
    private final OutputService outputServiceConsole;
    private final QuestionDao questionDaoCsv;
    private final UserService userServiceImpl;
    private final QuestionService questionServiceImpl;
    private final AppSettingService chooseAppSettingServiceImpl;

    @Autowired
    private TestService self;

    @Getter
    @Setter
    private int partRightAnswers;

    @MethodArg
    @Override
    public void test() {
        chooseAppSettingServiceImpl.printOptionsOfLanguage();
        chooseAppSettingServiceImpl.setLanguage();

        outputServiceConsole.getMessage("message.test-service-message.start");
        User user = userServiceImpl.createUser();
        List<Question> questions = questionDaoCsv.loadQuestion();
        if (!questions.isEmpty()) {
            int countCorrectAnswer = 0;

            for (Question question : questions) {
                questionServiceImpl.printQuestion(question);
                String numberAnswer = questionServiceImpl.setAndGetAnswer(question);
                if (questionServiceImpl.checkCorrectAnswer(question, numberAnswer)) {
                    ++countCorrectAnswer;
                }
            }

            int numberOfQuestions = questions.size();
            String testResult = self.passTest(countCorrectAnswer, numberOfQuestions) ? "message.test-service-message.win" : "message.test-service-message.fail";
            outputServiceConsole.getMessage(testResult, countCorrectAnswer, numberOfQuestions);
        } else {
            outputServiceConsole.getMessage("log.smth-wrong");
        }
        outputServiceConsole.getMessage(
                "message.test-service-message.finish",
                user.getName(), user.getSurname());
    }

    @Override
    public boolean passTest(double countCorrectAnswer, int numberOfQuestions) {
        int result = (int) Math.round(countCorrectAnswer / numberOfQuestions * 100);
        return result >= partRightAnswers;
    }
}
