package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class User {
    private String name;
    private String surname;
}
