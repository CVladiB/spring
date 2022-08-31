package ru.baranova.spring.controller.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.app.AppService;

@SpringBootTest(classes = {AppRestControllerTestConfig.class, StopSearchConfig.class})
class AppControllerTest {
    @Autowired
    private AppService appService;
    @Autowired
    private AppController appController;
    @Autowired
    private AppRestControllerTestConfig config;

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void stopApplication_correctStopApp() {
        Mockito.when(appService.stopApplication()).thenReturn(0);
        String expected = "Correct exit";
        String actual = appController.stopApplication();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void stopApplication_correctErrorStopApp() {
        Mockito.when(appService.stopApplication()).thenReturn(1);
        String expected = "Error";
        String actual = appController.stopApplication();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void echo_correctReturnInput() {
        String test = "smth";
        String expected = test;
        String actual = appController.echo(test);
        Assertions.assertEquals(expected, actual);
    }
}
