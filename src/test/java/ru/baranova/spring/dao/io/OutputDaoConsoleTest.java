package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@DisplayName("Test class OutputDaoConsole")
class OutputDaoConsoleTest {
    private final Path testFile = Paths.get("output_test_file.txt");
    private String expected = "This is the source of my output stream";
    private OutputDao outputDaoConsole;
    private List<String> actual;

    @Test
    @DisplayName("Test class OutputDaoReader, method outputLine")
    void shouldHaveCorrectListLine() {
        try {
            outputDaoConsole = new OutputDaoConsole(Files.newOutputStream(testFile));
            outputDaoConsole.outputLine(expected);
            actual = Files.readAllLines(testFile);
        } catch (IOException e) {
            log.error("Ошибка записи файла");
            actual = null;
        }
        Assertions.assertIterableEquals(List.of(expected), actual);
    }

    @Test
    @DisplayName("Test class OutputDaoReader, method outputFormatLine")
    void shouldHaveCorrectListFormatLine() {
        expected += 1;
        try {
            outputDaoConsole = new OutputDaoConsole(Files.newOutputStream(testFile));
            outputDaoConsole.outputFormatLine(expected, 1);
            actual = Files.readAllLines(testFile);
        } catch (IOException e) {
            log.error("Ошибка записи файла");
            actual = null;
        }
        Assertions.assertIterableEquals(List.of(expected), actual);
    }

    @AfterEach
    void tearDown() throws IOException {
        try {
            Files.delete(testFile);
        } catch (IOException e) {
            log.error("Ошибка удаления файла");
            throw e;
        }
    }
}