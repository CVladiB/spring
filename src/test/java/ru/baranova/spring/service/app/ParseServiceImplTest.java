package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.app.config.ParseServiceImplTestConfig;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {ParseServiceImplTestConfig.class, StopSearchConfig.class})
class ParseServiceImplTest {
    @Autowired
    private ParseService parseServiceImpl;

    @Test
    void string__parseDashToNull_DashInput__correctParse() {
        String inputStr = "-";
        String actual = parseServiceImpl.parseDashToNull(inputStr);
        Assertions.assertNull(actual);
    }

    @Test
    void string__parseDashToNull_Null__correctParse() {
        String inputStr = null;
        String actual = parseServiceImpl.parseDashToNull(inputStr);
        Assertions.assertNull(actual);
    }

    @Test
    void string__parseDashToNull_AnotherSymbol__notParseCorrectEcho() {
        String inputStr = "a";
        String expected = inputStr;
        String actual = parseServiceImpl.parseDashToNull(inputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a,b,c", "a,,,,,,b,c,"})
    void string__parseLinesToListByComma__correctParse(String inputStr) {
        List<String> expected = List.of("a", "b", "c");
        List<String> actual = parseServiceImpl.parseLinesToListByComma(inputStr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void string__parseLinesToListByComma_Separator__correctParse_EmptyList() {
        String inputStr = ",,,,,,";
        List<String> expected = new ArrayList<>();
        List<String> actual = parseServiceImpl.parseLinesToListByComma(inputStr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void string__parseLinesToListByCommaByComma_StrWithoutSeparator__correctParse_Echo() {
        String inputStr = "a";
        List<String> expected = List.of("a");
        List<String> actual = parseServiceImpl.parseLinesToListByComma(inputStr);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void string__parseStringToInt__correctParse() {
        String inputStr = "55";
        Integer expected = 55;
        Integer actual = parseServiceImpl.parseStringToInt(inputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"55 ", "a", "5.5", "5,5"})
    void string__parseStringToInt_IncorrectInput__returnNull(String inputStr) {
        Assertions.assertNull(parseServiceImpl.parseStringToInt(inputStr));
    }
}