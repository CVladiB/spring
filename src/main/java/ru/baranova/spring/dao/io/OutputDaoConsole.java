package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Slf4j
@Component
public class OutputDaoConsole implements OutputDao {

    private final PrintWriter writer = new PrintWriter(System.out, true);

    @Override
    public void outputLine(String line) {
        writer.println(line);
    }
}
