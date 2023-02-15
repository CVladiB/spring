package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class User {
    private String name;
    private String surname;
}