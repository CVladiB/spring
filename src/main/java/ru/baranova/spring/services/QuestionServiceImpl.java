package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.question-service-impl")
public class QuestionServiceImpl implements QuestionService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;

    private String inputAnswer;
    private String inputNumberAnswer;

    @Override
    public void printQuestion(@NonNull Question question) {
        outputDaoConsole.outputLine(question.getQuestion());
        if (question instanceof QuestionWithOptionAnswers) {
            int count = 0;
            for (Option option : question.getOptionAnswers()) {
                outputDaoConsole.outputFormatLine("%s) %s\n", ++count, option.getOption());
            }
        }
    }

    @Override
    public String getAnswer(@NonNull Question question) {
        String answer = null;

        if (question instanceof QuestionWithOptionAnswers) {
            int indexInputAnswer;
            int size = question.getOptionAnswers().size();

            do {
                outputDaoConsole.outputFormatLine(inputNumberAnswer);
                answer = inputDaoReader.inputLine();

                try {
                    indexInputAnswer = Integer.parseInt(answer);
                    if (indexInputAnswer > size) {
                        log.info("Указанный номер ответа превышает доступные варианты");
                    }
                    if (indexInputAnswer < 1) {
                        log.info("Требуется ввести номер ответа среди указанных");
                    }
                } catch (NumberFormatException e) {
                    log.info("Требуется ввести номер ответа");
                    indexInputAnswer = size + 1;
                }

            } while (indexInputAnswer > size || indexInputAnswer < 1);
        } else if (question instanceof QuestionOneAnswer || question instanceof QuestionWithoutAnswer) {
            do {
                outputDaoConsole.outputFormatLine(inputAnswer);
                answer = inputDaoReader.inputLine();
            } while (answer.length() < 1);
        }

        return answer;
    }

    @Override
    public boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer) {
        boolean result = false;
        if (question instanceof QuestionWithOptionAnswers) {
            int indexInputAnswer = -1;
            Option inputAnswerInOption = null;

            try {
                indexInputAnswer = Integer.parseInt(inputAnswer);
                inputAnswerInOption = question.getOptionAnswers().get(indexInputAnswer - 1);
                result = inputAnswerInOption.getOption().equals(question.getRightAnswer().getAnswer());
            } catch (NumberFormatException e) {
                log.info("Требуется передать номер ответа");
            } catch (IndexOutOfBoundsException e) {
                log.info("Переданный номер ответа превышает доступные варианты");
            }
        } else if (question instanceof QuestionOneAnswer) {
            result = inputAnswer.equalsIgnoreCase(question.getRightAnswer().getAnswer());
        } else if (question instanceof QuestionWithoutAnswer) {
            result = true;
        }
        return result;
    }
}
