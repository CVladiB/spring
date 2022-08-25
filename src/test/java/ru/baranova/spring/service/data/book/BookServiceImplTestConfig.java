package ru.baranova.spring.service.data.book;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.repository.entity.book.BookDao;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
@ConfigurationProperties(prefix = "app.service.book-service")
public class BookServiceImplTestConfig {
    @MockBean
    private BookDao bookDao;
    @MockBean
    private CheckService checkService;

    @Bean
    public BookService bookService(BookDao bookDao
            , CheckService checkService) {
        return new BookServiceImpl(bookDao, checkService);
    }
}
