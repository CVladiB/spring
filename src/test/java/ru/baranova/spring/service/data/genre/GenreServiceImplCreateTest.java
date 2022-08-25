package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplCreateTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreDao genreDao;
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
    void genre__create__correctReturnNewObject() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer newId = genreList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.eq(null))).thenReturn(inputDescription);
        testGenre.setId(newId);
        Mockito.when(genreDao.create(inputName, inputDescription)).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreService.create(inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__create_Exception__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer newId = genreList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.eq(null))).thenReturn(inputDescription);
        testGenre.setId(newId);
        Mockito.when(genreDao.create(inputName, inputDescription)).thenReturn(null);

        Assertions.assertNull(genreService.create(inputName, inputDescription));
    }

    @Test
    void genre__create_ExistName__returnNull() {
        String inputName = insertGenre1.getName();
        String inputDescription = testGenre.getDescription();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(genreService.create(inputName, inputDescription));
    }

    @Test
    void genre__create_IncorrectName__returnNull() {
        String inputName = "smth";
        String inputDescription = testGenre.getDescription();

        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(genreService.create(inputName, inputDescription));
    }

    @Test
    void genre__create_IncorrectDescription__correctReturnNewObjectWithNullDescription() {
        String inputName = testGenre.getName();
        String inputDescription = "smth";
        Integer newId = genreList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputDescription), Mockito.any(), Mockito.eq(null))).thenReturn(null);
        testGenre.setId(newId);
        testGenre.setDescription(null);
        Mockito.when(genreDao.create(inputName, null)).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreService.create(inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }
}
