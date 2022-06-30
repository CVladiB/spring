package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class QuestionWithOptionAnswers implements Question {
    private String question;
    private Answer rightAnswer;
    private List<Option> optionAnswers;
}
