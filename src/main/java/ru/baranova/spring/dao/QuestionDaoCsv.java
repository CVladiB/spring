package ru.baranova.spring.dao;

import lombok.Data;
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

    private String path;
    private String delimiter;

    private final ReaderDao readerDao;

    /**
     * демонстраиция конфигурации бинов разных реализаций
     */
    private final ReaderDao readerDaoImpl;

    public QuestionDaoCsv(ReaderDao readerDao, ReaderDao readerDaoImpl) {
        this.readerDao = readerDao;
        this.readerDaoImpl = readerDaoImpl;
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

    private List<Question> parseStrings(@NonNull List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            String[] arr = line.split(delimiter);
            if (arr.length == 1) {
                questions.add(new Question(arr[0]));
            } else if (arr.length == 2) {
                questions.add(new Question(arr[0], new Answer(arr[1])));
            } else if (arr.length > 2) {
                List<Option> listOption = new ArrayList<>();
                for (int j = 2; j < arr.length; j++) {
                    listOption.add(new Option(arr[j]));
                }
                questions.add(new Question(arr[0], new Answer(arr[1]), listOption));
            } else {
                return new ArrayList<>();
            }
        }
        return questions;
    }
}
