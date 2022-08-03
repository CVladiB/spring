package ru.baranova.spring.dao.book;

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
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {BookDaoTestConfig.class, StopSearchConfig.class})
public class BookDaoTest {
    @Autowired
    private BookDao bookDaoJdbc;
    private BookEntity insertBook1;
    private BookEntity insertBook2;
    private BookEntity insertBook3;
    private BookEntity testBook;
    private List<BookEntity> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);

        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Genre testGenre = new Genre(1, null, null);

        insertBook1 = new BookEntity(1, "Title1", insertAuthor1.getId(), List.of(insertGenre1.getId(), insertGenre2.getId()));
        insertBook2 = new BookEntity(2, "Title2", insertAuthor1.getId(), List.of(insertGenre2.getId()));
        insertBook3 = new BookEntity(3, "Title3", insertAuthor2.getId(), List.of(insertGenre1.getId()));
        testBook = new BookEntity(null, "TitleTest", testAuthor.getId(), List.of(insertGenre2.getId()));
        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void book__create__correctReturnNewBook() {
        List<Integer> listExistId = bookDaoJdbc.getAll()
                .stream()
                .map(BookEntity::getId)
                .toList();

        BookEntity expected = testBook;
        BookEntity actual = bookDaoJdbc.create(testBook.getTitle()
                , testBook.getAuthorId()
                , testBook.getGenreListId());


        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthorId(), actual.getAuthorId());
        Assertions.assertArrayEquals(expected.getGenreListId().toArray(), actual.getGenreListId().toArray());
    }

    @Test
    void book__create_NullTitle__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(null, testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__create_NullAuthorId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), null, testBook.getGenreListId()));
    }

    @Test
    void book__create_NonexistentAuthorId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), 100, testBook.getGenreListId()));
    }

    @Test
    void book__create_NullGenreId__incorrectException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), testBook.getAuthorId(), null));
    }

    @Test
    void book__create_NonexistentGenreId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), testBook.getAuthorId(), List.of(100)));
    }

    @Test
    void book__getById__correctReturnBookById() {
        Integer id = insertBook1.getId();
        BookEntity expected = insertBook1;
        BookEntity actual = bookDaoJdbc.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getTitle(), actual.getTitle()),
                () -> Assertions.assertEquals(expected.getAuthorId(), actual.getAuthorId()),
                () -> Assertions.assertArrayEquals(expected.getGenreListId().toArray(), actual.getGenreListId().toArray()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void book__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> bookDaoJdbc.getById(nonexistentId));
    }

    @Test
    void book__getByTitle__correctReturnListBooks() {
        insertBook1.setGenreListId(null);
        List<BookEntity> expected = List.of(insertBook1);
        List<BookEntity> actual = bookDaoJdbc.getByTitle(insertBook1.getTitle());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NullTitle__correctReturnEmptyListBooks() {
        List<BookEntity> expected = new ArrayList<>();
        List<BookEntity> actual = bookDaoJdbc.getByTitle(null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NonexistentTitle__correctReturnEmptyListBooks() {
        String nonexistentTitle = "Smth";
        List<BookEntity> expected = new ArrayList<>();
        List<BookEntity> actual = bookDaoJdbc.getByTitle(nonexistentTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getAll__returnListBooks() {
        bookList.forEach(book -> book.setGenreListId(null));
        List<BookEntity> expected = bookList;
        List<BookEntity> actual = bookDaoJdbc.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update__correctChangeAllFieldBookById() {
        Integer id = insertBook3.getId();
        testBook.setId(id);
        BookEntity expected = testBook;
        BookEntity actual = bookDaoJdbc.update(id
                , testBook.getTitle()
                , testBook.getAuthorId()
                , testBook.getGenreListId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NonexistentId__incorrectException() {
        testBook.setId(100);
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.update(testBook.getId(), testBook.getTitle(), testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__update_NullTitle__incorrectException() {
        Integer id = insertBook3.getId();
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.update(id, null, testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__delete__correctDelete() {
        int countAffectedRowsExpected = 3;
        int countAffectedRowsActual;

        List<BookEntity> actualBeforeDelete = bookDaoJdbc.getAll();
        countAffectedRowsActual = bookDaoJdbc.delete(insertBook1.getId());
        countAffectedRowsActual += bookDaoJdbc.delete(insertBook2.getId());
        countAffectedRowsActual += bookDaoJdbc.delete(insertBook3.getId());

        List<BookEntity> expected = new ArrayList<>();
        List<BookEntity> actual = bookDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }

    @Test
    void book__delete_NonexistentId__notDelete() {
        int countAffectedRowsExpected = 0;
        int countAffectedRowsActual;

        List<BookEntity> actualBeforeDelete = bookDaoJdbc.getAll();
        countAffectedRowsActual = bookDaoJdbc.delete(actualBeforeDelete.size() + 1);
        countAffectedRowsActual += bookDaoJdbc.delete(actualBeforeDelete.size() + 2);
        countAffectedRowsActual += bookDaoJdbc.delete(actualBeforeDelete.size() + 3);

        List<BookEntity> expected = actualBeforeDelete;
        List<BookEntity> actual = bookDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(countAffectedRowsExpected, countAffectedRowsActual);
    }

}
