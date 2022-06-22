package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class Answer {
    private String answer;

}
