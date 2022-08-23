package ru.baranova.spring.dao.entity.book;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@DataJpaTest
@Import(value = {BookDaoTestConfig.class, StopSearchConfig.class})
public class BookDaoTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private EntityManager em;
    @Autowired
    private BookDaoTestConfig config;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        //detached entity
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        //merge entity
        insertAuthor1 = em.merge(insertAuthor1);
        insertAuthor2 = em.merge(insertAuthor2);

        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        insertGenre1 = em.merge(insertGenre1);
        insertGenre2 = em.merge(insertGenre2);
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", config.getDateWithoutTime());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", config.getDateWithoutTime());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", config.getDateWithoutTime());
        Comment insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", config.getDateWithoutTime());
        insertComment1 = em.merge(insertComment1);
        insertComment2 = em.merge(insertComment2);
        insertComment3 = em.merge(insertComment3);
        insertComment4 = em.merge(insertComment4);
        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), List.of(insertComment1, insertComment3));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2), List.of(insertComment2));
        insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1), List.of(insertComment4));
        insertBook1 = em.merge(insertBook1);
        insertBook2 = em.merge(insertBook2);
        insertBook3 = em.merge(insertBook3);
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2), List.of(insertComment4));
        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void book__create__correctReturnNewBook() {
        List<Integer> listExistId = bookDao.getAll()
                .stream()
                .map(Book::getId)
                .toList();
        testBook.setAuthor(insertBook1.getAuthor());
        testBook.setGenreList(insertBook1.getGenreList());

        Book expected = testBook;
        Book actual = bookDao.create(testBook.getTitle()
                , testBook.getAuthor()
                , testBook.getGenreList());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertArrayEquals(expected.getGenreList().toArray(), actual.getGenreList().toArray());
    }

    @Test
    void book__create_NonexistentFieldsAuthorAndGenre__correctReturnNewBook() {
        Assertions.assertThrows(PersistenceException.class, () -> bookDao.create(testBook.getTitle()
                , testBook.getAuthor()
                , testBook.getGenreList()));
    }

    @Test
    void book__create_NullTitle__incorrectException() {
        Assertions.assertThrows(
                PersistenceException.class,
                () -> bookDao.create(null, testBook.getAuthor(), testBook.getGenreList()));
    }

    @Test
    void book__create_NullAuthorId__incorrectException() {
        Assertions.assertThrows(
                PersistenceException.class,
                () -> bookDao.create(testBook.getTitle(), null, testBook.getGenreList()));
    }

    @Test
    void book__getById__correctReturnBookById() {
        Integer id = insertBook1.getId();
        Book expected = insertBook1;
        Book actual = bookDao.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(PersistenceException.class, () -> bookDao.getById(nonexistentId));
    }

    @Test
    void book__getByTitle__correctReturnListBooks() {
        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookDao.getByTitle(insertBook1.getTitle());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NullTitle__emptyListResultException() {
        Assertions.assertThrows(DataAccessException.class, () -> bookDao.getByTitle(null));

    }

    @Test
    void book__getByTitle_NonexistentTitle__emptyListResultException() {
        String nonexistentTitle = "Smth";
        Assertions.assertThrows(DataAccessException.class, () -> bookDao.getByTitle(nonexistentTitle));
    }

    @Test
    void book__getByTitleAndAuthor() {
        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookDao.getByTitleAndAuthor(insertBook1.getTitle(), insertBook1.getAuthor().getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor__emptyListResultException() {
        Assertions.assertThrows(DataAccessException.class,
                () -> bookDao.getByTitleAndAuthor(insertBook2.getTitle(), insertBook3.getAuthor().getId()));
    }

    @Test
    void book__getByTitleAndAuthor_NullTitle__emptyListResultException() {
        Assertions.assertThrows(DataAccessException.class,
                () -> bookDao.getByTitleAndAuthor(null, insertBook1.getAuthor().getId()));

    }

    @Test
    void book__getByTitleAndAuthor_NullAuthorId__emptyListResultException() {
        Assertions.assertThrows(DataAccessException.class,
                () -> bookDao.getByTitleAndAuthor(insertBook1.getTitle(), null));
    }

    @Test
    void book__getAll__returnListBooks() {
        List<Book> expected = bookList;
        List<Book> actual = bookDao.getAll();
        Assertions.assertEquals(expected.get(2).getCommentList(), actual.get(2).getCommentList());
    }

    @Test
    void book__update__correctChangeAllFieldBookById() {
        Integer id = insertBook3.getId();
        testBook.setId(id);
        Book expected = testBook;
        bookDao.update(testBook
                , testBook.getTitle()
                , testBook.getAuthor()
                , testBook.getGenreList());
        Book actual = bookDao.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NonexistentId__incorrectException() {
        testBook.setId(100);
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.update(testBook, testBook.getTitle(), testBook.getAuthor(), testBook.getGenreList()));
    }

    @Test
    void book__update_NullTitle__incorrectException() {
        Integer id = insertBook3.getId();
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.update(testBook, null, testBook.getAuthor(), testBook.getGenreList()));
    }

    @Test
    void book__delete__correctDelete() {
        List<Book> actualBeforeDelete = bookDao.getAll();
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
        List<Book> actualBeforeDelete = bookDao.getAll();
        Integer inputId = actualBeforeDelete.size() + 1;
        Integer inputId2 = actualBeforeDelete.size() + 2;
        Integer inputId3 = actualBeforeDelete.size() + 3;

        List<Book> expected = actualBeforeDelete;
        List<Book> actual = bookDao.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId2));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookDao.delete(inputId3));
        Assertions.assertEquals(expected, actual);
    }

}
