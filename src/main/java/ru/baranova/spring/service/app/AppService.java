package ru.baranova.spring.service.app;

import org.springframework.context.ApplicationContext;

public interface AppService {
    void setContext(ApplicationContext context);
    int stopApplication();
}
