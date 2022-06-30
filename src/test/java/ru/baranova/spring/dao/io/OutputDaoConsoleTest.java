package ru.baranova.spring.dao.io;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Test class OutputDaoReader")
class OutputDaoConsoleTest {

    // Тестирование через ArgumentCaptor невозможнор ,т.к осуществляется проверка переданного аргумента.
    // Тест будет подготовлен на SpringBoot

    /*
    @Captor
    ArgumentCaptor<String> captor;

    @Disabled
    @Test
    @DisplayName("Test class OutputDaoReader, method outputLine")
    void outputLine() {
        String expected = "This is the source of my input stream";
        outputDaoConsole.outputLine(expected);
        Mockito.verify(outputDaoConsole).outputLine(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Disabled
    @Test
    @DisplayName("Test class OutputDaoReader, method outputFormatLine")
    void outputFormatLine() {
        String expected = "This is the source of my %s input stream";
        outputDaoConsole.outputFormatLine(expected, "second");
        Mockito.verify(outputDaoConsole).outputFormatLine(captor.capture());
        assertEquals("This is the source of my second input stream", captor.getValue());
    }
    */
}