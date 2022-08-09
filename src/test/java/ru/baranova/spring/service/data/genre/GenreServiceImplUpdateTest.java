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
class GenreServiceImplUpdateTest {
    @Autowired
    private GenreDao genreDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private GenreService genreServiceImpl;
    private int minInput;
    private int maxInputName;
    private int maxInputDescription;
    private Genre insertGenre1;
    private Genre testGenre;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);
        minInput = 3;
        maxInputName = 20;
        maxInputDescription = 200;
    }

    @Test
    void genre__update__correctReturnObject() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);
        testGenre.setId(inputId);
        Mockito.when(genreDaoJdbc.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_Exception__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);
        testGenre.setId(inputId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(genreDaoJdbc).update(Mockito.any(), Mockito.any(), Mockito.any());

        Assertions.assertNull(genreServiceImpl.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__update_IncorrectName__correctReturnObject() {
        String inputName = "smth";
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);
        testGenre.setId(inputId);
        testGenre.setName(insertGenre1.getName());
        Mockito.when(genreDaoJdbc.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_IncorrectDescription__correctReturnObject() {
        String inputName = testGenre.getName();
        String inputDescription = "smth";
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.FALSE);
        testGenre.setId(inputId);
        testGenre.setDescription(insertGenre1.getDescription());
        Mockito.when(genreDaoJdbc.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(testGenre);

        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = genreList.size() + 1;

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(null);
        Mockito.when(genreDaoJdbc.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);

        Assertions.assertNull(genreServiceImpl.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__update_ExistNonexistentId__returnNull() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.when(genreServiceImpl.readByName(inputName)).thenReturn(null);
        Mockito.when(genreServiceImpl.readById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);

        Assertions.assertNull(genreServiceImpl.update(inputId, inputName, inputDescription));
    }
}