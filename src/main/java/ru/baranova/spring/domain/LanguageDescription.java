package ru.baranova.spring.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class LanguageDescription {
    private String language;
    private String shortDescription;
    private String description;
    private String path;
}
