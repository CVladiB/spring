package ru.baranova.spring.dao.output;

import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.PrintWriter;

@Component
public class OutputDaoConsole implements OutputDao {
    private final PrintWriter writer;

    public OutputDaoConsole(OutputStream systemOutputStream) {
        writer = new PrintWriter(systemOutputStream, true);
    }

    @Override
    public void output(String str) {
        writer.print(str);
    }
}
