package ru.baranova.spring.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.io.InputDao;
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

@Slf4j
@Setter
@Getter
@Component
@RequiredArgsConstructor
@EqualsAndHashCode
public class QuestionDaoCsv implements QuestionDao {
    private final ReaderDao readerDaoFile;
    @Value("${app.bean.questionDaoCsv.path}")
    private String path;
    @Value("${app.bean.questionDaoCsv.delimiter}")
    private String delimiter;
    private int questionPosition;
    private int rightAnswerPosition;

    @Value("${app.bean.questionDaoCsv.questionPosition}")
    public void setQuestionPosition( int questionPosition) {
        this.questionPosition = questionPosition - 1;
    }
    @Value("${app.bean.questionDaoCsv.rightAnswerPosition}")
    public void setRightAnswerPosition( int rightAnswerPosition) {
        this.rightAnswerPosition = rightAnswerPosition - 1;
    }

    @Override
    public List<Question> loadQuestion() {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(readerDaoFile.getResource(path)))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            log.error("Ошибка загрузки вопросов, обратитесь к администратору");
            lines = new ArrayList<>();
        }
        return parseStrings(lines);
    }

    public List<Question> parseStrings(@NonNull List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isEmpty()) {
                continue;
            }

            String[] arr = line.split(delimiter);
            if (arr.length == 1) {
                questions.add(new Question(arr[questionPosition]));
            } else if (arr.length == 2) {
                questions.add(new Question(arr[questionPosition], new Answer(arr[rightAnswerPosition])));
            } else if (arr.length > 2) {
                List<Option> listOption = new ArrayList<>();
                for (int j = 0; j < arr.length; j++) {
                    if (j != questionPosition && j != rightAnswerPosition) {
                        listOption.add(new Option(arr[j]));
                    }
                }
                questions.add(new Question(arr[questionPosition], new Answer(arr[rightAnswerPosition]), listOption));
            } else {
                log.error("Неудачная попытка разобрать вопросы");
            }
        }
        return questions;
    }
}
