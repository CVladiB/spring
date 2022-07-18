package ru.baranova.spring.services.shell;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.baranova.spring.services.setting.AppSettingService;
import ru.baranova.spring.services.test.TestService;

@Service
@RequiredArgsConstructor
public class ShellServiceImpl implements ShellService {
    private final AppSettingService appSettingServiceImpl;
    private final TestService testServiceImpl;
    @Setter
    @Autowired
    private ApplicationContext context;

    @Override
    public void chooseLanguage() {
        appSettingServiceImpl.chooseLanguage();
    }

    @Override
    public void test() {
        testServiceImpl.test();
    }

    @Override
    public String echo(String value) {
        return value;
    }

    @Override
    public int stopApplication() {
        return SpringApplication.exit(context);
    }
}
