package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.StaticApplicationContext;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.app.config.AppServiceImplTestConfig;

@SpringBootTest(classes = {AppServiceImplTestConfig.class, StopSearchConfig.class})
class AppServiceImplTest {
    @Autowired
    private AppService appServiceImpl;


    @Test
    void stopApplication() {
        appServiceImpl.setContext(new StaticApplicationContext());
        int expected = 0;
        int actual = appServiceImpl.stopApplication();
        Assertions.assertEquals(expected, actual);
    }
}