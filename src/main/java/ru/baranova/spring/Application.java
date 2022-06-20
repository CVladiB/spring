package ru.baranova.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.services.AppService;

public class Application {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        AppService appService = context.getBean(AppService.class);
        appService.echo();
        QuestionDao questionDao = context.getBean(QuestionDao.class);
        questionDao.loadQuestion().forEach(System.out::println);

        context.close();
    }
}
