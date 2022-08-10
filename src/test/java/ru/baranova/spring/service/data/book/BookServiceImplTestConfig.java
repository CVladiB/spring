package ru.baranova.spring.service.data.book;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
public class BookServiceImplTestConfig {
    @MockBean
    private BookDao bookDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private AppService appServiceImpl;

    @Bean
    public BookService bookServiceImpl(BookDao bookDaoJdbc
            , CheckService checkServiceImpl, AppService appServiceImpl) {
        return new BookServiceImpl(bookDaoJdbc, checkServiceImpl, appServiceImpl);
    }
}
