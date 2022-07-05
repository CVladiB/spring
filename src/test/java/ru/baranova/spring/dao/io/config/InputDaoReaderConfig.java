package ru.baranova.spring.dao.io.config;


import lombok.Getter;
import lombok.Setter;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.InputDaoReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "app.dao.input-dao-reader")
public class InputDaoReaderConfig {

    private String value;

    @Bean
    public InputDao testInputDaoReader(InputStream byteArrayInputStream) {
        return new InputDaoReader(byteArrayInputStream);
    }

    @Bean
    public InputDao testInputDaoReaderNull(InputStream byteArrayInputStreamNull) {
        return new InputDaoReader(byteArrayInputStreamNull);
    }


    @Bean
    public InputStream byteArrayInputStream() {
        return new ByteArrayInputStream(value.getBytes());
    }

    @Bean
    public InputStream byteArrayInputStreamNull() {
        return Mockito.mock(ByteArrayInputStream.class);
    }
}
