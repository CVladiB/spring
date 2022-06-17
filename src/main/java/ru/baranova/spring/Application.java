package ru.baranova.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.baranova.spring.dao.QuestionDao;

public class Application {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        QuestionDao questionDao = context.getBean(QuestionDao.class);
        System.out.println(questionDao.loadQuestion().toString());

        context.close();
    }
}
