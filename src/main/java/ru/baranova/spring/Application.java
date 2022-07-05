package ru.baranova.spring;

import ch.qos.logback.classic.util.ContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.baranova.spring.services.TestServiceImpl;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback-spring.xml");
        SpringApplication.run(Application.class, args)
                .getBean(TestServiceImpl.class)
                .test();
    }
}