package ru.baranova.spring.dao.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@TestConfiguration
public class AuthorDaoTestConfig {
    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Bean
    public AuthorDao authorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        return new AuthorDaoJdbc(jdbc);
    }

}
