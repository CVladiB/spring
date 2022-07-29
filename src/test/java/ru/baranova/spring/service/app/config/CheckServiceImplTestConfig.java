package ru.baranova.spring.service.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.CheckServiceImpl;

@TestConfiguration
public class CheckServiceImplTestConfig {
    @Bean
    public CheckService CheckServiceImpl() {
        return new CheckServiceImpl();
    }
}
