package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.baranova.spring.services.data.visitor.QuestionElementParameterVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementVisitor;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class QuestionWithOptionAnswers implements Question {
    private String question;
    private Answer rightAnswer;
    private List<Option> optionAnswers;

    @Override
    public <T> T accept(QuestionElementVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, P> T accept(QuestionElementParameterVisitor<T, P> visitor, P parameter) {
        return visitor.visit(this, parameter);
    }

}
