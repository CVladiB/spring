package ru.baranova.spring.service.data.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
public class GenreServiceImplTestConfig {
    @MockBean
    private GenreDao genreDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @SpyBean
    private GenreService genreServiceImpl;

    @Bean
    public GenreService genreServiceImpl(GenreDao genreDaoJdbc
            , CheckService checkServiceImpl) {
        return new GenreServiceImpl(genreDaoJdbc, checkServiceImpl);
    }

}
