package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplOtherMethodsTest {
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
    void checkExistId__true() {
        Integer inputId = 1;
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(genreList.get(0));
        Assertions.assertTrue(genreServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId_Exception__false() {
        Integer inputId = 1;
        Mockito.when(genreServiceImpl.readById(inputId)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertFalse(genreServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId__false() {
        Integer inputId = 100;
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(null);
        Assertions.assertFalse(genreServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistName_Exception__true() {
        String inputName = insertGenre1.getName();
        Mockito.when(genreServiceImpl.readByName(inputName)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertFalse(genreServiceImpl.checkExist(inputName));
    }

    @Test
    void checkExistNameSurname_NonExistName__false() {
        String inputName = "test";
        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Assertions.assertFalse(genreServiceImpl.checkExist(inputName));
    }

}