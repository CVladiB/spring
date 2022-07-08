package ru.baranova.spring.dao.io.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.InputDaoReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Getter
@Setter
@TestConfiguration
public class InputDaoReaderConfig {

    private String value = "This is the source of my input stream";

    @Bean
    public InputDao testInputDaoReader(InputStream byteArrayInputStream) {
        return new InputDaoReader(byteArrayInputStream);
    }

    @Bean
    public InputStream byteArrayInputStream() {
        return new ByteArrayInputStream(value.getBytes());
    }
}
