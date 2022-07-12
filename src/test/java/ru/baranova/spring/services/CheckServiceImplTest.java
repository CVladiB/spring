package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.services.message.LocaleService;

@DisplayName("Test class CheckServiceImpl")
@SpringBootTest(classes = {CheckServiceImpl.class, ComponentScanStopConfig.class})
class CheckServiceImplTest {
    private final int incorrectReturnNumber = -1;
    private final int minAnswerSymbol = 3;
    private final int maxAnswerSymbol = 5;
    @Autowired
    private CheckService checkServiceImpl;
    @MockBean
    private LocaleService localeServiceImpl;

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, correct max")
    void shouldHaveCorrectInputNumber() {
        int expected = 5;
        int actual = checkServiceImpl.checkCorrectInputNumber(Integer.toString(maxAnswerSymbol), minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect empty")
    void shouldHaveIncorrectInputNumber_empty() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber("", minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect null")
    void shouldHaveIncorrectInputNumber_null() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber(null, minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect strung input")
    void shouldHaveIncorrectInputNumber_string() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber("4g", minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect int with point input")
    void shouldHaveIncorrectInputNumber_numberWithPoint() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber("4.", minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect float input")
    void shouldHaveIncorrectInputNumber_numberIsFloat() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber("4.0", minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect more than max")
    void shouldHaveIncorrectInputNumber_big() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber(Integer.toString(maxAnswerSymbol + 1), minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputNumber, incorrect less than min")
    void shouldHaveIncorrectInputNumber_small() {
        int expected = incorrectReturnNumber;
        int actual = checkServiceImpl.checkCorrectInputNumber(Integer.toString(minAnswerSymbol), minAnswerSymbol, maxAnswerSymbol);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputStr, correct min")
    void shouldHaveCorrectInputString() {
        Assertions.assertTrue(checkServiceImpl.checkCorrectInputStr("nam", minAnswerSymbol));
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputStr, incorrect empty")
    void shouldHaveCorrectInputString_empty() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr("", minAnswerSymbol));
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputStr, incorrect null")
    void shouldHaveCorrectInputString_null() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr(null, minAnswerSymbol));
    }

    @Test
    @DisplayName("Test class CheckServiceImpl, method checkCorrectInputStr, incorrect small input")
    void shouldHaveIncorrectInputString_small() {
        Assertions.assertFalse(checkServiceImpl.checkCorrectInputStr("na", minAnswerSymbol));
    }
}