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

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplReadTest {
    @Autowired
    private GenreDao genreDao;
    @Autowired
    private GenreService genreService;
    private Genre insertGenre1;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        genreList = List.of(insertGenre1, insertGenre2);
    }


    @Test
    void genre__readById__correctReturnObject() {
        Integer inputId = genreList.size();

        Mockito.when(genreDao.getById(inputId)).thenReturn(genreList.get(inputId - 1));

        Genre expected = genreList.get(inputId - 1);
        Genre actual = genreService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readById_NonexistentId__returnNull() {
        Integer inputId = genreList.size() + 1;
        Assertions.assertNull(genreService.readById(inputId));
    }

    @Test
    void genre__readById_Exception__returnNull() {
        Integer inputId = genreList.size();
        Mockito.when(genreDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(genreService.readById(inputId));
    }


    @Test
    void genre__readByName__correctReturnObject() {
        String inputName = insertGenre1.getName();

        Mockito.when(genreDao.getByName(inputName)).thenReturn(genreList.get(0));

        Genre expected = genreList.get(0);
        Genre actual = genreService.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readByName_NonexistentName__returnNull() {
        String inputName = "smth";
        Assertions.assertNull(genreService.readByName(inputName));
    }

    @Test
    void genre__readByName_Exception__returnNull() {
        String inputName = insertGenre1.getName();
        Mockito.when(genreDao.getByName(inputName)).thenReturn(null);
        Assertions.assertNull(genreService.readByName(inputName));
    }


    @Test
    void genre__readAll__correctReturnLstObject() {
        Mockito.doReturn(genreList).when(genreDao).getAll();
        List<Genre> expected = genreList;
        List<Genre> actual = genreService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readAll_Exception__EmptyList() {
        Mockito.when(genreDao.getAll()).thenReturn(Collections.emptyList());
        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = genreService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}