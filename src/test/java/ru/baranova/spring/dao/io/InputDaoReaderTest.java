package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.dao.io.config.InputDaoReaderConfig;

@DisplayName("Test class InputDaoReader")
@ActiveProfiles("input-dao-reader")
@SpringBootTest(classes = InputDaoReaderConfig.class)
class InputDaoReaderTest {

    @Autowired
    private InputDaoReader testInputDaoReader;

    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveCorrectString() {
        String expected = "This is the source of my input stream";
        String actual = testInputDaoReader.inputLine();
        Assertions.assertEquals(expected, actual);
    }

    @Disabled
    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveNull() {
        String actual = testInputDaoReader.inputLine();
        Assertions.assertNull(actual);
    }

}