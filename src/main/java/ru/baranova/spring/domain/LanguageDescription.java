package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDescription {
    private String language;
    private String shortDescription;
    private String description;
    private String path;
}
