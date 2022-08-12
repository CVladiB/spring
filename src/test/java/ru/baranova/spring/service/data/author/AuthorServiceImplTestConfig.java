package ru.baranova.spring.service.data.author;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

@TestConfiguration
public class AuthorServiceImplTestConfig {
    @MockBean
    private AuthorDao authorDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private ParseService parseServiceImpl;


    @Bean
    public AuthorService authorServiceImpl(AuthorDao authorDaoJdbc
            , CheckService checkServiceImpl
            , ParseService parseServiceImpl) {
        return new AuthorServiceImpl(authorDaoJdbc, checkServiceImpl, parseServiceImpl);
    }
}
