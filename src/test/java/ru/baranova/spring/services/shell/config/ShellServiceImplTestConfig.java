package ru.baranova.spring.services.shell.config;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.services.setting.AppSettingService;
import ru.baranova.spring.services.shell.ShellService;
import ru.baranova.spring.services.shell.ShellServiceImpl;
import ru.baranova.spring.services.test.TestService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@TestConfiguration
public class ShellServiceImplTestConfig {
    @MockBean
    private AppSettingService appSettingServiceImpl;
    @MockBean
    private TestService testServiceImpl;
    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;


    @Bean
    public ShellService shellServiceImpl(AppSettingService appSettingServiceImpl, TestService testServiceImpl) {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new ShellServiceImpl(appSettingServiceImpl, testServiceImpl);
    }
}