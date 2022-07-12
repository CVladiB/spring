package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.baranova.spring.services.setting.AppSettingServiceImpl;
import ru.baranova.spring.services.test.TestServiceImpl;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        applicationContext.getBean(AppSettingServiceImpl.class).chooseLanguage();
        applicationContext.getBean(TestServiceImpl.class).test();
    }
}