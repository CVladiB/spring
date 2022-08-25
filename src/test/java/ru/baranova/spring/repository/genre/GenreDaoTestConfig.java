package ru.baranova.spring.repository.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.repository.entity.genre.GenreDao;
import ru.baranova.spring.repository.entity.genre.GenreDaoJpa;

import javax.persistence.EntityManager;

@TestConfiguration
public class GenreDaoTestConfig {
    @Bean
    public GenreDao genreDao(EntityManager em) {
        return new GenreDaoJpa(em);
    }

}
