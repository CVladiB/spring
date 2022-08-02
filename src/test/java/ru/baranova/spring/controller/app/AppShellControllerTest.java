package ru.baranova.spring.controller.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AppShellController;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.service.app.AppService;

@SpringBootTest(classes = {AppShellControllerTestConfig.class, StopSearchConfig.class})
class AppShellControllerTest {
    @Autowired
    private AppService appServiceImpl;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private AppShellController appShellController;
    @Autowired
    private AppShellControllerTestConfig config;

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void stopApplication_correctStopApp() {
        Mockito.when(appServiceImpl.stopApplication()).thenReturn(0);
        String expected = "Correct exit";
        String actual = appShellController.stopApplication();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void stopApplication_correctErrorStopApp() {
        Mockito.when(appServiceImpl.stopApplication()).thenReturn(1);
        String expected = "Error";
        String actual = appShellController.stopApplication();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void echo_correctReturnInput() {
        String test = "smth";
        String expected = test;
        String actual = appShellController.echo(test);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void output_correctReturnInput() {
        String test = "smth";
        Mockito.doAnswer(invocation -> {
            config.getWriter().println(test);
            return null;
        }).when(outputDaoConsole).output(test);
        String expected = test + "\r\n";
        appShellController.output(test);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}