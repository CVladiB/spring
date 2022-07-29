package ru.baranova.spring.dao.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@TestConfiguration
public class GenreDaoTestConfig {
    @Bean
    public GenreDao authorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        return new GenreDaoJdbc(jdbc);
    }

}
