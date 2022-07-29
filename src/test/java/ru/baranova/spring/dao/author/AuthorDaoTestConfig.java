package ru.baranova.spring.dao.author;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@TestConfiguration
public class AuthorDaoTestConfig {
    @Bean
    public AuthorDao authorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        return new AuthorDaoJdbc(jdbc);
    }

}
