package ru.baranova.spring.services.data;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.Question;

public interface QuestionService {
    void printQuestion(@NonNull Question question);

    String setAndGetAnswer(@NonNull Question question);

    boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer);
}
