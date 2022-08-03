package ru.baranova.spring.dao.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {GenreDaoTestConfig.class, StopSearchConfig.class})
class GenreDaoTest {
    @Autowired
    private GenreDao genreDaoJdbc;
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
    }

    @Test
    void genre__create__correctReturnNewGenre() {
        List<Integer> listExistId = genreDaoJdbc.getAll()
                .stream()
                .map(Genre::getId)
                .toList();

        Genre expected = testGenre;
        Genre actual = genreDaoJdbc.create(testGenre.getName(), testGenre.getDescription());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__create_NullName__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDaoJdbc.create(null, testGenre.getDescription()));
    }

    @Test
    void genre__create_DuplicateName__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDaoJdbc.create(insertGenre1.getName(), insertGenre1.getDescription()));
    }

    @Test
    void genre__create_NullDescription_correctReturnNewGenre() {
        List<Integer> listExistId = genreDaoJdbc.getAll().stream().map(Genre::getId).toList();

        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreDaoJdbc.create(testGenre.getName(), null);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__getById__correctReturnGenreById() {
        Integer id = insertGenre1.getId();
        Genre expected = insertGenre1;
        Genre actual = genreDaoJdbc.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getName(), actual.getName()),
                () -> Assertions.assertEquals(expected.getDescription(), actual.getDescription()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void genre__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDaoJdbc.getById(nonexistentId));
    }

    @Test
    void genre__getByName__correctReturnGenreByName() {
        Genre expected = insertGenre1;
        Genre actual = genreDaoJdbc.getByName(insertGenre1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getByName_NonexistentName__incorrectException() {
        String nonexistentName = "Name25";
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDaoJdbc.getByName(nonexistentName));
    }

    @Test
    void genre__getByName_NullName__incorrectException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDaoJdbc.getByName(null));
    }

    @Test
    void genre__getAll__returnListGenres() {
        List<Genre> expected = genreList;
        List<Genre> actual = genreDaoJdbc.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getAll_Empty_incorrectException() {
        genreList.stream().map(Genre::getId).forEach(genreDaoJdbc::delete);
        List<Genre> expected = new ArrayList<>();
        List<Genre> actual = genreDaoJdbc.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        Genre expected = testGenre;
        Genre actual = genreDaoJdbc.update(testGenre.getId(), testGenre.getName(), testGenre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__incorrectException() {
        testGenre.setId(100);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDaoJdbc.update(testGenre.getId(), testGenre.getName(), testGenre.getDescription()));
    }

    @Test
    void genre__update_NullName__incorrectException() {
        testGenre.setId(insertGenre1.getId());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDaoJdbc.update(testGenre.getId(), null, testGenre.getDescription()));
    }

    @Test
    void genre__update_NullDescription__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreDaoJdbc.update(testGenre.getId(), testGenre.getName(), null);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void genre__delete__correctDelete() {
        int countAffectedRowsExpected = 2;
        int countAffectedRowsActual;

        Integer id1 = insertGenre1.getId();
        Integer id2 = insertGenre2.getId();

        List<Genre> actualBeforeDelete = genreDaoJdbc.getAll();
        countAffectedRowsActual = genreDaoJdbc.delete(id1);
        countAffectedRowsActual += genreDaoJdbc.delete(id2);

        List<Genre> expected = new ArrayList<>();
        List<Genre> actual = genreDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }

    @Test
    void genre__delete_NonexistentId__notDelete() {
        int countAffectedRowsExpected = 0;
        int countAffectedRowsActual;


        List<Genre> actualBeforeDelete = genreDaoJdbc.getAll();
        Integer id1 = actualBeforeDelete.size() + 1;
        Integer id2 = actualBeforeDelete.size() + 2;
        countAffectedRowsActual = genreDaoJdbc.delete(id1);
        countAffectedRowsActual += genreDaoJdbc.delete(id2);

        List<Genre> expected = actualBeforeDelete;
        List<Genre> actual = genreDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }
}