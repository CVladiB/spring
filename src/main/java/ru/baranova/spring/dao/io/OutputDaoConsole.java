package ru.baranova.spring.dao.io;

import org.springframework.stereotype.Component;
import ru.baranova.spring.annotation.MethodArg;

import java.io.OutputStream;
import java.io.PrintWriter;

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

    @MethodArg
    @Override
    public void outputFormatLine(String formatLine, Object... args) {
        writer.printf(formatLine, args);
    }
}
