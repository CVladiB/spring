package ru.baranova.spring.dao.reader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@DisplayName("class ReaderDaoFile")
@SpringBootTest
class ReaderDaoFileTest {
    private final String expected = "This is the source of my output stream";
    @Autowired
    private ReaderDao readerDaoFile;
    @Autowired
    private ApplicationContext ctx;
    private Path testFile;

    @BeforeEach
    void createNewFileForTest() throws IOException {
        try {
            testFile = Files.createFile(Paths.get("input_test_file.txt"));
            try (PrintWriter printWriter = new PrintWriter(testFile.toAbsolutePath().toString())) {
                printWriter.print(expected);
                printWriter.flush();
            }
        } catch (IOException e) {
            log.error("Ошибка создания файла");
            throw e;
        }
        log.info(String.valueOf(Files.exists(testFile)));
        log.info(testFile.toAbsolutePath().toString());
    }

    @Test
    @DisplayName("class ReaderDaoFile method getResource, return right string from file")
    void shouldHaveCorrectReadFile() {
        String actual;
        try (InputStream stream = readerDaoFile.getResource("file:" + testFile.toAbsolutePath())) {
            actual = new String(stream.readAllBytes());
        } catch (IOException e) {
            log.error("Ошибка получения потока");
            actual = null;
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("class ReaderDaoFile method getResource, return null if wrong path")
    void shouldHaveNull() {
        InputStream actual = readerDaoFile.getResource(null);
        Assertions.assertNull(actual);
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
