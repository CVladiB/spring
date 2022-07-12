package ru.baranova.spring.domain;

import ru.baranova.spring.services.data.visitor.QuestionElementParameterVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementVisitor;

import java.util.List;

public interface Question {
    String getQuestion();

    Answer getRightAnswer();

    List<Option> getOptionAnswers();

    <T> T accept(QuestionElementVisitor<T> visitor);

    <T, P> T accept(QuestionElementParameterVisitor<T, P> visitor, P parameter);
}
