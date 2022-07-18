package ru.baranova.spring.services.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;
import ru.baranova.spring.services.setting.AppSettingService;
import ru.baranova.spring.services.test.TestService;

@Service
@RequiredArgsConstructor
public class ShellServiceImpl implements ShellService {
    private final AppSettingService appSettingServiceImpl;
    private final TestService testServiceImpl;

    public void chooseLanguage() {
        appSettingServiceImpl.chooseLanguage();
    }

    public void test() {
        testServiceImpl.test();
    }

}
