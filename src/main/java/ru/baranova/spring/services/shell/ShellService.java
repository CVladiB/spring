package ru.baranova.spring.services.shell;

import org.springframework.context.ApplicationContext;

public interface ShellService {
    void setContext(ApplicationContext context);

    void chooseLanguage();

    void test();

    String echo(String value);

    int stopApplication();
}
