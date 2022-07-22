package ru.baranova.spring.dao.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
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

    @Test
    void genre__create__correctReturnNewGenre() {
        Genre expected = new Genre(3, "NameTest", "DescriptionTest");
        // todo syntax error or can't return id
        Integer id = genreDaoJdbc.create(expected);
        Genre actual = genreDaoJdbc.getById(3);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void genre__create_NullName__incorrectNPE() {
        Genre expected = new Genre(3, null, "DescriptionTest");
        Assertions.assertThrows(NullPointerException.class, () -> genreDaoJdbc.create(expected));
    }

    @Test
    void genre__create_NullDescription__incorrectNPE() {
        Genre expected = new Genre(3, "NameTest", null);
        Assertions.assertThrows(NullPointerException.class, () -> genreDaoJdbc.create(expected));
    }

    @Test
    void genre__getById__correctReturnGenreById() {
        Genre expected = new Genre(1, "Name1", "Description1");
        Genre actual = genreDaoJdbc.getById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getName(),actual.getName()),
                () -> Assertions.assertEquals(expected.getDescription(),actual.getDescription()),
                () -> Assertions.assertEquals(expected.getId(),actual.getId())
        );
    }

    @Test
    void genre__getById__incorrectEmptyRDAException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDaoJdbc.getById(100));
    }

    @Test
    void genre__getByName__correctReturnGenreByName() {
        // todo syntax error
        Genre expected = new Genre(1, "Name1", "Description1");
        Genre actual = genreDaoJdbc.getByName("Name1");
        Assertions.assertEquals(expected,actual);
    }
    @Disabled
    @Test
    void genre__getByName_NullName__incorrectNPE() {
        // todo
        Genre expected = new Genre(1, "Name1", "Description1");
        Genre actual = genreDaoJdbc.getByName("Name25");
        Assertions.assertEquals(expected,actual);
    }


    @Test
    void genre__getAll__returnListGenres() {
        List<Genre> expected = List.of(new Genre(1, "Name1", "Description1"), new Genre(2, "Name2", "Description2"));
        List<Genre> actual = genreDaoJdbc.getAll();
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void genre__update__correctChangeAllАFieldGenreById() {
        Genre expected = new Genre(1, "NameTest", "DescriptionTest");
        genreDaoJdbc.update(expected);
        Genre actual = genreDaoJdbc.getById(1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NullName__incorrectNPE() {
        Genre expected = new Genre(1, null, "NameTest");
        Assertions.assertThrows(NullPointerException.class, () -> genreDaoJdbc.update(expected));
    }

    @Test
    void genre__update_NullDescription__correctChangeAllFieldGenreById() {
        // todo why NPE?
        Genre expected = new Genre(1, "NameTest", null);
        genreDaoJdbc.update(expected);
        Genre actual = genreDaoJdbc.getById(1);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void genre__update_UnexpectedId__incorrectEmptyRDAException() {
        // todo not exception, why?
        Genre expected = new Genre(100, "SurnameTest", "NameTest");
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDaoJdbc.update(expected));
    }

    @Test
    void genre__delete__correctDelete() {
        List<Genre> expected = new ArrayList<>();
        genreDaoJdbc.delete(1);
        genreDaoJdbc.delete(2);
        List<Genre> actual = genreDaoJdbc.getAll();
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void genre__delete__incorrectDelete() {
        // todo not exception, why?
        Assertions.assertThrows(Exception.class,
                () -> genreDaoJdbc.delete(100));

    }
}