package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.dao.genre.GenreDaoJdbc;
import ru.baranova.spring.domain.Genre;

@SpringBootApplication
public class Library {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Library.class, args);
        GenreDao dao = ctx.getBean(GenreDaoJdbc.class);
        dao.create(new Genre(5, "name", "desc"));
        System.out.println(dao.read());

        //dao.update(new Genre(1, "name", "desc"));
        //System.out.println(dao.read(1));

        dao.delete(1);
        System.out.println(dao.read());
    }
}
