package ru.baranova.spring.service.data;

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
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.config.GenreServiceImplTestConfig;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplTest {
    @Autowired
    private GenreDao genreDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private ParseService parseServiceImpl;
    @Autowired
    private GenreService genreServiceImpl;
    private int minInput;
    private int maxInputName;
    private int maxInputDescription;
    private Genre insertGenre1;
    private Genre insertGenre2;
    private Genre testGenre;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);
        minInput = 3;
        maxInputName = 20;
        maxInputDescription = 200;
    }

    @Test
    void genre__create__correctReturnNewObject() {
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer newId = genreList.size() + 1;

        Mockito.when(parseServiceImpl.parseDashToNull(inputDescription)).thenReturn(inputDescription);

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);

        Mockito.when(genreDaoJdbc.create(Mockito.any())).thenReturn(newId);

        testGenre.setId(newId);
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.create(inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__create_ExistName__returnNull() {
        String inputName = insertGenre1.getName();
        String inputDescription = testGenre.getDescription();

        Mockito.when(parseServiceImpl.parseDashToNull(inputDescription)).thenReturn(inputDescription);

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        Assertions.assertNull(genreServiceImpl.create(inputName, inputDescription));
    }

    @Test
    void genre__create_IncorrectName__returnNull() {
        String inputName = "smth";
        String inputDescription = testGenre.getDescription();

        Mockito.when(parseServiceImpl.parseDashToNull(inputDescription)).thenReturn(inputDescription);

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);

        Assertions.assertNull(genreServiceImpl.create(inputName, inputDescription));
    }

    @Test
    void genre__create_IncorrectDescription__correctReturnNewObjectWithNullDescription() {
        String inputName = testGenre.getName();
        String inputDescription = "smth";
        Integer newId = genreList.size() + 1;

        Mockito.when(parseServiceImpl.parseDashToNull(inputDescription)).thenReturn(inputDescription);

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.FALSE);

        Mockito.when(genreDaoJdbc.create(Mockito.any())).thenReturn(newId);

        testGenre.setId(newId);
        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.create(inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__create_NullDescription__correctReturnNewObjectWithNullDescription() {
        String inputName = testGenre.getName();
        String inputDescription = "-";
        Integer newId = genreList.size() + 1;

        Mockito.when(parseServiceImpl.parseDashToNull(inputDescription)).thenReturn(null);

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.FALSE);

        Mockito.when(genreDaoJdbc.create(Mockito.any())).thenReturn(newId);

        testGenre.setId(newId);
        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.create(inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
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
        String inputName = testGenre.getName();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(genreServiceImpl.readByName(inputName));
    }

    @Test
    void genre__readAll__correctReturnLstObject() {
        Mockito.doReturn(genreList).when(genreDaoJdbc).getAll();

        List<Genre> expected = genreList;
        List<Genre> actual = genreServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update__correctReturnObject() {
        int countAffectedRows = 1;
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);

        Mockito.when(genreDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testGenre.setId(inputId);
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_IncorrectName__correctReturnObject() {
        int countAffectedRows = 1;
        String inputName = "smth";
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);

        Mockito.when(genreDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testGenre.setId(inputId);
        testGenre.setName(insertGenre1.getName());
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_IncorrectDescription__correctReturnObject() {
        int countAffectedRows = 1;
        String inputName = testGenre.getName();
        String inputDescription = "smth";
        Integer inputId = insertGenre1.getId();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.FALSE);

        Mockito.when(genreDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testGenre.setId(inputId);
        testGenre.setDescription(insertGenre1.getDescription());
        Genre expected = testGenre;
        Genre actual = genreServiceImpl.update(inputId, inputName, inputDescription);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__returnNull() {
        int countAffectedRows = 0;
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = genreList.size() + 1;

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Mockito.when(genreDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        Assertions.assertNull(genreServiceImpl.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__update_ExistNonexistentId__returnNull() {
        int countAffectedRows = 0;
        String inputName = testGenre.getName();
        String inputDescription = testGenre.getDescription();
        Integer inputId = insertGenre1.getId();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.getById(inputId)).thenReturn(insertGenre1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputDescription, minInput, maxInputDescription))
                .thenReturn(Boolean.TRUE);

        Mockito.when(genreDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        Assertions.assertNull(genreServiceImpl.update(inputId, inputName, inputDescription));
    }

    @Test
    void genre__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = genreList.size();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertTrue(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_NonexistentId__false() {
        Integer inputId = genreList.size() + 1;

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }

    @Test
    void genre__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = genreList.size();

        Mockito.doReturn(genreList).when(genreServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(genreDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertFalse(genreServiceImpl.delete(inputId));
    }
}