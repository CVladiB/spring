package ru.baranova.spring.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
@EqualsAndHashCode
@Component
@ConfigurationProperties(prefix = "app.dao.question-dao-csv")
public class QuestionDaoCsv implements QuestionDao {
    private final ReaderDao readerDaoFile;
    @Getter
    private String path;
    private String delimiter;
    private int questionPosition;
    private int rightAnswerPosition;


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

    @Override
    public List<Question> parseStrings(@NonNull List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            String[] arr = line.split(delimiter);
            switch (arr.length) {
                case 0 -> log.error("Неудачная попытка разобрать вопросы");
                case 1 -> questions.add(new QuestionWithoutAnswer(arr[questionPosition]));
                case 2 ->
                        questions.add(new QuestionOneAnswer(arr[questionPosition], new Answer(arr[rightAnswerPosition])));
                default -> {
                    List<Option> listOption = new ArrayList<>();
                    for (int j = 0; j < arr.length; j++) {
                        if (j != questionPosition && j != rightAnswerPosition) {
                            listOption.add(new Option(arr[j]));
                        }
                    }
                    questions.add(new QuestionWithOptionAnswers(arr[questionPosition], new Answer(arr[rightAnswerPosition]), listOption));
                }
            }
        }

        return questions;
    }
}
