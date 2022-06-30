package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Test class TestServiceImpl")
@ExtendWith(value = {SpringExtension.class})
@TestPropertySource("classpath:testServiceImplTest.properties")
@ContextConfiguration(classes = {TestServiceImpl.class, TestServiceImplTestConfig.class})
class TestServiceImplTest {
    @Autowired
    private TestServiceImpl testServiceImpl;

    @Test
    @DisplayName("Test class TestServiceImpl, method passTest, return true because enough correct answers")
    void shouldHaveTrue_EnoughCorrectAnswers() {
        testServiceImpl.setPartRightAnswers(50);
        boolean expected = testServiceImpl.passTest(5, 10);
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class TestServiceImpl, method passTest, return false because few correct answers")
    void shouldHaveFalse_FewCorrectAnswers() {
        testServiceImpl.setPartRightAnswers(50);
        boolean expected = testServiceImpl.passTest(4, 10);
        Assertions.assertFalse(expected);
    }
}