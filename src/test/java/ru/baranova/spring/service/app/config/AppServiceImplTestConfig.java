package ru.baranova.spring.service.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.AppServiceImpl;

@TestConfiguration
public class AppServiceImplTestConfig {
    @Bean
    public AppService appService() {
        return new AppServiceImpl();
    }
}
