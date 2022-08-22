package ru.baranova.spring.service.data.author;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.entity.author.AuthorDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

@TestConfiguration
public class AuthorServiceImplTestConfig {
    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private CheckService checkService;
    @MockBean
    private ParseService parseService;

    @Bean
    public AuthorService authorService(AuthorDao authorDao
            , CheckService checkService
            , ParseService parseService) {
        return new AuthorServiceImpl(authorDao, checkService, parseService);
    }
}
