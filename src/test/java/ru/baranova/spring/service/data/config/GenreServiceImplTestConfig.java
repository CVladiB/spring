package ru.baranova.spring.service.data.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.data.genre.GenreServiceImpl;

@TestConfiguration
public class GenreServiceImplTestConfig {
    @MockBean
    private GenreDao genreDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private ParseService parseServiceImpl;
    @SpyBean
    private GenreService genreServiceImpl;

    @Bean
    public GenreService genreServiceImpl(GenreDao genreDaoJdbc
            , CheckService checkServiceImpl
            , ParseService parseServiceImpl) {
        return new GenreServiceImpl(genreDaoJdbc, checkServiceImpl, parseServiceImpl);
    }

}
