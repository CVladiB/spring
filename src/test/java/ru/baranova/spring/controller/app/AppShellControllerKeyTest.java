package ru.baranova.spring.controller.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.CommandNotFound;
import org.springframework.shell.Shell;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AppShellController;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.service.app.AppService;

@SpringBootTest(classes = {AppShellControllerTestConfig.class, StopSearchConfig.class})
class AppShellControllerKeyTest {
    @Autowired
    private Shell shell;
    @Autowired
    private AppService appServiceImpl;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private AppShellController appShellController;
    @Autowired
    private AppShellControllerTestConfig config;

    @Test
    void stopApplication_correctKey() {
        shell.evaluate(() -> config.getStopApplication());
        Mockito.verify(appServiceImpl).stopApplication();
    }

    @Test
    void stopApplication_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(appServiceImpl, Mockito.never()).stopApplication();
    }

    @Test
    void echo_correctKey() {
        String test = "smth";
        String expected = test;
        String actual = shell.evaluate(() -> config.getEcho() + " " + test).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void echo_incorrectKey() {
        String test = "smth";
        Class<CommandNotFound> expected = org.springframework.shell.CommandNotFound.class;
        Class<?> actual = shell.evaluate(() -> "smthWrong" + " " + test).getClass();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void output_correctKey() {
        String test = "smth";
        shell.evaluate(() -> config.getOutput() + " " + test);
        Mockito.verify(outputDaoConsole).output(test);
    }

    @Test
    void output_incorrectKey() {
        String test = "smth";
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(outputDaoConsole, Mockito.never()).output(Mockito.any());
    }
}