package ru.baranova.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.baranova.spring.services.TestService;
import ru.baranova.spring.services.TestServiceImpl;

@Component
@ComponentScan
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        TestService testService = context.getBean(TestServiceImpl.class);
        testService.test();
        context.close();
    }


}
