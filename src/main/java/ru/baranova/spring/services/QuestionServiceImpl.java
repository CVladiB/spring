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
        int count = 0;
        for(Option temp : question.getOptionAnswers()) {
            outputDaoConsole.outputLine(count++ + ") " +  temp.getOption());
        }
    }

    public boolean checkCorrect(Question question, int inputAnswer) {

        return true;
    }
}
