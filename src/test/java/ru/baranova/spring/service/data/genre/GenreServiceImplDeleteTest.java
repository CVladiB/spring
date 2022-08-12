package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplDeleteTest {
    @Autowired
    private GenreDao genreDaoJdbc;
    @Autowired
    private GenreService genreServiceImpl;
    @Autowired
    private CheckService checkServiceImpl;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        genreList = List.of(insertGenre1, insertGenre2);
    }

    @Test
    void genre__delete__true() {
        Integer inputId = genreList.size();
        Mockito.when(checkServiceImpl.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(true);
        Assertions.assertTrue(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_Exception__false() {
        Integer inputId = genreList.size();
        Mockito.when(checkServiceImpl.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(false);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_NonexistentId__false() {
        Integer inputId = genreList.size() + 1;
        Mockito.when(checkServiceImpl.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.FALSE);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_ExistNonexistentId__false() {
        Integer inputId = genreList.size();
        Mockito.when(checkServiceImpl.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(false);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }
}