package ru.baranova.spring.service.data.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
public class GenreServiceImplTestConfig {
    @MockBean
    private GenreDao genreDaoJdbc;
    @MockBean
    private CheckService checkServiceImpl;
    @MockBean
    private AppService appServiceImpl;

    @Bean
    public GenreService genreServiceImpl(GenreDao genreDaoJdbc
            , CheckService checkServiceImpl, AppService appServiceImpl) {
        return new GenreServiceImpl(genreDaoJdbc, checkServiceImpl, appServiceImpl);
    }

}
