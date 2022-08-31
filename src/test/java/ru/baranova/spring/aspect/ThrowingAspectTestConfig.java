package ru.baranova.spring.aspect;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.author.AuthorRestController;
import ru.baranova.spring.repository.entity.AuthorRepository;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.author.AuthorServiceImpl;

import javax.persistence.EntityManager;

@TestConfiguration
public class ThrowingAspectTestConfig {
    @MockBean
    private CheckService checkService;
    @MockBean
    private ParseService parseService;

    @Bean
    public ThrowingAspect throwingAspect() {
        return new ThrowingAspect();
    }

    @Bean
    public AuthorRepository authorDao(EntityManager em) {
        return null;
    }

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository
            , CheckService checkService
            , ParseService parseService) {
        return new AuthorServiceImpl(authorRepository, checkService, parseService);
    }

    @Bean
    public AuthorRestController authorShellController(AuthorService authorService) {
        return new AuthorRestController(authorService);
    }
}
