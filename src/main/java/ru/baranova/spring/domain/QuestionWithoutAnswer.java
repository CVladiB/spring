package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class QuestionWithoutAnswer implements Question {

    private String question;

    @Override
    public Answer getRightAnswer() {
        return null;
    }

    @Override
    public List<Option> getOptionAnswers() {
        return null;
    }
}
