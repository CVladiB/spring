package ru.baranova.spring.service.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.CheckServiceImpl;

@TestConfiguration
public class CheckServiceImplTestConfig {
    @MockBean
    private AppService appServiceImpl;

    @Bean
    public CheckService CheckServiceImpl(AppService appServiceImpl) {
        return new CheckServiceImpl(appServiceImpl);
    }
}
