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
public class QuestionWithOptionAnswers implements Question {
    private String question;
    private Answer rightAnswer;
    private List<Option> optionAnswers;
}
