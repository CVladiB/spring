package ru.baranova.spring.services;

import ru.baranova.spring.domain.Question;

public interface QuestionService {
    void printQuestion (Question question);
    boolean checkCorrect(Question question, int inputAnswer);

    int getAnswer(Question question);
}
