package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.question-service-impl")
public class QuestionServiceImpl implements QuestionService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private String inputAnswer;

    @Override
    public void printQuestion(Question question) {
        outputDaoConsole.outputLine(question.getQuestion());
        int count = 1;
        for (Option temp : question.getOptionAnswers()) {
            outputDaoConsole.outputLine(count++ + ") " + temp.getOption());
        }
    }

    @Override
    public boolean checkCorrect(Question question, int inputAnswer) {
        String textInputAnswer = question.getOptionAnswers().get(inputAnswer - 1).getOption();
        return textInputAnswer.equals(question.getRightAnswer().getAnswer());
    }

    @Override
    public int getAnswer(Question question) {
        int numberAnswer;
        do {
            outputDaoConsole.outputLine(inputAnswer);
            try {
                numberAnswer = Integer.parseInt(inputDaoReader.inputLine());
            } catch (NumberFormatException e) {
                log.error("Введите номер ответа без лишних символов");
                outputDaoConsole.outputLine(inputAnswer);
                numberAnswer = Integer.parseInt(inputDaoReader.inputLine());
            }
        } while (question.getOptionAnswers().size() < numberAnswer);
        return numberAnswer;
    }
}

