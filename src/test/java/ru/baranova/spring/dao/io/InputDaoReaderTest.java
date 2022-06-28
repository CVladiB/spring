package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.QuestionDaoCsv;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@DisplayName("Test class InputDaoReader")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
class InputDaoReaderTest {
    private InputDao inputDaoReader;

    @BeforeEach
    void setUp() {
        inputDaoReader = new InputDaoReader(System.in);
    }

    @Disabled
    // todo
    @Test
    @DisplayName("Test class InputDaoReader, method inputLine")
    void shouldHaveCorrectString() {
        String expected = "This is the source of my input stream \n";
        String actual = String.valueOf(Mockito.when(inputDaoReader.inputLine()).thenReturn(expected));

        Assertions.assertEquals(expected,actual);
    }

}