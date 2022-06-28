package ru.baranova.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString(includeFieldNames = false)
@EqualsAndHashCode
public class Question {
    private String question;
    private Answer rightAnswer;
    private List<Option> optionAnswers;

    public Question(String question) {
        this(question, new Answer("Unanswered question"), Arrays.asList(new Option("Unanswered question")));
    }

    public Question(String question, Answer rightAnswer) {
        this(question, rightAnswer, Arrays.asList(new Option("Open-ended question")));
    }

    public Question(String question, Answer rightAnswer, List<Option> optionAnswers) {
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.optionAnswers = optionAnswers;
    }
}
