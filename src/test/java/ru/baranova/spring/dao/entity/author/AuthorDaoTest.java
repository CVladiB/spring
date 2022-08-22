package ru.baranova.spring.dao.entity.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;

import javax.persistence.PersistenceException;
import java.util.List;

@DataJpaTest
@Import(value = {AuthorDaoTestConfig.class, StopSearchConfig.class})
class AuthorDaoTest {
    @Autowired
    private AuthorDao authorDao;
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
        List<Integer> listExistId = authorDao.getAll().stream()
                .map(Author::getId)
                .toList();

        Author expected = testAuthor;
        Author actual = authorDao.create(testAuthor.getSurname(), testAuthor.getName());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void author__create_NullSurname__incorrectException() {
        Assertions.assertThrows(PersistenceException.class,
                () -> authorDao.create(null, testAuthor.getName()));
    }

    @Test
    void author__create_NullName__incorrectException() {
        Assertions.assertThrows(PersistenceException.class,
                () -> authorDao.create(testAuthor.getSurname(), null));
    }

    @Test
    void author__getById__correctReturnAuthorById() {
        Integer id = insertAuthor1.getId();
        Author expected = insertAuthor1;
        Author actual = authorDao.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(PersistenceException.class, () -> authorDao.getById(nonexistentId));
    }

    @Test
    void author__getBySurnameAndName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDao.getBySurnameAndName(insertAuthor1.getSurname(), insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullSurname__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDao.getBySurnameAndName(null, insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorDao.getBySurnameAndName(insertAuthor1.getSurname(), null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NonexistentSurname__emptyListResultException() {
        String nonexistentSurname = "Smth";
        Assertions.assertThrows(DataIntegrityViolationException.class
                , () -> authorDao.getBySurnameAndName(nonexistentSurname, insertAuthor1.getName()));
    }

    @Test
    void author__getBySurnameAndName_NonexistentName__emptyListResultException() {
        String nonexistentName = "Smth";
        Assertions.assertThrows(DataIntegrityViolationException.class
                , () -> authorDao.getBySurnameAndName(insertAuthor1.getSurname(), nonexistentName));
    }

    @Test
    void author__getBySurnameAndName_NullSurnameAndName__emptyListResultException() {
        Assertions.assertThrows(DataIntegrityViolationException.class
                , () -> authorDao.getBySurnameAndName(null, null));
    }

    @Test
    void author__getBySurnameAndName_DifferentSurnameAndName__emptyListResultException() {
        Assertions.assertThrows(DataIntegrityViolationException.class
                , () -> authorDao.getBySurnameAndName(insertAuthor1.getSurname(), insertAuthor2.getName()));
    }

    @Test
    void author__getAll__returnListAuthors() {
        List<Author> expected = authorList;
        List<Author> actual = authorDao.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update__correctChangeAllFieldAuthorById() {
        Integer id = insertAuthor1.getId();
        testAuthor.setId(id);
        Author expected = testAuthor;
        Author actual = authorDao.update(testAuthor.getId(), testAuthor.getSurname(), testAuthor.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__incorrectException() {
        testAuthor.setId(100);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorDao.update(testAuthor.getId(), testAuthor.getSurname(), testAuthor.getName()));
    }

    @Test
    void author__update_NullSurname__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        Assertions.assertThrows(PersistenceException.class,
                () -> authorDao.update(testAuthor.getId(), null, testAuthor.getName()));
    }

    @Test
    void author__update_NullName__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        Assertions.assertThrows(PersistenceException.class,
                () -> authorDao.update(testAuthor.getId(), testAuthor.getSurname(), null));
    }

    @Test
    void author__delete__correctDelete() {
        List<Author> actualBeforeDelete = authorDao.getAll();
        Integer inputId = actualBeforeDelete.get(0).getId();
        Integer inputId2 = actualBeforeDelete.get(1).getId();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertTrue(authorDao.delete(inputId));
        Assertions.assertTrue(authorDao.delete(inputId2));

    }

    @Test
    void author__delete_NonexistentId__notDelete() {
        List<Author> actualBeforeDelete = authorDao.getAll();
        Integer inputId = actualBeforeDelete.size() + 1;
        Integer inputId2 = actualBeforeDelete.size() + 2;

        List<Author> expected = actualBeforeDelete;
        List<Author> actual = authorDao.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> authorDao.delete(inputId));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> authorDao.delete(inputId2));
        Assertions.assertEquals(expected, actual);
    }
}
