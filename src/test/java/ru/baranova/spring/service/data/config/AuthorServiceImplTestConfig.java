package ru.baranova.spring.service.data.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.author.AuthorServiceImpl;

@TestConfiguration
public class AuthorServiceImplTestConfig {
    @MockBean
    private AuthorDao authorDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private ParseService parseServiceImpl;
    @SpyBean
    private AuthorService authorServiceImpl;

    @Bean
    public AuthorService authorServiceImpl(AuthorDao authorDaoJdbc
            , CheckService checkServiceImpl
            , ParseService parseServiceImpl) {
        return new AuthorServiceImpl(authorDaoJdbc, checkServiceImpl, parseServiceImpl);
    }
}
