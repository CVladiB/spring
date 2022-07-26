package ru.baranova.spring.dao.output;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
class OutputDaoConsoleTest {
    private Path testFile;
    private OutputDao outputDaoConsole;

    @BeforeEach
    void beforeEach() {
        try {
            testFile = Paths.get("output_test_file.txt");
            outputDaoConsole = new OutputDaoConsole(Files.newOutputStream(testFile));
        } catch (IOException e) {
            log.error("Ошибка создания OutputStream");
        }
    }

    @Test
    void output() {
        String test = "Smth string bla-bla-bla";

        String expected = test;
        List<String> actual = null;
        outputDaoConsole.output(test);
        try {
            actual = Files.readAllLines(testFile);
        } catch (IOException e) {
            log.error("Ошибка записи файла");
        }


        Assertions.assertEquals(List.of(expected), actual);
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