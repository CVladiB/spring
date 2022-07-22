package ru.baranova.spring.dao.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.baranova.spring.dao.book.book_genre.BookGenreDao;

@TestConfiguration
public class BookDaoTestConfig {
    @Autowired
    private NamedParameterJdbcOperations jdbc;
    @MockBean
    private BookGenreDao bookGenreDaoJdbc;

    @Bean
    public BookDao bookDaoJdbc(NamedParameterJdbcOperations jdbc, BookGenreDao bookGenreDaoJdbc) {
        return new BookDaoJdbc(jdbc,bookGenreDaoJdbc);
    }

}
