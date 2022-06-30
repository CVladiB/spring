package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.PrintWriter;

@Slf4j
@Component
public class OutputDaoConsole implements OutputDao {

    private final PrintWriter writer;

    public OutputDaoConsole(OutputStream systemOutputStream) {
        writer = new PrintWriter(systemOutputStream, true);
    }

    @Override
    public void outputLine(String line) {
        writer.println(line);
    }

    @Override
    public void outputFormatLine(String formatLine, Object... args) {
        log.info("{}, {}", formatLine, args);
        writer.printf(formatLine, args);
    }
}
