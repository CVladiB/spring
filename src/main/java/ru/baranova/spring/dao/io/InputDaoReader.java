package ru.baranova.spring.dao.io;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Data
public class InputDaoReader implements InputDao {

    @Nullable
    @Override
    public String inputLine() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.readLine();
        } catch (IOException e) {
            log.error("Не удалось прочесть");
            return null;
        }
    }
}
