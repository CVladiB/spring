package ru.baranova.spring.services.test;

public interface TestService {
    void setPartRightAnswers(int partRightAnswers);

    void test();

    boolean passTest(double countCorrectAnswer, int numberOfQuestions);

}
