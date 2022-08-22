package ru.baranova.spring.aspect;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.dao.entity.author.AuthorDao;
import ru.baranova.spring.dao.entity.author.AuthorDaoJpa;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.author.AuthorServiceImpl;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import javax.persistence.EntityManager;

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
    public AuthorDao authorDao(EntityManager em) {
        return new AuthorDaoJpa(em);
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
