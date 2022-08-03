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
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplReadTest {
    @Autowired
    private GenreDao genreDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private GenreService genreServiceImpl;
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

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenReturn(genreList.get(inputId - 1));

        Genre expected = genreList.get(inputId - 1);
        Genre actual = genreServiceImpl.readById(inputId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readById_NonexistentId__returnNull() {
        Integer inputId = genreList.size() + 1;

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(genreServiceImpl.readById(inputId));
    }

    @Test
    void genre__readById_Exception__returnNull() {
        Integer inputId = genreList.size();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenThrow(EmptyResultDataAccessException.class);

        Assertions.assertNull(genreServiceImpl.readById(inputId));
    }


    @Test
    void genre__readByName__correctReturnObject() {
        String inputName = insertGenre1.getName();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getByName(inputName)).thenReturn(genreList.get(0));

        Genre expected = genreList.get(0);
        Genre actual = genreServiceImpl.readByName(inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readByName_NonexistentName__returnNull() {
        String inputName = "smth";

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(genreServiceImpl.readByName(inputName));
    }

    @Test
    void genre__readByName_Exception__returnNull() {
        String inputName = insertGenre1.getName();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getByName(inputName)).thenThrow(EmptyResultDataAccessException.class);

        Assertions.assertNull(genreServiceImpl.readByName(inputName));
    }


    @Test
    void genre__readAll__correctReturnLstObject() {
        Mockito.doReturn(genreList).when(genreDaoJdbc).getAll();

        List<Genre> expected = genreList;
        List<Genre> actual = genreServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }
}