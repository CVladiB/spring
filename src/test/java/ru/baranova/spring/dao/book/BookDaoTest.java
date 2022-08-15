package ru.baranova.spring.dao.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

import java.util.Collections;
import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {BookDaoTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
public class BookDaoTest {
    @Autowired
    private BookDao bookDao;
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
        List<Integer> listExistId = bookDao.getAll()
                .stream()
                .map(BookEntity::getId)
                .toList();

        BookEntity expected = testBook;
        BookEntity actual = bookDao.create(testBook.getTitle()
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
                () -> bookDao.create(null, testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__create_NullAuthorId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.create(testBook.getTitle(), null, testBook.getGenreListId()));
    }

    @Test
    void book__create_NonexistentAuthorId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.create(testBook.getTitle(), 100, testBook.getGenreListId()));
    }

    @Test
    void book__create_NullGenreId__incorrectException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> bookDao.create(testBook.getTitle(), testBook.getAuthorId(), null));
    }

    @Test
    void book__create_NonexistentGenreId__incorrectException() {
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.create(testBook.getTitle(), testBook.getAuthorId(), List.of(100)));
    }

    @Test
    void book__getById__correctReturnBookById() {
        Integer id = insertBook1.getId();
        BookEntity expected = insertBook1;
        BookEntity actual = bookDao.getById(id);
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
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(nonexistentId));
    }

    @Test
    void book__getByTitle__correctReturnListBooks() {
        insertBook1.setGenreListId(null);
        List<BookEntity> expected = List.of(insertBook1);
        List<BookEntity> actual = bookDao.getByTitle(insertBook1.getTitle());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NullTitle__correctReturnEmptyListBooks() {
        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookDao.getByTitle(null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NonexistentTitle__correctReturnEmptyListBooks() {
        String nonexistentTitle = "Smth";
        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookDao.getByTitle(nonexistentTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor__correctReturnListBooks() {
        insertBook1.setGenreListId(null);
        List<BookEntity> expected = List.of(insertBook1);
        List<BookEntity> actual = bookDao.getByTitleAndAuthor(insertBook1.getTitle(), insertBook1.getAuthorId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor_NullTitle__correctReturnEmptyListBooks() {
        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookDao.getByTitleAndAuthor(null, insertBook1.getAuthorId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor_NullAuthorId__correctReturnEmptyListBooks() {
        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookDao.getByTitleAndAuthor(insertBook1.getTitle(), null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getAll__returnListBooks() {
        bookList.forEach(book -> book.setGenreListId(null));
        List<BookEntity> expected = bookList;
        List<BookEntity> actual = bookDao.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update__correctChangeAllFieldBookById() {
        Integer id = insertBook3.getId();
        testBook.setId(id);
        BookEntity expected = testBook;
        BookEntity actual = bookDao.update(id
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
                () -> bookDao.update(testBook.getId(), testBook.getTitle(), testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__update_NullTitle__incorrectException() {
        Integer id = insertBook3.getId();
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.update(id, null, testBook.getAuthorId(), testBook.getGenreListId()));
    }

    @Test
    void book__delete__correctDelete() {
        List<BookEntity> actualBeforeDelete = bookDao.getAll();
        Integer inputId = insertBook1.getId();
        Integer inputId2 = insertBook2.getId();
        Integer inputId3 = insertBook3.getId();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertTrue(bookDao.delete(inputId));
        Assertions.assertTrue(bookDao.delete(inputId2));
        Assertions.assertTrue(bookDao.delete(inputId3));
    }

    @Test
    void book__delete_NonexistentId__notDelete() {
        List<BookEntity> actualBeforeDelete = bookDao.getAll();
        Integer inputId = actualBeforeDelete.size() + 1;
        Integer inputId2 = actualBeforeDelete.size() + 2;
        Integer inputId3 = actualBeforeDelete.size() + 3;

        List<BookEntity> expected = actualBeforeDelete;
        List<BookEntity> actual = bookDao.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId2));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId3));
        Assertions.assertEquals(expected, actual);
    }

}
