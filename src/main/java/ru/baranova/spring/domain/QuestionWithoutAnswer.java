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
@RequiredArgsConstructor
@Component
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
