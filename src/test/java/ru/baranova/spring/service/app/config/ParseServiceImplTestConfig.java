package ru.baranova.spring.service.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.app.ParseServiceImpl;

@TestConfiguration
public class ParseServiceImplTestConfig {
    @Bean
    public ParseService parseService() {
        return new ParseServiceImpl();
    }
}
