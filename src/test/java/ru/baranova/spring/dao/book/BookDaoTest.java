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
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {BookDaoTestConfig.class, StopSearchConfig.class})
public class BookDaoTest {
    @Autowired
    private BookDao bookDaoJdbc;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private Author testAuthor;
    private Genre insertGenre1;
    private Genre insertGenre2;
    private Genre testGenre;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, null, null);
        insertAuthor2 = new Author(2, null, null);
        testAuthor = new Author(1, null, null);

        insertGenre1 = new Genre(1, null, null);
        insertGenre2 = new Genre(2, null, null);
        testGenre = new Genre(1, null, null);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1));
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2));

        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void book__create__correctReturnNewBook() {
        List<Integer> listExistId = bookDaoJdbc.getAll()
                .stream()
                .map(Book::getId)
                .toList();
        Integer expectedId = 1 + listExistId
                .stream()
                .mapToInt(v -> v)
                .max()
                .orElse(insertBook3.getId());

        Integer actualId = bookDaoJdbc.create(testBook.getTitle(), testBook.getAuthor().getId(), testBook.getGenre().stream().map(Genre::getId).toList());

        Book expected = testBook;
        expected.setId(expectedId);
        Book actual = bookDaoJdbc.getById(actualId);

        Assertions.assertFalse(listExistId.contains(actualId));
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertArrayEquals(expected.getGenre().toArray(), actual.getGenre().toArray());
    }

    @Test
    void book__create_NullTitle__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(null, testBook.getAuthor().getId(), List.of(testBook.getGenre().get(0).getId())));
    }

    @Test
    void book__create_NullAuthorId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), null, List.of(testBook.getGenre().get(0).getId())));
    }

    @Test
    void book__create_NullGenreId__incorrectException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> bookDaoJdbc.create(testBook.getTitle(), testBook.getAuthor().getId(), null));
    }

    @Test
    void book__getById__correctReturnBookById() {
        Integer id = insertBook1.getId();
        Book expected = insertBook1;
        Book actual = bookDaoJdbc.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getTitle(), actual.getTitle()),
                () -> Assertions.assertEquals(expected.getAuthor().getSurname(), actual.getAuthor().getSurname()),
                () -> Assertions.assertArrayEquals(expected.getGenre().toArray(), actual.getGenre().toArray()),
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
        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookDaoJdbc.getByTitle(insertBook1.getTitle());
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void book__getByTitle_NullTitle__correctReturnEmptyListBooks() {
        List<Book> expected = new ArrayList<>();
        List<Book> actual = bookDaoJdbc.getByTitle(null);
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void book__getByTitle_NonexistentTitle__correctReturnEmptyListBooks() {
        String nonexistentTitle = "Smth";
        List<Book> expected = new ArrayList<>();
        List<Book> actual = bookDaoJdbc.getByTitle(nonexistentTitle);
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void book__getAll__returnListBooks() {
        List<Book> expected = bookList;
        List<Book> actual = bookDaoJdbc.getAll();
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void book__update__correctChangeAllFieldBookById() {
        Integer id = insertBook3.getId();
        bookDaoJdbc.update(id, testBook.getTitle(), testBook.getAuthor().getId(), testBook.getGenre().stream().map(Genre::getId).toList());
        testBook.setId(id);
        Book expected = testBook;
        Book actual = bookDaoJdbc.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NullTitle__incorrectException() {
        Integer id = insertBook3.getId();
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDaoJdbc.update(id, null, testBook.getAuthor().getId(), List.of(testBook.getGenre().get(0).getId())));
    }

    @Test
    void book__delete__correctDelete() {
        List<Book> actualBeforeDelete = bookDaoJdbc.getAll();
        bookDaoJdbc.delete(insertBook1.getId());
        bookDaoJdbc.delete(insertBook2.getId());
        bookDaoJdbc.delete(insertBook3.getId());

        List<Book> expected = new ArrayList<>();
        List<Book> actual = bookDaoJdbc.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
    }


}
