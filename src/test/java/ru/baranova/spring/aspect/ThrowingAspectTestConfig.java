package ru.baranova.spring.aspect;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.dao.author.AuthorDaoJdbc;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.author.AuthorServiceImpl;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@TestConfiguration
public class ThrowingAspectTestConfig {
    @MockBean
    private CheckService checkService;
    @MockBean
    private ParseService parseService;
    @MockBean
    private EntityPrintVisitor printer;

    @Bean
    public ThrowingAspect throwingAspect() {
        return new ThrowingAspect();
    }

    @Bean
    public AuthorDao authorDao(NamedParameterJdbcOperations jdbc) {
        return new AuthorDaoJdbc(jdbc);
    }

    @Bean
    public AuthorService authorService(AuthorDao authorDao
            , CheckService checkService
            , ParseService parseService) {
        return new AuthorServiceImpl(authorDao, checkService, parseService);
    }

    @Bean
    public AuthorShellController authorShellController(AuthorService authorService
            , EntityPrintVisitor printer) {
        return new AuthorShellController(authorService, printer);
    }
}
