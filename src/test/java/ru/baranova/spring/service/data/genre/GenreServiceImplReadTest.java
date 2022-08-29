package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplReadTest {
    @Autowired
    private GenreRepository genreRepository;
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

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(genreList.get(inputId - 1)));

        Genre expected = genreList.get(inputId - 1);
        Genre actual = genreService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readById_NonexistentId__returnNull() {
        Integer inputId = genreList.size() + 1;
        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertNull(genreService.readById(inputId));
    }

    @Test
    void genre__readById_Exception__returnNull() {
        Integer inputId = genreList.size();
        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertNull(genreService.readById(inputId));
    }


    @Test
    void genre__readByName__correctReturnObject() {
        String inputName = insertGenre1.getName();

        Mockito.when(genreRepository.findByName(inputName)).thenReturn(List.of(genreList.get(0)));

        List<Genre> expected = List.of(genreList.get(0));
        List<Genre> actual = genreService.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readByName_NonexistentName__returnNull() {
        String inputName = "smth";
        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = genreService.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readByName_Exception__returnNull() {
        String inputName = insertGenre1.getName();
        Mockito.when(genreRepository.findByName(inputName)).thenReturn(Collections.emptyList());
        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = genreService.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void genre__readAll__correctReturnLstObject() {
        Mockito.doReturn(genreList).when(genreRepository).findAll();
        List<Genre> expected = genreList;
        List<Genre> actual = genreService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__readAll_Exception__EmptyList() {
        Mockito.when(genreRepository.findAll()).thenReturn(Collections.emptyList());
        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = genreService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}
