package ru.baranova.spring.dao.io;

import lombok.SneakyThrows;
import net.bytebuddy.build.BuildLogger;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.dao.io.config.InputDaoReaderConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

@DisplayName("Test class InputDaoReader")
@ActiveProfiles("input-dao-reader")
@SpringBootTest(classes = InputDaoReaderConfig.class)
class InputDaoReaderTest {

    @Autowired
    private InputDaoReader testInputDaoReader;
    @Autowired
    private InputDaoReader testInputDaoReaderNull;

    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveCorrectString() {
        String expected = "This is the source of my input stream";
        String actual = testInputDaoReader.inputLine();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveNull() {
        String actual = testInputDaoReaderNull.inputLine();
        Assertions.assertNull(actual);
    }
}