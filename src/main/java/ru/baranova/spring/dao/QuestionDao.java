package ru.baranova.spring.dao;

import ru.baranova.spring.domain.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> loadQuestion();
}
