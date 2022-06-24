package ru.baranova.spring;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.baranova.spring.services.TestService;
import ru.baranova.spring.services.TestServiceImpl;

@Component
@ComponentScan()
@PropertySource("classpath:application.properties")
public class Application {


    @Autowired
    private static ApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(Application.class);
        TestService testService = context.getBean(TestServiceImpl.class);
        testService.test();
    }


}
