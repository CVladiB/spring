package ru.baranova.spring.dao.output;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
//@TestConfiguration
// todo
public class OutputDaoConsoleTestConfig {
    @Getter
    private Path testFile;

    @Bean
    public OutputStream fileOutputStream() {
        try {
            testFile = Paths.get("output_test_file.txt");
            return Files.newOutputStream(testFile);
        } catch (IOException e) {
            log.error("Ошибка создания OutputStream");
            return null;
        }
    }

    @Bean
    public OutputDao outputDaoConsole(OutputStream fileOutputStream) {
        return new OutputDaoConsole(fileOutputStream);
    }

}
