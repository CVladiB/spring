package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

@DisplayName("Test class InputDaoReader")
class InputDaoReaderTest {

    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveCorrectString() {
        String expected = "This is the source of my input stream";
        String actual = new InputDaoReader(new ByteArrayInputStream(expected.getBytes())).inputLine();

        Assertions.assertEquals(expected, actual);
    }

}