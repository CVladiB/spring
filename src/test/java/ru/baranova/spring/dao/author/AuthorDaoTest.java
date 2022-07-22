package ru.baranova.spring.dao.author;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ContextConfiguration(classes = {AuthorDaoTestConfig.class, StopSearchConfig.class})
class AuthorDaoTest {
    @Autowired
    private AuthorDao authorDaoJdbc;

    @Test
    void author__create__correctReturnNewAuthor() {
        Author expected = new Author(2, "SurnameTest", "NameTest");
        // todo syntax error or can't return id
        Integer id = authorDaoJdbc.create(expected);
        Author actual = authorDaoJdbc.getById(2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getSurname(),actual.getSurname()),
                () -> Assertions.assertEquals(expected.getName(),actual.getName()),
                () -> Assertions.assertEquals(expected.getId(),actual.getId())
        );
    }

    @Test
    void author__create_NullSurname__incorrectNPE() {
        Author expected = new Author(2, null, "NameTest");
        Assertions.assertThrows(NullPointerException.class, () -> authorDaoJdbc.create(expected));
    }

    @Test
    void author__create_NullName__incorrectNPE() {
        Author expected = new Author(2, "SurnameTest", null);
        Assertions.assertThrows(NullPointerException.class, () -> authorDaoJdbc.create(expected));
    }

    @Test
    void author__getById__correctReturnAuthorById() {
        Author expected = new Author(1, "Surname", "Name");
        Author actual = authorDaoJdbc.getById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getSurname(),actual.getSurname()),
                () -> Assertions.assertEquals(expected.getName(),actual.getName()),
                () -> Assertions.assertEquals(expected.getId(),actual.getId())
        );
    }

    @Test
    void author__getById__incorrectEmptyRDAException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> authorDaoJdbc.getById(100));
    }

    @Test
    void author__getBySurnameAndName__correctReturnListAuthors() {
        // todo syntax error
        List<Author> expected = List.of(new Author(1, "Surname", "Name"));
        List<Author> actual = authorDaoJdbc.getBySurnameAndName("Surname", "Name");
        Assertions.assertArrayEquals(expected.toArray(),actual.toArray());
    }
    @Disabled
    @Test
    void author__getBySurnameAndName_NullSurname__incorrectNPE() {
        // todo
        List<Author> expected = List.of(new Author(1, "Surname", "Name"));
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(null, "Name");
        Assertions.assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Disabled
    @Test
    void author__getBySurnameAndName_NullName__incorrectNPE() {
        // todo
        List<Author> expected = List.of(new Author(1, "Surname", "Name"));
        List<Author> actual = authorDaoJdbc.getBySurnameAndName("Surname", null);
        Assertions.assertArrayEquals(expected.toArray(),actual.toArray());
    }

    @Test
    void author__getAll__returnListAuthors() {
        List<Author> expected = List.of(new Author(1, "Surname", "Name"));
        List<Author> actual = authorDaoJdbc.getAll();
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void author__update__correctChangeAllFieldAuthorById() {
        Author expected = new Author(1, "SurnameTest", "NameTest");
        authorDaoJdbc.update(expected);
        Author actual = authorDaoJdbc.getById(1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NullSurname__incorrectNPE() {
        Author expected = new Author(1, null, "NameTest");
        Assertions.assertThrows(NullPointerException.class, () -> authorDaoJdbc.update(expected));
    }

    @Test
    void author__update_NullName__incorrectNPE() {
        Author expected = new Author(1, "SurnameTest", null);
        Assertions.assertThrows(NullPointerException.class, () -> authorDaoJdbc.update(expected));
    }


    @Test
    void author__update_UnexpectedId__incorrectEmptyRDAException() {
        // todo not exception, why?
        Author expected = new Author(100, "SurnameTest", "NameTest");
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> authorDaoJdbc.update(expected));
    }

    @Test
    void author__delete__correctDelete() {
        List<Author> expected = new ArrayList<>();
        authorDaoJdbc.delete(1);
        List<Author> actual = authorDaoJdbc.getAll();
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void author__delete__incorrectDelete() {
        // todo not exception, why?
        Assertions.assertThrows(NullPointerException.class, () -> authorDaoJdbc.delete(100));

    }
}