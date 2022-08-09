package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplDeleteTest {
    @Autowired
    private GenreDao genreDaoJdbc;
    @Autowired
    private GenreService genreServiceImpl;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        genreList = List.of(insertGenre1, insertGenre2);
    }

    @Test
    void genre__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = genreList.size();
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(genreList.get(1));
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);
        Assertions.assertTrue(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_Exception__false() {
        Integer inputId = genreList.size();
        Mockito.doThrow(EmptyResultDataAccessException.class).when(genreDaoJdbc).delete(inputId);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_NonexistentId__false() {
        Integer inputId = genreList.size() + 1;
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(null);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = genreList.size();
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(genreList.get(0));
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);
        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }
}