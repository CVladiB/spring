package ru.baranova.spring.services;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.Question;

public interface QuestionService {
    void printQuestion(@NonNull Question question);

    String getAnswer(@NonNull Question question);

    boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer);
}
