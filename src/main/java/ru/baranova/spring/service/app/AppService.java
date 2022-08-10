package ru.baranova.spring.service.app;

import org.springframework.context.ApplicationContext;

import java.util.function.Supplier;

public interface AppService {
    void setContext(ApplicationContext context);

    int stopApplication();

    <T> T evaluate(Supplier<T> predicate);
}
