package ru.baranova.spring.dao.io;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
public class InputDaoReader implements InputDao {

    private final BufferedReader reader;

    public InputDaoReader (InputStream systemInputStream) {
        this.reader = new BufferedReader(new InputStreamReader(systemInputStream));
    }

    @Nullable
    @Override
    public String inputLine() {
        try {
            String value = null;
            while (value == null) {
                value = reader.readLine();
            }
            return value;
        } catch (IOException e) {
            log.error("Не удалось прочесть");
            return null;
        }
    }
}
