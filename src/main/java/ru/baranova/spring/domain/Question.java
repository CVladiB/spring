package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@ToString(includeFieldNames = false, exclude = "rightAnswer")
public class Question {
    private String question;
    private Answer rightAnswer;
    private List<Option> optionAnswers;

    /**
     * Unanswered question
     * */
    public Question(String question) {
        this.question = question;
        this.rightAnswer = new Answer("Unanswered question");
        this.optionAnswers = Arrays.asList(new Option("Unanswered question"));
    }

    /**
    * Open-ended question
    * */
    public Question(String question, Answer rightAnswer) {
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.optionAnswers = Arrays.asList(new Option("Open-ended question"));
    }

    public Question(String question, Answer rightAnswer, List<Option> optionAnswers) {
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.optionAnswers = optionAnswers;
    }
}
