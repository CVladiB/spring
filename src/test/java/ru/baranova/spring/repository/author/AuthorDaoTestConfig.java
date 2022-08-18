package ru.baranova.spring.repository.author;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@TestConfiguration
public class AuthorDaoTestConfig {
    @Bean
    public AuthorDao authorDao(EntityManager em) {
        return new AuthorDaoJpa(em);
    }

}
