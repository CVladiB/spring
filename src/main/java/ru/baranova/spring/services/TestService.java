package ru.baranova.spring.services;

public interface TestService {

    void test();
    boolean passTest(double countCorrectAnswer, int numberOfQuestions);

}
