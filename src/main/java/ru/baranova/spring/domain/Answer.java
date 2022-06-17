package ru.baranova.spring.domain;

import lombok.*;

@Data
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class Answer {
    private String answer;
}
