package ru.baranova.spring.service.data.book;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
public class BookServiceImplTestConfig {
    @MockBean
    private BookDao bookDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @SpyBean
    private BookService bookServiceImpl;

    @Bean
    public BookService bookServiceImpl(BookDao bookDaoJdbc
            , CheckService checkServiceImpl) {
        return new BookServiceImpl(bookDaoJdbc, checkServiceImpl);
    }
}
