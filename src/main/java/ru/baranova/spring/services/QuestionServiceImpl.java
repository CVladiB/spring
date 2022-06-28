package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;

    public void printQuestion(Question question) {
        outputDaoConsole.outputLine(question.getQuestion());
        int count = 1;
        for (Option temp : question.getOptionAnswers()) {
            outputDaoConsole.outputLine(count++ + ") " + temp.getOption());
        }
    }

    public boolean checkCorrect(Question question, int inputAnswer) {
        String textInputAnswer = question.getOptionAnswers().get(inputAnswer - 1).getOption();
        return textInputAnswer.equals(question.getRightAnswer().getAnswer());
    }

    @Override
    public int getAnswer(Question question) {
        int numberAnswer;
        do {
            outputDaoConsole.outputLine("inputAnswer");
             numberAnswer = Integer.parseInt(inputDaoReader.inputLine());
        } while (question.getOptionAnswers().size() < numberAnswer - 1);
        return numberAnswer;
    }
}
