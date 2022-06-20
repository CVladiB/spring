package ru.baranova.spring.dao.io;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@Data
public class OutputDaoConsole implements OutputDao {
    @Override
    public void outputLine(String line) {
        PrintWriter writer = new PrintWriter(System.out, true);
        writer.println(line);
    }
}
