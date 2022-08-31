package ru.baranova.spring.controller.app;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.AppController;
import ru.baranova.spring.service.app.AppService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Getter
@TestConfiguration
public class AppShellControllerTestConfig {
    @MockBean
    private AppService appService;
    private ByteArrayOutputStream out;
    private PrintWriter writer;
    private String output;
    private String echo;
    private String stopApplication;

    @Bean
    public AppController appShellController(AppService appService) {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        output = "out";
        echo = "echo";
        stopApplication = "sa";
        return new AppController(appService);
    }

}
