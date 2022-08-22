package ru.baranova.spring.dao.entity.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@TestConfiguration
public class GenreDaoTestConfig {
    @Bean
    public GenreDao genreDao(EntityManager em) {
        return new GenreDaoJpa(em);
    }

}
