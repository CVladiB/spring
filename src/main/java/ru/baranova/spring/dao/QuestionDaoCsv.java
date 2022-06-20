package ru.baranova.spring.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class QuestionDaoCsv implements QuestionDao {

    private final ReaderDao readerDao;
    private String path;
    private String delimiter;
    private int questionPosition;
    private int rightAnswerPosition;
    private int optionPosition;

    public QuestionDaoCsv(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public void setQuestionPosition(int questionPosition) {
        this.questionPosition = questionPosition - 1;
    }

    public void setRightAnswerPosition(int rightAnswerPosition) {
        this.rightAnswerPosition = rightAnswerPosition - 1;
    }

    public void setOptionPosition(int optionPosition) {
        this.optionPosition = optionPosition - 1;
    }

    @Override
    public List<Question> loadQuestion() {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(readerDao.getResource(path)))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Нет вопросов");
            lines = new ArrayList<>();
        }
        return parseStrings(lines);
    }

    public List<Question> parseStrings(@NonNull List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            String[] arr = line.split(delimiter);
            if (arr.length == 1) {
                questions.add(new Question(arr[getQuestionPosition()]));
            } else if (arr.length == 2) {
                questions.add(new Question(arr[getQuestionPosition()], new Answer(arr[getRightAnswerPosition()])));
            } else if (arr.length > 2) {
                List<Option> listOption = new ArrayList<>();
                for (int j = 0; j < arr.length; j++) {
                    if (j != getQuestionPosition() && j != getRightAnswerPosition()) {
                        listOption.add(new Option(arr[j]));
                    }
                }
                questions.add(new Question(arr[getQuestionPosition()], new Answer(arr[getRightAnswerPosition()]), listOption));
            } else {
                log.error("Неудачная попытка разобрать вопросы");
            }
        }
        return questions;
    }
}
