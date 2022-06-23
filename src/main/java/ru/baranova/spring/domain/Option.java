package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
@EqualsAndHashCode
public class Option {
    private String option;
}
