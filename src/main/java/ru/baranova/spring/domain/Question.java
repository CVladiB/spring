package ru.baranova.spring.domain;

import lombok.ToString;

import java.util.List;

public interface Question {
    String getQuestion();

    Answer getRightAnswer();

    List<Option> getOptionAnswers();
}
