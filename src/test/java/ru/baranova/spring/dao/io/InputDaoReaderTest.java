package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.dao.io.config.InputDaoReaderConfig;

import java.io.IOException;
import java.io.InputStream;

@DisplayName("Test class InputDaoReader")
@ActiveProfiles("input-dao-reader")
@SpringBootTest(classes = {InputDaoReaderConfig.class, ComponentScanStopConfig.class})
class InputDaoReaderTest {
    @Autowired
    private InputDao testInputDaoReader;

    @Autowired
    private InputDao testInputDaoReaderException;
    @Autowired
    private InputStream byteArrayInputStreamException;


    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveCorrectString() {
        String expected = "This is the source of my input stream";
        String actual = testInputDaoReader.inputLine();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveNull() throws IOException {
        Mockito.when(byteArrayInputStreamException.read()).thenThrow(new IOException());
        String actual = testInputDaoReaderException.inputLine();
        Assertions.assertNull(actual);
    }
}