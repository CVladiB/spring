package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@DisplayName("Test class OutputDaoReader")
@SpringBootTest
class OutputDaoConsoleTest {
    private final Path testFile = Paths.get("output_test_file.txt");

    @Test
    @DisplayName("Test class OutputDaoReader, method outputLine")
    void outputLine() throws IOException {
        String expected = "This is the source of my output stream";
        OutputDao outputDaoConsole = new OutputDaoConsole(Files.newOutputStream(testFile));
        outputDaoConsole.outputLine(expected);
        Assertions.assertIterableEquals(List.of(expected),Files.readAllLines(testFile));
    }
    @AfterEach
    void tearDown() throws IOException {
        Files.delete(testFile);
        log.error("Ошибка удаления файла");
    }
}