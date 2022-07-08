package ru.baranova.spring.services.data;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private final OutputService outputServiceConsole;
    private final CheckService checkServiceImpl;

    @Override
    public void printQuestion(@NonNull Question question) {
        outputDaoConsole.outputLine(question.getQuestion());
        if (question.getOptionAnswers() != null) {
            int count = 0;
            for (Option option : question.getOptionAnswers()) {
                outputDaoConsole.outputFormatLine("%s) %s\n", ++count, option.getOption());
            }
        }
    }

    @Override
    public String setAndGetAnswer(@NonNull Question question) {
        String answer = null;
        if (question.getOptionAnswers() != null) {
            int indexInputAnswer;
            do {
                outputServiceConsole.getMessage("message.question-service-message.input-number-answer");
                answer = inputDaoReader.inputLine();
                indexInputAnswer = checkServiceImpl.checkCorrectInputNumber(answer, 0, question.getOptionAnswers().size());

            } while (indexInputAnswer == -1);
        } else {
            do {
                outputServiceConsole.getMessage("message.question-service-message.input-answer");
                answer = inputDaoReader.inputLine();
            } while (!checkServiceImpl.checkCorrectInputStr(answer, 1));
        }
        return answer;
    }

    @Override
    public boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer) {
        boolean result = false;
        if (question.getOptionAnswers() != null) {
            int indexInputAnswer = checkServiceImpl.checkCorrectInputNumber(inputAnswer, 0, question.getOptionAnswers().size()); // в этом нет смысла
            if (indexInputAnswer != -1) {
                Option inputAnswerInOption = question.getOptionAnswers().get(indexInputAnswer - 1);
                result = inputAnswerInOption.getOption().equals(question.getRightAnswer().getAnswer());
            }
        } else if (question.getOptionAnswers() == null && question.getRightAnswer() != null) {
            result = inputAnswer.equalsIgnoreCase(question.getRightAnswer().getAnswer());
        } else if (question.getRightAnswer() == null) {
            result = true;
        }
        return result;
    }
}
