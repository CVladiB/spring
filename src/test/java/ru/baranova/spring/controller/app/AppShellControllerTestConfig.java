package ru.baranova.spring.controller.app;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.AppShellController;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.service.app.AppService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Getter
@TestConfiguration
public class AppShellControllerTestConfig {
    @MockBean
    private AppService appServiceImpl;
    @MockBean
    private OutputDao outputDaoConsole;
    private ByteArrayOutputStream out;
    private PrintWriter writer;
    private String output;
    private String echo;
    private String stopApplication;

    @Bean
    public AppShellController appShellController(AppService appServiceImpl, OutputDao outputDaoConsole) {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        output = "out";
        echo = "echo";
        stopApplication = "sa";
        return new AppShellController(appServiceImpl, outputDaoConsole);
    }

}
