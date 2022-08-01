package ru.baranova.spring.service.data.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.book.BookServiceImpl;


@TestConfiguration
public class BookServiceImplTestConfig {
    @MockBean
    private BookDao bookDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private ParseService parseServiceImpl;
    @SpyBean
    private BookService bookServiceImpl;

    @Bean
    public BookService bookServiceImpl(BookDao bookDaoJdbc
            , CheckService checkServiceImpl
            , ParseService parseServiceImpl) {
        return new BookServiceImpl(bookDaoJdbc, checkServiceImpl, parseServiceImpl);
    }
}
