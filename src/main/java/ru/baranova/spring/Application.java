package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.baranova.spring.services.test.TestServiceImpl;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
                .getBean(TestServiceImpl.class)
                .test();

    }
}