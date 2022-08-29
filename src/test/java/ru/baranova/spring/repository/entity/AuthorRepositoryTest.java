package ru.baranova.spring.repository.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.model.Author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
//@Import(value = {AuthorRepository.class, StopSearchConfig.class})
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
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
        List<Integer> listExistId = authorRepository.findAll().stream()
                .map(Author::getId)
                .toList();

        Author expected = testAuthor;
        Author actual = authorRepository.save(testAuthor);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void author__create_NullSurname__incorrectException() {
        testAuthor.setSurname(null);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorRepository.save(testAuthor));
    }

    @Test
    void author__create_NullName__incorrectException() {
        testAuthor.setName(null);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> authorRepository.save(testAuthor));
    }

    @Test
    void author__findById__correctReturnAuthorById() {
        Integer id = insertAuthor1.getId();
        Author expected = insertAuthor1;
        Author actual = authorRepository.findById(id).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__findById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertEquals(Optional.empty(), authorRepository.findById(nonexistentId));

    }

    @Test
    void author__getBySurnameAndName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorRepository.findBySurnameAndName(insertAuthor1.getSurname(), insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullSurname__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorRepository.findBySurnameAndName(null, insertAuthor1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NullName__correctReturnListAuthors() {
        List<Author> expected = List.of(insertAuthor1);
        List<Author> actual = authorRepository.findBySurnameAndName(insertAuthor1.getSurname(), null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__getBySurnameAndName_NonexistentSurname__emptyListResultException() {
        String nonexistentSurname = "Smth";
        Assertions.assertEquals(Collections.emptyList()
                , authorRepository.findBySurnameAndName(nonexistentSurname, insertAuthor1.getName()));
    }

    @Test
    void author__getBySurnameAndName_NonexistentName__emptyListResultException() {
        String nonexistentName = "Smth";
        Assertions.assertEquals(Collections.emptyList()
                , authorRepository.findBySurnameAndName(insertAuthor1.getSurname(), nonexistentName));
    }

    @Test
    void author__getBySurnameAndName_NullSurnameAndName__emptyListResultException() {
        Assertions.assertEquals(Collections.emptyList()
                , authorRepository.findBySurnameAndName(null, null));
    }

    @Test
    void author__getBySurnameAndName_DifferentSurnameAndName__emptyListResultException() {
        Assertions.assertEquals(Collections.emptyList()
                , authorRepository.findBySurnameAndName(insertAuthor1.getSurname(), insertAuthor2.getName()));
    }

    @Test
    void author__getAll__returnListAuthors() {
        List<Author> expected = authorList;
        List<Author> actual = authorRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update__correctChangeAllFieldAuthorById() {
        Integer id = insertAuthor1.getId();
        testAuthor.setId(id);
        Author expected = testAuthor;
        Author actual = authorRepository.save(testAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__incorrectException() {
        testAuthor.setId(100);
        Author actual = authorRepository.save(testAuthor);

        testAuthor.setId(authorList.size() + 1);
        Author expected = testAuthor;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NullSurname__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        testAuthor.setSurname(null);
        Author expected = testAuthor;
        Author actual = authorRepository.save(testAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NullName__incorrectException() {
        testAuthor.setId(insertAuthor1.getId());
        testAuthor.setName(null);
        Author expected = testAuthor;
        Author actual = authorRepository.save(testAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__delete__correctDelete() {
        List<Author> actualBeforeDelete = authorRepository.findAll();
        Assertions.assertNotNull(actualBeforeDelete);

        authorRepository.delete(actualBeforeDelete.get(0));
        authorRepository.delete(actualBeforeDelete.get(1));

        List<Author> expected = Collections.emptyList();
        List<Author> actual = authorRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__delete_NonexistentId__notDelete() {
        List<Author> expected = authorRepository.findAll();
        Assertions.assertNotNull(expected);
        authorRepository.delete(new Author(100, "s", "n"));

        List<Author> actual = authorRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
