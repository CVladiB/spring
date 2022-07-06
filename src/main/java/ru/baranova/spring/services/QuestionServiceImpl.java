package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private final LocaleService localeServiceImpl;

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
    public String getAnswer(@NonNull Question question) {
        String answer = null;
        if (question.getOptionAnswers() != null) {
            int indexInputAnswer;
            int size = question.getOptionAnswers().size();

            do {
                outputDaoConsole.outputFormatLine(localeServiceImpl.getMessage("message.question-service-message.input-number-answer"));
                answer = inputDaoReader.inputLine();

                try {
                    indexInputAnswer = Integer.parseInt(answer);
                    if (indexInputAnswer > size) {
                        log.info(localeServiceImpl.getMessage("log.long-input"));
                    }
                    if (indexInputAnswer < 1) {
                        log.info(localeServiceImpl.getMessage("log.short-input"));
                    }
                } catch (NumberFormatException e) {
                    log.info(localeServiceImpl.getMessage("log.should-number-input"));
                    indexInputAnswer = size + 1;
                }

            } while (indexInputAnswer > size || indexInputAnswer < 1);
        } else {
            do {
                outputDaoConsole.outputFormatLine(localeServiceImpl.getMessage("message.question-service-message.input-answer"));
                answer = inputDaoReader.inputLine();
            } while (answer.length() < 1);
        }
        return answer;
    }

    @Override
    public boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer) {
        boolean result = false;
        if (question.getOptionAnswers() != null) {
            int indexInputAnswer = -1;
            Option inputAnswerInOption = null;

            try {
                indexInputAnswer = Integer.parseInt(inputAnswer);
                inputAnswerInOption = question.getOptionAnswers().get(indexInputAnswer - 1);
                result = inputAnswerInOption.getOption().equals(question.getRightAnswer().getAnswer());
            } catch (NumberFormatException e) {
                log.info(localeServiceImpl.getMessage("log.should-number-input"));
            } catch (IndexOutOfBoundsException e) {
                log.info(localeServiceImpl.getMessage("log.short-input"));
            }
        } else if (question.getOptionAnswers() == null && question.getRightAnswer() != null) {
            result = inputAnswer.equalsIgnoreCase(question.getRightAnswer().getAnswer());
        } else if (question.getRightAnswer() == null) {
            result = true;
        }
        return result;
    }
}
