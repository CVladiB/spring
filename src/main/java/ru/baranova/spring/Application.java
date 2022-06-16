package ru.baranova.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");
//        QuestionDao questionDao = context.getBean(QuestionDao.class);
//        System.out.println(questionDao.loadQuestion());
    }
}
