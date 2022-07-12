package ru.baranova.spring.services.data.visitor;

import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;


public interface QuestionElementParameterVisitor<T, P> {
    T visit(QuestionWithOptionAnswers questionWithOptionAnswers, P parameter);

    T visit(QuestionOneAnswer questionOneAnswer, P parameter);

    T visit(QuestionWithoutAnswer questionWithoutAnswer, P parameter);
}
