package ru.baranova.spring.dao.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.dao.author.AuthorDaoJdbc;

@TestConfiguration
public class GenreDaoTestConfig {
    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Bean
    public GenreDao authorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        return new GenreDaoJdbc(jdbc);
    }

}
