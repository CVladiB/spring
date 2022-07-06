package ru.baranova.spring.dao;

import org.springframework.lang.NonNull;
import ru.baranova.spring.annotation.MethodArg;
import ru.baranova.spring.domain.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> loadQuestion();

    @MethodArg
    List<Question> parseStrings(@NonNull List<String> lines);
}
