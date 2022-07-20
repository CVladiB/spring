package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.dao.genre.GenreDaoJdbc;

@SpringBootApplication
public class Library {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Library.class, args);
        GenreDao dao = ctx.getBean(GenreDaoJdbc.class);

    }
}
