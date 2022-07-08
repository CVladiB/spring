package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {CheckServiceImpl.class})
class CheckServiceImplTest {
    @Autowired
    private CheckService checkServiceImpl;
    @MockBean
    private LocaleService localeServiceImpl;

    @Test
    void shouldHaveCorrectInputNumber() {
        int expected = 5;
        int actual = checkServiceImpl.checkCorrectInputNumber("5", 4, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_empty() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("", 3, 4);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_null() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber(null, 3, 4);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_string() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("4g", 3, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_numberWithPoint() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("4.", 3, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_numberIsFloat() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("4.0", 3, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_big() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("5", 3, 4);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveIncorrectInputNumber_small() {
        int expected = -1;
        int actual = checkServiceImpl.checkCorrectInputNumber("0", 3, 4);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldHaveCorrectInputString() {
        Assertions.assertTrue(checkServiceImpl.checkCorrectInputStr("nam", 3));
    }

    @Test
    void shouldHaveCorrectInputString_empty() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr("", 3));
    }

    @Test
    void shouldHaveCorrectInputString_null() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr(null, 3));
    }

    @Test
    void shouldHaveIncorrectInputString_small() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr("nam", 4));
    }
}