package ru.baranova.spring.dao.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@TestConfiguration
public class BookDaoTestConfig {
    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Bean
    public BookDao bookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        return new BookDaoJdbc(jdbc);
    }

}
