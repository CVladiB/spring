package ru.baranova.spring.dao.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {AuthorDaoTestConfig.class, StopSearchConfig.class})
class AuthorDaoTest {
    @Autowired
    private AuthorDao authorDaoJdbc;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private Author testAuthor;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);
    }

    @Test
    void author__create__correctReturnNewAuthor() {
        List<Integer> listExistId = authorDaoJdbc.getAll().stream()
                .map(Author::getId)
                .toList();

        Author expected = testAuthor;
        Author actual = authorDaoJdbc.create(testAuthor.getSurname(), testAuthor.getName());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void author__create_NullSurname__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDaoJdbc.create(null, testAuthor.getName()));
    }

    @Test
    void author__create_NullName__incorrectException() {
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDaoJdbc.create(testAuthor.getSurname(), null));
    }

    @Test
    void author__getById__correctReturnAuthorById() {
        Integer id = insertAuthor1.getId();
        Author expected = insertAuthor1;
        Author actual = authorDaoJdbc.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getSurname(), actual.getSurname()),
                () -> Assertions.assertEquals(expected.getName(), actual.getName()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void author__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> authorDaoJdbc.getById(nonexistentId));
    }

    @Test
    void author__getBySurnameAndName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(insertAuthor1.getSurname(), insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullSurname__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(null, insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(insertAuthor1.getSurname(), null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NonexistentSurname__correctReturnListAuthors() {
        String nonexistentSurname = "Smth";
        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(nonexistentSurname, insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NonexistentName__correctReturnListAuthors() {
        String nonexistentName = "Smth";
        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(insertAuthor1.getSurname(), nonexistentName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullSurnameAndName__correctReturnEmptyListAuthors() {
        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(null, null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_DifferentSurnameAndName__correctReturnEmptyListAuthors() {
        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorDaoJdbc.getBySurnameAndName(insertAuthor1.getSurname(), insertAuthor2.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getAll__returnListAuthors() {
        List<Author> expected = authorList;
        List<Author> actual = authorDaoJdbc.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update__correctChangeAllFieldAuthorById() {
        Integer id = insertAuthor1.getId();
        testAuthor.setId(id);
        Author expected = testAuthor;
        Author actual = authorDaoJdbc.update(testAuthor.getId(), testAuthor.getSurname(), testAuthor.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__incorrectException() {
        testAuthor.setId(100);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDaoJdbc.update(testAuthor.getId(), testAuthor.getSurname(), testAuthor.getName()));
    }

    @Test
    void author__update_NullSurname__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDaoJdbc.update(testAuthor.getId(), null, testAuthor.getName()));
    }

    @Test
    void author__update_NullName__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDaoJdbc.update(testAuthor.getId(), testAuthor.getSurname(), null));
    }

    @Test
    void author__delete__correctDelete() {
        int countAffectedRowsExpected = 2;
        int countAffectedRowsActual;

        List<Author> actualBeforeDelete = authorDaoJdbc.getAll();
        countAffectedRowsActual = authorDaoJdbc.delete(insertAuthor1.getId());
        countAffectedRowsActual += authorDaoJdbc.delete(insertAuthor2.getId());

        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }

    @Test
    void author__delete_NonexistentId__notDelete() {
        int countAffectedRowsExpected = 0;
        int countAffectedRowsActual;

        List<Author> actualBeforeDelete = authorDaoJdbc.getAll();
        countAffectedRowsActual = authorDaoJdbc.delete(actualBeforeDelete.size() + 1);
        countAffectedRowsActual += authorDaoJdbc.delete(actualBeforeDelete.size() + 2);

        List<Author> expected = actualBeforeDelete;
        List<Author> actual = authorDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }
}