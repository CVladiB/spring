package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.app.config.ParseServiceImplTestConfig;

@SpringBootTest(classes = {ParseServiceImplTestConfig.class, StopSearchConfig.class})
class ParseServiceImplTest {
    @Autowired
    private ParseService parseService;

    @Test
    void string__parseDashToNull_DashInput__correctParse() {
        String inputStr = "-";
        String actual = parseService.parseDashToNull(inputStr);
        Assertions.assertNull(actual);
    }

    @Test
    void string__parseDashToNull_Null__correctParse() {
        String inputStr = null;
        String actual = parseService.parseDashToNull(inputStr);
        Assertions.assertNull(actual);
    }

    @Test
    void string__parseDashToNull_AnotherSymbol__notParseCorrectEcho() {
        String inputStr = "a";
        String expected = inputStr;
        String actual = parseService.parseDashToNull(inputStr);
        Assertions.assertEquals(expected, actual);
    }
}