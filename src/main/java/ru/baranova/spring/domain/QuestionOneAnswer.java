package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class QuestionOneAnswer implements Question {

    private String question;
    private Answer rightAnswer;

    @Override
    public List<Option> getOptionAnswers() {
        return null;
    }
}
