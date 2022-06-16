package ru.baranova.spring.dao;

import ru.baranova.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDaoCsv implements QuestionDao {



    @Override
    public List<Question> loadQuestion() {
        return new ArrayList<>();
    }
}
