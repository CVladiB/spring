package ru.baranova.spring.dao;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.annotation.MethodArg;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.domain.Answer;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.message.LocaleService;

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
@ConfigurationProperties(prefix = "app.dao.question-dao-csv")
public class QuestionDaoCsv implements QuestionDao {
    private final ReaderDao readerDaoFile;
    private final LocaleProvider localeProviderImpl;
    private final LocaleService localeServiceImpl;
    private String delimiter;
    private int questionPosition;
    private int rightAnswerPosition;
    @Autowired
    private QuestionDao self;

    public String getPath() {
        return localeProviderImpl.getLanguageDescription().getPath();
    }

    @Override
    public List<Question> loadQuestion() {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(readerDaoFile.getResource(getPath())))) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            log.error(localeServiceImpl.getMessage("log.wrong-load-question"));
            lines = new ArrayList<>();
        }
        return self.parseStrings(lines);
    }

    @MethodArg
    @Override
    public List<Question> parseStrings(@NonNull List<String> lines) {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            String[] arr = line.split(delimiter);
            switch (arr.length) {
                case 0 -> log.error(localeServiceImpl.getMessage("log.wrong-parse_question"));
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
