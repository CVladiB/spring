package ru.baranova.spring.dao.book;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@TestConfiguration
public class BookDaoTestConfig {
    @SpyBean
    public BookDao bookDao;

    @Bean
    public BookDao bookDao(NamedParameterJdbcOperations jdbc) {
        return new BookDaoJdbc(jdbc);
    }

}
