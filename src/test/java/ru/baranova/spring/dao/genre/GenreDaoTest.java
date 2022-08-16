package ru.baranova.spring.dao.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {GenreDaoTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class GenreDaoTest {
    @Autowired
    private GenreDao genreDao;
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
        List<Integer> listExistId = genreDao.getAll()
                .stream()
                .map(Genre::getId)
                .toList();

        Genre expected = testGenre;
        Genre actual = genreDao.create(testGenre.getName(), testGenre.getDescription());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__create_NullName__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDao.create(null, testGenre.getDescription()));
    }

    @Test
    void genre__create_DuplicateName__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDao.create(insertGenre1.getName(), insertGenre1.getDescription()));
    }

    @Test
    void genre__create_NullDescription_correctReturnNewGenre() {
        List<Integer> listExistId = genreDao.getAll().stream().map(Genre::getId).toList();

        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreDao.create(testGenre.getName(), null);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__getById__correctReturnGenreById() {
        Integer id = insertGenre1.getId();
        Genre expected = insertGenre1;
        Genre actual = genreDao.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getName(), actual.getName()),
                () -> Assertions.assertEquals(expected.getDescription(), actual.getDescription()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void genre__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDao.getById(nonexistentId));
    }

    @Test
    void genre__getByName__correctReturnGenreByName() {
        Genre expected = insertGenre1;
        Genre actual = genreDao.getByName(insertGenre1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getByName_NonexistentName__incorrectException() {
        String nonexistentName = "Name25";
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDao.getByName(nonexistentName));
    }

    @Test
    void genre__getByName_NullName__incorrectException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDao.getByName(null));
    }

    @Test
    void genre__getAll__returnListGenres() {
        List<Genre> expected = genreList;
        List<Genre> actual = genreDao.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getAll_Empty_emptyListResultException() {
        genreList.stream().map(Genre::getId).forEach(genreDao::delete);
        Assertions.assertThrows(DataAccessException.class, () -> genreDao.getAll());
    }

    @Test
    void genre__update__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        Genre expected = testGenre;
        Genre actual = genreDao.update(testGenre.getId(), testGenre.getName(), testGenre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__incorrectException() {
        testGenre.setId(100);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDao.update(testGenre.getId(), testGenre.getName(), testGenre.getDescription()));
    }

    @Test
    void genre__update_NullName__incorrectException() {
        testGenre.setId(insertGenre1.getId());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreDao.update(testGenre.getId(), null, testGenre.getDescription()));
    }

    @Test
    void genre__update_NullDescription__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreDao.update(testGenre.getId(), testGenre.getName(), null);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void genre__delete__correctDelete() {
        List<Genre> actualBeforeDelete = genreDao.getAll();
        Integer inputId = insertGenre1.getId();
        Integer inputId2 = insertGenre2.getId();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertTrue(genreDao.delete(inputId));
        Assertions.assertTrue(genreDao.delete(inputId2));
    }

    @Test
    void genre__delete_NonexistentId__notDelete() {
        List<Genre> actualBeforeDelete = genreDao.getAll();
        Integer inputId = actualBeforeDelete.size() + 1;
        Integer inputId2 = actualBeforeDelete.size() + 2;

        List<Genre> expected = actualBeforeDelete;
        List<Genre> actual = genreDao.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> genreDao.delete(inputId));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> genreDao.delete(inputId2));
        Assertions.assertEquals(expected, actual);
    }
}