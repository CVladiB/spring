package ru.baranova.spring.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class User {
    private String name;
    private String surname;

}
