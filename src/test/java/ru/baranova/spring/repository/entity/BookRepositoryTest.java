package ru.baranova.spring.repository.entity;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private EntityManager em;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private Book testBook;
    private List<Book> bookList;
    private Date dateWithoutTime;


    @BeforeEach
    void setUp() throws ParseException {
        dateWithoutTime = sdf.parse(sdf.format(new Date()));

        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        insertAuthor1 = em.merge(insertAuthor1);
        insertAuthor2 = em.merge(insertAuthor2);

        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        insertGenre1 = em.merge(insertGenre1);
        insertGenre2 = em.merge(insertGenre2);

        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", dateWithoutTime);
        Comment insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
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
        List<Integer> listExistId = bookRepository.findAll()
                .stream()
                .map(Book::getId)
                .toList();
        testBook.setAuthor(insertBook1.getAuthor());
        testBook.setGenreList(insertBook1.getGenreList());

        Book expected = testBook;
        Book actual = bookRepository.save(testBook);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertArrayEquals(expected.getGenreList().toArray(), actual.getGenreList().toArray());
    }

    @Test
    void book__create_NonexistentFieldsAuthorAndGenre__correctReturnNewBook() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> bookRepository.save(testBook));
    }

    @Test
    void book__create_NullTitle__incorrectException() {
        testBook.setTitle(null);
        Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> bookRepository.save(testBook));
    }

    @Test
    void book__create_NullAuthorId__incorrectException() {
        testBook.setAuthor(null);
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> bookRepository.save(testBook));
    }

    @Test
    void book__findById__correctReturnBookById() {
        Integer id = insertBook1.getId();
        Book expected = insertBook1;
        Book actual = bookRepository.findById(id).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__findById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertEquals(Optional.empty(), bookRepository.findById(nonexistentId));
    }

    @Test
    void book__getByTitle__correctReturnListBooks() {
        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookRepository.findByTitle(insertBook1.getTitle());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NullTitle__emptyListResultException() {
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.findByTitle(null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitle_NonexistentTitle__emptyListResultException() {
        String nonexistentTitle = "Smth";
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.findByTitle(nonexistentTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor() {
        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookRepository.findByTitleAndAuthor(insertBook1.getTitle(), insertBook1.getAuthor().getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor__emptyListResultException() {
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.findByTitleAndAuthor(insertBook2.getTitle(), insertBook3.getAuthor().getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getByTitleAndAuthor_NullTitle__emptyListResultException() {
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.findByTitleAndAuthor(null, insertBook3.getAuthor().getId());
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void book__getByTitleAndAuthor_NullAuthorId__emptyListResultException() {
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.findByTitleAndAuthor(insertBook2.getTitle(), null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__getAll__returnListBooks() {
        List<Book> expected = bookList;
        List<Book> actual = bookRepository.findAll();
        Assertions.assertEquals(expected.get(2).getCommentList(), actual.get(2).getCommentList());
    }

    @Test
    void book__getAll_EmptyList__emptyListResultException() {
        bookRepository.deleteAll(bookRepository.findAll());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookRepository.findAll());
    }

    @Test
    void book__update__correctChangeAllFieldBookById() {
        Integer id = insertBook3.getId();
        testBook.setId(id);
        testBook.setCommentList(insertBook3.getCommentList());
        Book expected = testBook;
        Book actual = bookRepository.save(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NonexistentId__incorrectException() {
        testBook.setId(100);
        testBook.setCommentList(new ArrayList<>());
        Book actual = bookRepository.save(testBook);

        testBook.setId(bookList.size() + 1);
        Book expected = testBook;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__delete__correctDelete() {
        List<Book> actualBeforeDelete = bookRepository.findAll();
        Assertions.assertNotNull(actualBeforeDelete);

        bookRepository.delete(actualBeforeDelete.get(0));
        bookRepository.delete(actualBeforeDelete.get(1));
        bookRepository.delete(actualBeforeDelete.get(2));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookRepository.findAll());
    }

    @Test
    void book__delete_NonexistentId__notDelete() {
        List<Book> expected = bookRepository.findAll();
        Assertions.assertNotNull(expected);
        bookRepository.delete(new Book(100, "t", new Author(), Collections.emptyList(), Collections.emptyList()));

        List<Book> actual = bookRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

}
