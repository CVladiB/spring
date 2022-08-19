package ru.baranova.spring.repository.book;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

@TestConfiguration
public class BookDaoTestConfig {

    @Bean
    public BookDao bookDao(EntityManager em) {
        return new BookDaoJpa(em);
    }

}
