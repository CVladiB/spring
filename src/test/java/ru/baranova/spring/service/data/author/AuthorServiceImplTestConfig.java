package ru.baranova.spring.service.data.author;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.repository.entity.AuthorRepository;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

@TestConfiguration
@ConfigurationProperties(prefix = "app.service.author-service")
public class AuthorServiceImplTestConfig {
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private CheckService checkService;
    @MockBean
    private ParseService parseService;

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository
            , CheckService checkService
            , ParseService parseService) {
        return new AuthorServiceImpl(authorRepository, checkService, parseService);
    }
}
