package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.GenreRepository;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplUpdateTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CheckService checkService;
    @Autowired
    private GenreService genreService;
    private Genre insertGenre1;
    private Genre testGenre;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);
    }

    @Test
    void genre__update__correctReturnObject() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(insertGenre1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputDescription);
        testGenre.setId(inputId);
        Mockito.when(genreRepository.save(testGenre)).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreService.update(inputId, inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_Exception__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(insertGenre1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputName);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputDescription);
        testGenre.setId(inputId);
        Mockito.when(genreRepository.save(testGenre)).thenThrow(EmptyResultDataAccessException.class);

        Assertions.assertNull(genreService.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__update_IncorrectName__correctReturnObject() {
        String inputName = "smth";
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(insertGenre1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(insertGenre1.getName());
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputDescription);
        testGenre.setId(inputId);
        testGenre.setName(insertGenre1.getName());
        Mockito.when(genreRepository.save(testGenre)).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreService.update(inputId, inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_IncorrectDescription__correctReturnObject() {
        String inputName = testGenre.getName();
        String inputDescription = "smth";
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(insertGenre1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(insertGenre1.getDescription());
        testGenre.setId(inputId);
        testGenre.setDescription(insertGenre1.getDescription());
        Mockito.when(genreRepository.save(testGenre)).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreService.update(inputId, inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = genreList.size() + 1;

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.empty());
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.FALSE);
        testGenre.setId(inputId);
        Mockito.when(genreRepository.save(testGenre)).thenReturn(testGenre);

        Assertions.assertNull(genreService.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__update_ExistNonexistentId__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(insertGenre1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertGenre1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputName);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.any())).thenReturn(inputDescription);
        testGenre.setId(inputId);
        Mockito.when(genreRepository.save(testGenre)).thenReturn(null);

        Assertions.assertNull(genreService.update(inputId, inputName, inputDescription));
    }
}
