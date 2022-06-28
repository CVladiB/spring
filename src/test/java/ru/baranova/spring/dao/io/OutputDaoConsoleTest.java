package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test class OutputDaoReader")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
class OutputDaoConsoleTest {
    @MockBean
    private OutputDao outputDaoConsole;
    @Captor
    ArgumentCaptor<String> captor;

    @Test
    @DisplayName("Test class OutputDaoReader, method outputLine")
    void outputLine() {
        String expected = "This is the source of my input stream";
        outputDaoConsole.outputLine(expected);
        Mockito.verify(outputDaoConsole).outputLine(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    // todo
    @Disabled
    @Test
    @DisplayName("Test class OutputDaoReader, method outputFormatLine")
    void outputFormatLine() {
        String expected = "This is the source of my %s input stream";
        outputDaoConsole.outputFormatLine(expected, "second");
        Mockito.verify(outputDaoConsole).outputFormatLine(captor.capture());
        assertEquals("This is the source of my second input stream", captor.getValue());
    }
}