package ru.baranova.spring.services.test;

public interface TestService {

    void test();

    boolean passTest(double countCorrectAnswer, int numberOfQuestions);

}
