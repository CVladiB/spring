package ru.baranova.spring.controller.app;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Disabled
    @Test
    void echo_correctKey() {
        // todo
        String test = "smth";
        shell.evaluate(() -> config.getEcho() + " " + test);
    }

    @Disabled
    @Test
    void echo_incorrectKey() {
        // todo
        shell.evaluate(() -> "smthWrong");
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