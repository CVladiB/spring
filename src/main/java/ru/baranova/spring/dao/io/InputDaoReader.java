package ru.baranova.spring.dao.io;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class InputDaoReader implements InputDao {
    private final BufferedReader reader;

    public InputDaoReader(InputStream systemInputStream) {
        reader = new BufferedReader(new InputStreamReader(systemInputStream));
    }

    @Override
    public String input() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
