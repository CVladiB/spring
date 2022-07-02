package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.baranova.spring.services.TestServiceImpl;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
                .getBean(TestServiceImpl.class)
                .test();
    }
}