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
public class QuestionServiceImpl implements QuestionService{
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;

    public void printQuestion (Question question) {
        outputDaoConsole.outputLine(question.getQuestion());
        int count = 1;
        for(Option temp : question.getOptionAnswers()) {
            outputDaoConsole.outputLine(count++ + ") " +  temp.getOption());
        }
    }

    public boolean checkCorrect(Question question, int inputAnswer) {
        String textInputAnswer = null;
        while (true) {
            try {
                textInputAnswer = question.getOptionAnswers().get(inputAnswer - 1).getOption();
                return textInputAnswer.equals(question.getRightAnswer().getAnswer());
            } catch (IndexOutOfBoundsException e) {
                log.info("Введен несуществующий номер, попробуйте снова");
                printQuestion(question);
                inputAnswer = Integer.parseInt(inputDaoReader.inputLine());
            }
        }
    }
}
