package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Slf4j
@Component
public class OutputDaoConsole implements OutputDao {
    @Override
    public void outputLine(String line) {
        PrintWriter writer = new PrintWriter(System.out);
        writer.print(line);
        writer.flush();
    }
}
