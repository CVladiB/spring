package ru.baranova.spring.service.data.genre;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.repository.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;


@TestConfiguration
public class GenreServiceImplTestConfig {
    @MockBean
    private GenreDao genreDao;
    @MockBean
    private CheckService checkService;

    @Bean
    public GenreService genreService(GenreDao genreDao
            , CheckService checkService) {
        return new GenreServiceImpl(genreDao, checkService);
    }

}
