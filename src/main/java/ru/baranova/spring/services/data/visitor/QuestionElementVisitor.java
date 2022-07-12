package ru.baranova.spring.services.data.visitor;

import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;


public interface QuestionElementVisitor<T> {
    T visit(QuestionWithOptionAnswers questionWithOptionAnswers);

    T visit(QuestionOneAnswer questionOneAnswer);

    T visit(QuestionWithoutAnswer questionWithoutAnswer);
}
